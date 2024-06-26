package org.bananalaba.datatraining.aws.testdata.application;

import static org.apache.commons.lang3.Validate.notBlank;

import java.io.FileInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.bananalaba.datatraining.aws.testdata.definition.DataGenerationTask;
import org.bananalaba.datatraining.aws.testdata.definition.KinesisMetricsGeneratorTask;
import org.bananalaba.datatraining.aws.testdata.definition.RealtimeTimerDefinition;
import org.bananalaba.datatraining.aws.testdata.definition.ServerAccessLogGenerationTask;
import org.bananalaba.datatraining.aws.testdata.definition.SyntheticTimerDefinition;
import org.bananalaba.datatraining.aws.testdata.definition.TimerDefinition;
import org.bananalaba.datatraining.aws.testdata.definition.WindowedMetricsGenerationTask;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;
import org.bananalaba.datatraining.aws.testdata.factory.EventSinkFactory;
import org.bananalaba.datatraining.aws.testdata.factory.GenericDispatcher;
import org.bananalaba.datatraining.aws.testdata.factory.GenericFactory;
import org.bananalaba.datatraining.aws.testdata.factory.MetricsTemplateFactory;
import org.bananalaba.datatraining.aws.testdata.factory.RealtimeTimerFactory;
import org.bananalaba.datatraining.aws.testdata.factory.ServiceLoadCompiler;
import org.bananalaba.datatraining.aws.testdata.factory.SyntheticTimerFactory;
import org.bananalaba.datatraining.aws.testdata.factory.WindowedMetricsTemplateFactory;

@Slf4j
public class TestDataGeneratorApplication {

    private final ObjectMapper jsonMapper;
    private final TestDataTaskParser taskParser;

    private final GenericDispatcher<DataGenerationTask> dispatcher;

    public TestDataGeneratorApplication() {
        System.setProperty("com.amazonaws.sdk.disableCbor", "true");

        jsonMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        taskParser = new TestDataTaskParser(jsonMapper, System::getProperty);

        var timerFactory = GenericFactory.builder(TimerDefinition.class, EventTimer.class)
            .register(SyntheticTimerDefinition.class, new SyntheticTimerFactory())
            .register(RealtimeTimerDefinition.class, new RealtimeTimerFactory())
            .build();
        var sinkFactory = new EventSinkFactory(jsonMapper);

        this.dispatcher = GenericDispatcher.builder(DataGenerationTask.class)
            .register(
                WindowedMetricsGenerationTask.class,
                new WindowedMetricsGeneratorRunner(
                    timerFactory, new WindowedMetricsTemplateFactory(), sinkFactory, 10000)
            )
            .register(
                ServerAccessLogGenerationTask.class,
                new ServerAccessLogGeneratorRunner(
                    timerFactory, new ServiceLoadCompiler(), sinkFactory, 150000)
            )
            .register(
                KinesisMetricsGeneratorTask.class,
                new KinesisMetricsGeneratorRunner(
                    timerFactory, new MetricsTemplateFactory(), sinkFactory, 100)
            )
            .build();
    }

    public void run(String taskDefinitionFilePath) {
        notBlank(taskDefinitionFilePath, "task definition file path required");

        log.info("loading tasks from " + taskDefinitionFilePath);
        try (var input = new FileInputStream(taskDefinitionFilePath)) {
            var taskDefinition = taskParser.parse(input);

            log.info("executing the task");
            dispatcher.run(taskDefinition);
        } catch (Exception e) {
            throw new TestDataGeneratorConfigException("data generation failed", e);
        }
    }

}

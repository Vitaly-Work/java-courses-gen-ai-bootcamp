package org.bananalaba.datatraining.aws.testdata.application;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bananalaba.datatraining.aws.testdata.definition.TimerDefinition;
import org.bananalaba.datatraining.aws.testdata.definition.WindowedMetricsGenerationTask;
import org.bananalaba.datatraining.aws.testdata.event.EventGenerator;
import org.bananalaba.datatraining.aws.testdata.event.source.WindowedEventSource;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;
import org.bananalaba.datatraining.aws.testdata.factory.EventSinkFactory;
import org.bananalaba.datatraining.aws.testdata.factory.GenericFactory;
import org.bananalaba.datatraining.aws.testdata.factory.WindowedMetricsTemplateFactory;

@RequiredArgsConstructor
@Slf4j
public class WindowedMetricsGeneratorRunner implements Consumer<WindowedMetricsGenerationTask> {

    @NonNull
    private final GenericFactory<TimerDefinition, EventTimer> timerFactory;
    @NonNull
    private final WindowedMetricsTemplateFactory templateFactory;
    @NonNull
    private final EventSinkFactory sinkFactory;
    private final int maxNumberOfEvents;

    @Override
    public void accept(final WindowedMetricsGenerationTask task) {
        notNull(task, "task cannot be null");
        if (task.getRandomSeed() != null) {
            log.info("using fixed random seed: " + task.getRandomSeed());
        }

        var timer = timerFactory.create(task.getTimer());
        var templates = templateFactory.createEventTemplates(task.getMetrics(), task.getRandomSeed());
        var source = new WindowedEventSource<>(timer, templates);

        var eventsNumber = source.estimateNumberOfEvents(task.getNumberOfIterations());
        checkArgument(eventsNumber <= maxNumberOfEvents, "estimated number of events = " + eventsNumber + " > "
            + maxNumberOfEvents + " which might lead to unwanted AWS expenses");

        var sink = sinkFactory.createCsvMetricsWindowSink(task.getOutputFilePath());

        new EventGenerator(source, sink).generate(task.getNumberOfIterations());
        sink.close();
    }

}

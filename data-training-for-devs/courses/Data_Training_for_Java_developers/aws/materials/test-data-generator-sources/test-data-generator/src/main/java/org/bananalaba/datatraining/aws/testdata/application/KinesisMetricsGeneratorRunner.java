package org.bananalaba.datatraining.aws.testdata.application;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bananalaba.datatraining.aws.testdata.definition.KinesisMetricsGeneratorTask;
import org.bananalaba.datatraining.aws.testdata.definition.TimerDefinition;
import org.bananalaba.datatraining.aws.testdata.event.EventGenerator;
import org.bananalaba.datatraining.aws.testdata.event.source.BasicEventSource;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;
import org.bananalaba.datatraining.aws.testdata.factory.EventSinkFactory;
import org.bananalaba.datatraining.aws.testdata.factory.GenericFactory;
import org.bananalaba.datatraining.aws.testdata.factory.MetricsTemplateFactory;

@RequiredArgsConstructor
@Slf4j
public class KinesisMetricsGeneratorRunner implements Consumer<KinesisMetricsGeneratorTask> {

    @NonNull
    private final GenericFactory<TimerDefinition, EventTimer> timerFactory;
    @NonNull
    private final MetricsTemplateFactory templateFactory;
    @NonNull
    private final EventSinkFactory sinkFactory;
    private final int maxNumberOfEvents;

    @Override
    public void accept(KinesisMetricsGeneratorTask task) {
        notNull(task, "task required");

        var timer = timerFactory.create(task.getTimer());
        var templates = templateFactory.createEventTemplates(task.getMetrics(), task.getRandomSeed());
        var sources = templates.values()
            .stream()
            .map(metrics -> new BasicEventSource<>(timer, metrics))
            .toList();

        var eventsNumber = sources.stream()
            .map(source -> source.estimateNumberOfEvents(task.getIterationsNumber()))
            .reduce(0, Integer::sum);
        checkArgument(eventsNumber <= maxNumberOfEvents, "estimated number of events = " + eventsNumber + " > "
            + maxNumberOfEvents + " which might lead to unwanted Kinesis expenses");

        var sink = sinkFactory.kinesisSink(task.getAuth(), task.getStream());

        var executor = Executors.newFixedThreadPool(sources.size());
        var activities = sources.stream()
            .map(source ->
                executor.submit(() -> new EventGenerator(source, sink).generate(task.getIterationsNumber())))
            .toList();

        log.info("waiting for all events being published...");
        try {
            activities.forEach(this::join);
        } finally {

            executor.shutdown();
            try {
                while (!executor.awaitTermination(10000L, TimeUnit.MILLISECONDS)) {
                    log.info("waiting for all events being published...");
                }
            } catch (InterruptedException e) {
                log.error("couldn't dispose the streaming job pool", e);
            }

            sink.close();
        }
    }

    private void join(Future<?> activity) {
        try {
            activity.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new TestDataGeneratorExecutionException("failed a streaming job", e);
        }
    }

}

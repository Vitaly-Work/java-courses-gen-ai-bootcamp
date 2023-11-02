package org.bananalaba.datatraining.aws.testdata.application;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Random;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bananalaba.datatraining.aws.testdata.definition.ServerAccessLogGenerationTask;
import org.bananalaba.datatraining.aws.testdata.definition.TimerDefinition;
import org.bananalaba.datatraining.aws.testdata.event.EventGenerator;
import org.bananalaba.datatraining.aws.testdata.event.RandomValueGenerator;
import org.bananalaba.datatraining.aws.testdata.event.source.CompositeEventSource;
import org.bananalaba.datatraining.aws.testdata.event.source.ServerAccessLogSource;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;
import org.bananalaba.datatraining.aws.testdata.factory.EventSinkFactory;
import org.bananalaba.datatraining.aws.testdata.factory.GenericFactory;
import org.bananalaba.datatraining.aws.testdata.factory.ServiceLoadCompiler;

@RequiredArgsConstructor
@Slf4j
public class ServerAccessLogGeneratorRunner implements Consumer<ServerAccessLogGenerationTask> {

    @NonNull
    private final GenericFactory<TimerDefinition, EventTimer> timerFactory;
    @NonNull
    private final ServiceLoadCompiler loadCompiler;
    @NonNull
    private final EventSinkFactory sinkFactory;
    private final int maxNumberOfEvents;

    @Override
    public void accept(ServerAccessLogGenerationTask task) {
        notNull(task, "task cannot be null");

        var random = new Random();
        if (task.getRandomSeed() != null) {
            log.info("using fixed random seed: " + task.getRandomSeed());
            random.setSeed(task.getRandomSeed());
        }
        var networkLag = new RandomValueGenerator(random, task.getNetworkLagMinMillis(), task.getNetworkLagMaxMillis());

        var load = loadCompiler.compile(task.getServices(), task.getLoadItems());
        var sources = load.stream()
            .map(loadItem -> ServerAccessLogSource.builder()
                .root(loadItem.getTarget())
                .userAgent(loadItem.getUserAgent())
                .remoteUser(loadItem.getRemoteUser())
                .userAddress(loadItem.getUserAddress())
                .timer(timerFactory.create(task.getTimer()))
                .networkLag(networkLag)
                .build()
            )
            .toList();
        var source = new CompositeEventSource<>(sources);

        var eventsNumber = source.estimateNumberOfEvents(task.getNumberOfIterations());
        checkArgument(eventsNumber <= maxNumberOfEvents, "estimated number of events = " + eventsNumber + " > "
            + maxNumberOfEvents + " which might lead to unwanted AWS expenses");

        var sink = sinkFactory.createTxtServerLogSink(task.getOutputDirectoryPath());

        new EventGenerator(source, sink).generate(task.getNumberOfIterations());
        sink.close();
    }

}

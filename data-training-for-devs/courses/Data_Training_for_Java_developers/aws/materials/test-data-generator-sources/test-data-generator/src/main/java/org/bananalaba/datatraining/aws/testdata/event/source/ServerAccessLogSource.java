package org.bananalaba.datatraining.aws.testdata.event.source;

import static com.google.common.base.Preconditions.checkArgument;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import org.bananalaba.datatraining.aws.testdata.event.RandomValueGenerator;
import org.bananalaba.datatraining.aws.testdata.event.ServerAccessLogEntry;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;
import org.bananalaba.datatraining.aws.testdata.factory.ServiceCallGraph;

@Builder
public class ServerAccessLogSource implements EventSource<ServerAccessLogEntry> {

    @NonNull
    private final EventTimer timer;
    @NonNull
    private final RandomValueGenerator networkLag;

    @NonNull
    private final String remoteUser;
    @NonNull
    private final String userAgent;
    @NonNull
    private final String userAddress;

    private final ServiceCallGraph root;

    @Override
    public List<ServerAccessLogEntry> emit() {
        return emit(timer.nextTime(), remoteUser, userAgent, userAddress, List.of(root));
    }

    private List<ServerAccessLogEntry> emit(
            Instant time, String remoteUser, String userAgent, String remoteAddress, List<ServiceCallGraph> targets) {
        return targets.stream()
            .flatMap(target -> emit(time, remoteUser, userAgent, remoteAddress, target).stream())
            .toList();
    }

    private List<ServerAccessLogEntry> emit(
            Instant time, String remoteUser, String userAgent, String remoteAddress, ServiceCallGraph target) {
        var callArrivalTime = time.plus((long) networkLag.generate(), ChronoUnit.MILLIS);
        var logEntries = new ArrayList<ServerAccessLogEntry>();
        logEntries.add(ServerAccessLogEntry.builder()
            .serviceName(target.getServiceName())
            .time(time)
            .userAgent(userAgent)
            .remoteAddress(remoteAddress)
            .remoteUser(remoteUser)
            .request(target.getRequest())
            .status(target.getStatus())
            .build()
        );

        logEntries.addAll(emit(
            callArrivalTime,
            target.getServiceName(),
            target.getServiceAgent(),
            target.getServiceAddress(),
            target.getDependencies()
        ));

        return logEntries;
    }

    @Override
    public int estimateNumberOfEvents(int iterationsNumber) {
        checkArgument(iterationsNumber >= 0, "iterationsNumber must be >= 0");
        return iterationsNumber * estimateNumberOfEvents(root);
    }

    private int estimateNumberOfEvents(ServiceCallGraph graph) {
        var result = graph.getDependencies()
            .stream()
            .map(this::estimateNumberOfEvents)
            .reduce(0, Integer::sum);

        return result + 1;
    }

}

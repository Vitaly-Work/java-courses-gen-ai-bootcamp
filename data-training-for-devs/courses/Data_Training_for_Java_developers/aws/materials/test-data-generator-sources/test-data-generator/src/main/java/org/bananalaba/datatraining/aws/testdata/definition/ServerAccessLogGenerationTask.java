package org.bananalaba.datatraining.aws.testdata.definition;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Builder
public class ServerAccessLogGenerationTask implements DataGenerationTask {

    private final List<ServiceDefinition> services;
    private final List<ServiceLoadDefinition> loadItems;
    private final Integer numberOfIterations;

    private final Integer networkLagMinMillis;
    private final Integer networkLagMaxMillis;
    private final SyntheticTimerDefinition timer;
    private final Long randomSeed;

    private final String outputDirectoryPath;

    @Builder
    public ServerAccessLogGenerationTask(@NonNull List<ServiceDefinition> services,
            @NonNull List<ServiceLoadDefinition> loadItems,
            @NonNull Integer numberOfIterations,
            @NonNull Integer networkLagMinMillis,
            @NonNull Integer networkLagMaxMillis,
            @NonNull SyntheticTimerDefinition timer,
            Long randomSeed,
            @NonNull String outputDirectoryPath) {
        this.services = services;
        this.loadItems = loadItems;

        checkArgument(numberOfIterations > 0, "number of iterations must be > 0");
        this.numberOfIterations = numberOfIterations;

        checkArgument(networkLagMinMillis >= 0, "network lag min must be >= 0");
        this.networkLagMinMillis = networkLagMinMillis;
        checkArgument(networkLagMaxMillis >= 0, "network lag max must be >= 0");
        this.networkLagMaxMillis = networkLagMaxMillis;
        checkArgument(networkLagMaxMillis >= networkLagMinMillis, "network lag max must be >= min");

        this.timer = timer;
        this.randomSeed = randomSeed;

        this.outputDirectoryPath = outputDirectoryPath;
    }
}

package org.bananalaba.datatraining.aws.testdata.definition;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class KinesisMetricsGeneratorTask implements DataGenerationTask {

    private final Long randomSeed;

    private final AwsApiAuthDefinition auth;
    @NonNull
    private final KinesisStreamDefinition stream;

    @NonNull
    private final Integer iterationsNumber;
    @NonNull
    private final RealtimeTimerDefinition timer;

    @NonNull
    private final Map<String, Map<String, MetricDefinition>> metrics;

}

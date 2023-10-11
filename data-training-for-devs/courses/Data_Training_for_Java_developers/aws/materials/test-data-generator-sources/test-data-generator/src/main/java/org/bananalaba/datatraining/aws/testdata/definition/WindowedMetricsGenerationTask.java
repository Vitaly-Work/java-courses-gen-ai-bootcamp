package org.bananalaba.datatraining.aws.testdata.definition;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class WindowedMetricsGenerationTask implements DataGenerationTask {

    private final Long randomSeed;

    private final Map<String, Map<String, MetricAggregateDefinition>> metrics;
    private final Integer numberOfIterations;

    private final SyntheticTimerDefinition timer;

    private final String outputFilePath;

}

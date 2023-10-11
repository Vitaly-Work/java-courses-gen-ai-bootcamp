package org.bananalaba.datatraining.aws.testdata.definition;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@ToString
@Jacksonized
public class MetricAggregateDefinition {

    @NonNull
    private final String unit;

    @NonNull
    private final Double averageFrom;
    @NonNull
    private final Double averageTo;

    @NonNull
    private final Double minDiff;
    @NonNull
    private final Double maxDiff;

}

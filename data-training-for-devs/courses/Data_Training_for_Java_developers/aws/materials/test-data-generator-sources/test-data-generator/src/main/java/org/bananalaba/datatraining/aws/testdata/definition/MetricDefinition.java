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
public class MetricDefinition {

    @NonNull
    private final String unit;

    @NonNull
    private final Double minValue;
    @NonNull
    private final Double maxValue;

}

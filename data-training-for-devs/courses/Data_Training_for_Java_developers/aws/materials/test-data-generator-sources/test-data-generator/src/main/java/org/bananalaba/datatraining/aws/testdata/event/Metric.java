package org.bananalaba.datatraining.aws.testdata.event;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@ToString
@Getter
@Builder
@Jacksonized
public class Metric implements Event {

    @NonNull
    private final String metricName;
    @NonNull
    private final String componentName;
    @NonNull
    private final Instant publicationTimestamp;

    @NonNull
    private final Double value;
    @NonNull
    private final String unit;

}

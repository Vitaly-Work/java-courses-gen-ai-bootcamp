package org.bananalaba.datatraining.aws.testdata.definition;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class RealtimeTimerDefinition implements TimerDefinition {

    private final Instant startTime;
    private final Long rateMillis;

}

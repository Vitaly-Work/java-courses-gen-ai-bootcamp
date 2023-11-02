package org.bananalaba.datatraining.aws.testdata.definition;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class SyntheticTimerDefinition implements TimerDefinition {

    @NonNull
    private final Instant startTime;
    @NonNull
    private final Long stepMillis;

}

package org.bananalaba.datatraining.aws.testdata.event.time;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.Validate.notNull;

import java.time.Instant;

public class SyntheticEventTimer implements EventTimer {

    private Instant currentTime;
    private final long stepMillis;

    public SyntheticEventTimer(Instant startTime, long stepMillis) {
        currentTime = notNull(startTime, "start time required");

        checkArgument(stepMillis > 0, "step must be > 0");
        this.stepMillis = stepMillis;
    }

    @Override
    public Instant nextTime() {
        var result = currentTime;
        currentTime = currentTime.plusMillis(stepMillis);

        return result;
    }

    @Override
    public long estimateRealTimeDelayMillis() {
        return 0;
    }

}

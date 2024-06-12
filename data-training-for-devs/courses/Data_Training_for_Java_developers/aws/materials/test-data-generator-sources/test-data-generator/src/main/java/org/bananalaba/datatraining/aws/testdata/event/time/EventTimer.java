package org.bananalaba.datatraining.aws.testdata.event.time;

import java.time.Instant;

public interface EventTimer {

    Instant nextTime();

    long estimateRealTimeDelayMillis();

}

package org.bananalaba.datatraining.aws.testdata.event.time;

import java.time.Instant;
import lombok.NonNull;

public class RealtimeTimer implements EventTimer {

    private final SystemTime systemTime;
    private final Instant startTime;
    private final Long rateMillis;

    private final ThreadLocal<Long> logicalTime;

    public RealtimeTimer(Instant startTime, Long rateMillis) {
        this(
            Thread::sleep,
            startTime,
            rateMillis
        );
    }

    RealtimeTimer(@NonNull SystemTime systemTime, @NonNull Instant startTime, @NonNull Long rateMillis) {
        this.systemTime = systemTime;
        this.startTime = startTime;
        this.rateMillis = rateMillis;

        logicalTime = new ThreadLocal<>();
    }

    @Override
    public Instant nextTime() {
        var currentTime = logicalTime.get();
        if (currentTime == null) {
            logicalTime.set(startTime.toEpochMilli());
            return startTime;
        }

        try {
            systemTime.sleep(rateMillis);
        } catch (InterruptedException e) {
            throw new EventTimingException("failed to tick", e);
        }

        currentTime += rateMillis;
        logicalTime.set(currentTime);
        return Instant.ofEpochMilli(currentTime);
    }

    @Override
    public long estimateRealTimeDelayMillis() {
        return rateMillis;
    }

    interface SystemTime {

        void sleep(long millis) throws InterruptedException;

    }

}

package org.bananalaba.datatraining.aws.testdata.event.time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SyntheticEventTimerTest {

    @Test
    public void shouldNotAllowNullStartTime() {
        assertThatThrownBy(() -> new SyntheticEventTimer(null, 100))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldNotAllowNegativeStep() {
        assertThatThrownBy(() -> new SyntheticEventTimer(Instant.EPOCH, -100))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldNotAllowZeroStep() {
        assertThatThrownBy(() -> new SyntheticEventTimer(Instant.EPOCH, 0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldGenerateTime() {
        var timer = new SyntheticEventTimer(Instant.ofEpochMilli(165984560), 300);

        var actual = List.of(
            timer.nextTime(),
            timer.nextTime(),
            timer.nextTime()
        );
        var expected = List.of(
            Instant.ofEpochMilli(165984560),
            Instant.ofEpochMilli(165984860),
            Instant.ofEpochMilli(165985160)
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldEstimateRealtimeDelayAsZero() {
        var timer = new SyntheticEventTimer(Instant.ofEpochMilli(165984560), 300);

        var actual = timer.estimateRealTimeDelayMillis();
        assertThat(actual).isEqualTo(0);
    }

}

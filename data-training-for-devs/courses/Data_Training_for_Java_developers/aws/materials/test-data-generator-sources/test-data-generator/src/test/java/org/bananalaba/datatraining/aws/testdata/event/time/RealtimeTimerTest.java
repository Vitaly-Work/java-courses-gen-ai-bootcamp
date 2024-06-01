package org.bananalaba.datatraining.aws.testdata.event.time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bananalaba.datatraining.aws.testdata.event.time.RealtimeTimer.SystemTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RealtimeTimerTest {

    @Mock
    private SystemTime systemTime;
    private ExecutorService executor1 = Executors.newFixedThreadPool(1);
    private ExecutorService executor2 = Executors.newFixedThreadPool(1);

    @Test
    public void shouldProvideTime() throws InterruptedException, ExecutionException {
        var timer = new RealtimeTimer(systemTime, Instant.ofEpochMilli(100), 200L);

        var actualFromThread1 = CompletableFuture.supplyAsync(timer::nextTime, executor1).get();
        var actualFromThread2 = CompletableFuture.supplyAsync(timer::nextTime, executor2).get();

        assertThat(actualFromThread1).isEqualTo(Instant.ofEpochMilli(100));
        assertThat(actualFromThread2).isEqualTo(Instant.ofEpochMilli(100));

        verifyNoInteractions(systemTime);
    }

    @Test
    public void shouldProvideTimeAfterSleeping() throws InterruptedException, ExecutionException {
        var timer = new RealtimeTimer(systemTime, Instant.ofEpochMilli(100), 200L);

        var actualFromThread1 = CompletableFuture.supplyAsync(
            () -> {
                timer.nextTime();
                return timer.nextTime();
            },
            executor1).get();
        var actualFromThread2 = CompletableFuture.supplyAsync(
            () -> {
                timer.nextTime();
                return timer.nextTime();
            },
            executor2).get();

        assertThat(actualFromThread1).isEqualTo(Instant.ofEpochMilli(300));
        assertThat(actualFromThread2).isEqualTo(Instant.ofEpochMilli(300));

        verify(systemTime, times(2)).sleep(200L);
    }

    @Test
    public void shouldEstimateDelayAsItsRate() {
        var timer = new RealtimeTimer(systemTime, Instant.ofEpochMilli(100), 200L);

        var actual = timer.estimateRealTimeDelayMillis();

        assertThat(actual).isEqualTo(200L);
    }

}

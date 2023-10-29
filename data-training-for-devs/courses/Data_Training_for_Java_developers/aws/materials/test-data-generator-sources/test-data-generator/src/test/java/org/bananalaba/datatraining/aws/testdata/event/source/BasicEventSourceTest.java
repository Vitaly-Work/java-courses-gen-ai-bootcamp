package org.bananalaba.datatraining.aws.testdata.event.source;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import org.bananalaba.datatraining.aws.testdata.event.Event;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BasicEventSourceTest {

    @Mock
    private EventTimer timer;

    @Mock
    private EventTemplate<TestEvent> template1;
    @Mock
    private EventTemplate<TestEvent> template2;

    @Test
    public void shouldEmitEvents() {
        var source = new BasicEventSource<>(timer, List.of(template1, template2));

        when(timer.nextTime()).thenReturn(Instant.ofEpochMilli(100));

        var event1 = new TestEvent();
        when(template1.create(Instant.ofEpochMilli(100))).thenReturn(event1);

        var event2 = new TestEvent();
        when(template2.create(Instant.ofEpochMilli(100))).thenReturn(event2);

        var actual = source.emit();

        var expected = List.of(event1, event2);
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void shouldEstimateNumberOfEvents() {
        var source = new BasicEventSource<>(timer, List.of(template1, template2));

        var actual = source.estimateNumberOfEvents(2);
        assertThat(actual).isEqualTo(4);
    }

    static class TestEvent implements Event {
    }

}

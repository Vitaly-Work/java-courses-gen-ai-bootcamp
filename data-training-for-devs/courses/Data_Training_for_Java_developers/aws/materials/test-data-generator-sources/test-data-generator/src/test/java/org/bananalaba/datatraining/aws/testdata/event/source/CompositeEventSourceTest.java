package org.bananalaba.datatraining.aws.testdata.event.source;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.bananalaba.datatraining.aws.testdata.event.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CompositeEventSourceTest {

    @Mock
    private EventSource<TestEvent> delegate1;
    @Mock
    private EventSource<TestEvent> delegate2;

    @Test
    public void shouldEmitEvents() {
        var event1 = new TestEvent();
        var event2 = new TestEvent();
        when(delegate1.emit()).thenReturn(List.of(event1, event2));

        var event3 = new TestEvent();
        when(delegate2.emit()).thenReturn(List.of(event3));

        var source = new CompositeEventSource<>(List.of(delegate1, delegate2));

        var actual = source.emit();
        var expected = List.of(event1, event2, event3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldEstimateNumberOfEvents() {
        when(delegate1.estimateNumberOfEvents(3)).thenReturn(6);
        when(delegate2.estimateNumberOfEvents(3)).thenReturn(3);

        var source = new CompositeEventSource<>(List.of(delegate1, delegate2));

        var actual = source.estimateNumberOfEvents(3);
        assertThat(actual).isEqualTo(9);
    }

    static class TestEvent implements Event {
    }

}

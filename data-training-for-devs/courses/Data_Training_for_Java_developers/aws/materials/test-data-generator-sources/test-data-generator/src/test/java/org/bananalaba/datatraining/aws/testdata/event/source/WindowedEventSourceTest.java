package org.bananalaba.datatraining.aws.testdata.event.source;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
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
public class WindowedEventSourceTest {

    @Mock
    private EventTimer timer;
    @Mock
    private WindowedEventTemplate<TestEvent> template1;
    @Mock
    private WindowedEventTemplate<TestEvent> template2;

    @Test
    public void shouldNotAllowNullTimer() {
        var templates = List.of(template1, template2);
        assertThatThrownBy(() -> new WindowedEventSource<>(null, templates))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("timer");
    }

    @Test
    public void shouldNotAllowNullTemplateList() {
        assertThatThrownBy(() -> new WindowedEventSource<>(timer, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("template list");
    }

    @Test
    public void shouldNotAllowNullTemplate() {
        var templates = new ArrayList<WindowedEventTemplate<TestEvent>>();
        templates.add(template1);
        templates.add(null);

        assertThatThrownBy(() -> new WindowedEventSource<>(timer, templates))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("template");
    }

    @Test
    public void shouldEmitEvents() {
        when(timer.nextTime()).thenReturn(
            Instant.ofEpochMilli(100),
            Instant.ofEpochMilli(200),
            Instant.ofEpochMilli(300)
        );

        var event11 = new TestEvent();
        var event12 = new TestEvent();
        when(template1.create(Instant.ofEpochMilli(100), Instant.ofEpochMilli(200))).thenReturn(event11);
        when(template1.create(Instant.ofEpochMilli(200), Instant.ofEpochMilli(300))).thenReturn(event12);

        var event21 = new TestEvent();
        var event22 = new TestEvent();
        when(template2.create(Instant.ofEpochMilli(100), Instant.ofEpochMilli(200))).thenReturn(event21);
        when(template2.create(Instant.ofEpochMilli(200), Instant.ofEpochMilli(300))).thenReturn(event22);

        var source = new WindowedEventSource<>(timer, List.of(template1, template2));

        var actual = List.of(
            source.emit(),
            source.emit()
        );
        var expected = List.of(
            List.of(event11, event21),
            List.of(event12, event22)
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldEstimateNumberOfEvents() {
        var source = new WindowedEventSource<>(timer, List.of(template1, template2));

        var actual = source.estimateNumberOfEvents(2);
        assertThat(actual).isEqualTo(4);
    }

    static class TestEvent implements Event {
    }

}

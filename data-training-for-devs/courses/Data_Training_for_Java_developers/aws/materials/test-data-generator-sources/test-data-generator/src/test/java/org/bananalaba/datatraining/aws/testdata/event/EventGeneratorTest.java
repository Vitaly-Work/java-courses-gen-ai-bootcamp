package org.bananalaba.datatraining.aws.testdata.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import org.bananalaba.datatraining.aws.testdata.event.sink.EventSink;
import org.bananalaba.datatraining.aws.testdata.event.source.EventSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EventGeneratorTest {

    @Mock
    private EventSource<TestEvent> source;
    @Mock
    private EventSink<TestEvent> sink;

    @InjectMocks
    private EventGenerator generator;

    @Test
    public void shouldNotAllowNullSource() {
        assertThatThrownBy(() -> new EventGenerator(null, sink))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("source");
    }

    @Test
    public void shouldNotAllowNullSink() {
        assertThatThrownBy(() -> new EventGenerator(source, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("sink");
    }

    @Test
    public void shouldNotAllowNegativeNumberOfIterations() {
        assertThatThrownBy(() -> generator.generate(-10)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldGenerateZeroEventsForZeroIterations() {
        generator.generate(0);

        verifyNoInteractions(source, sink);
    }

    @Test
    public void shouldGenerateEvents() {
        var event1 = new TestEvent();
        var event2 = new TestEvent();
        var event3 = new TestEvent();
        when(source.emit()).thenReturn(
            List.of(event1),
            List.of(event2, event3)
        );

        generator.generate(2);

        var flow = inOrder(sink);
        flow.verify(sink).submit(event1);
        flow.verify(sink).submit(event2);
        flow.verify(sink).submit(event3);
    }

    @Test
    public void shouldEstimateIterationDelay() {
        when(sink.estimateSubmissionLatencyMillis()).thenReturn(100L);

        var actual = generator.estimateIterationLatencyMillis();
        assertThat(actual).isEqualTo(100L);
    }

    static class TestEvent implements Event {
    }

}

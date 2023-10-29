package org.bananalaba.datatraining.aws.testdata.event.sink;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bananalaba.datatraining.aws.testdata.event.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PartitionedEventSinkTest {

    @Mock
    private EventSink<TestEvent> partitionA;
    @Mock
    private EventSink<TestEvent> partitionB;

    @Test
    public void shouldDelegateToPartitions() {
        var sink = new PartitionedEventSink<>(
            TestEvent::getPartition,
            partition -> switch (partition) {
                case "a" -> partitionA;
                case "b" -> partitionB;
                default -> throw new IllegalArgumentException("unknown partition " + partition);
            }
        );

        var event1 = new TestEvent("a");
        var event2 = new TestEvent("b");
        var event3 = new TestEvent("a");
        sink.submit(event1);
        sink.submit(event2);
        sink.submit(event3);

        verify(partitionA).submit(event1);
        verify(partitionA).submit(event3);
        verify(partitionB).submit(event2);
    }

    @Test
    public void shouldCloseAllPartitions() {
        var sink = new PartitionedEventSink<>(
            TestEvent::getPartition,
            partition -> switch (partition) {
                case "a" -> partitionA;
                case "b" -> partitionB;
                default -> throw new IllegalArgumentException("unknown partition " + partition);
            }
        );

        var event1 = new TestEvent("a");
        var event2 = new TestEvent("b");
        var event3 = new TestEvent("a");
        sink.submit(event1);
        sink.submit(event2);
        sink.submit(event3);

        sink.close();

        verify(partitionA).close();
        verify(partitionB).close();
    }

    @Test
    public void shouldCloseNoPartitions() {
        var sink = new PartitionedEventSink<>(
            TestEvent::getPartition,
            partition -> switch (partition) {
                case "a" -> partitionA;
                case "b" -> partitionB;
                default -> throw new IllegalArgumentException("unknown partition " + partition);
            }
        );

        sink.close();

        verifyNoInteractions(partitionA, partitionB);
    }

    @RequiredArgsConstructor
    @Getter
    static class TestEvent implements Event {

        private final String partition;

    }

}

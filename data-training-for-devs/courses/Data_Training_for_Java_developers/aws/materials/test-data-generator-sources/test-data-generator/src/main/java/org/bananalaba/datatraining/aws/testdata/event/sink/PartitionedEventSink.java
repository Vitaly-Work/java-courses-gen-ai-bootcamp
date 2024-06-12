package org.bananalaba.datatraining.aws.testdata.event.sink;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bananalaba.datatraining.aws.testdata.event.Event;

@RequiredArgsConstructor
public class PartitionedEventSink<T extends Event> implements EventSink<T> {

    @NonNull
    private final Function<T, String> partitionSelector;
    @NonNull
    private final Function<String, EventSink<T>> partitionFactory;

    private final Map<String, EventSink<T>> partitions = new HashMap<>();

    @Override
    public void submit(T event) {
        notNull(event, "event cannot be null");

        var partitionName = partitionSelector.apply(event);
        var partition = partitions.computeIfAbsent(partitionName, partitionFactory);
        partition.submit(event);
    }

    @Override
    public void close() {
        for (var partition : partitions.values()) {
            partition.close();
        }

        partitions.clear();
    }

    @Override
    public long estimateSubmissionLatencyMillis() {
        throw new UnsupportedOperationException("not implemented yet");
    }

}

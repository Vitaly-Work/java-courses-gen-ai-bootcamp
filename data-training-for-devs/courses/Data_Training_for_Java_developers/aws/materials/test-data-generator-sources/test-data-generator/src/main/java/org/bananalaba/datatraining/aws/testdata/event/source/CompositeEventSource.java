package org.bananalaba.datatraining.aws.testdata.event.source;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bananalaba.datatraining.aws.testdata.event.Event;

@RequiredArgsConstructor
public class CompositeEventSource<T extends Event> implements EventSource<T> {

    @NonNull
    private final List<? extends EventSource<T>> delegates;

    @Override
    public List<T> emit() {
        return delegates.stream()
            .flatMap(delegate -> delegate.emit().stream())
            .toList();
    }

    @Override
    public int estimateNumberOfEvents(int iterationsNumber) {
        checkArgument(iterationsNumber >= 0, "iterationsNumber must be >= 0");
        return delegates.stream()
            .map(delegate -> delegate.estimateNumberOfEvents(iterationsNumber))
            .reduce(0, Integer::sum);
    }

}

package org.bananalaba.datatraining.aws.testdata.event.source;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bananalaba.datatraining.aws.testdata.event.Event;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;

@RequiredArgsConstructor
public class BasicEventSource<T extends Event> implements EventSource<T> {

    @NonNull
    private final EventTimer timer;
    @NonNull
    private final List<? extends EventTemplate<T>> templates;

    @Override
    public List<T> emit() {
        var time = timer.nextTime();
        return templates.stream()
            .map(template -> template.create(time))
            .toList();
    }

    @Override
    public int estimateNumberOfEvents(int iterationsNumber) {
        checkArgument(iterationsNumber >= 0, "iterationsNumber must be >= 0");
        return templates.size() * iterationsNumber;
    }

}

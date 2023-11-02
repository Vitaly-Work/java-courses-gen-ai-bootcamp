package org.bananalaba.datatraining.aws.testdata.event.source;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.Validate.notNull;

import java.time.Instant;
import java.util.List;
import javax.annotation.concurrent.NotThreadSafe;
import org.bananalaba.datatraining.aws.testdata.event.Event;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;

@NotThreadSafe
public class WindowedEventSource<T extends Event> implements EventSource<T> {

    private final EventTimer timer;
    private final List<WindowedEventTemplate<T>> templates;

    private Instant fromTime;

    public WindowedEventSource(EventTimer timer, List<WindowedEventTemplate<T>> templates) {
        this.timer = notNull(timer, "timer required");

        this.templates = notNull(templates, "template list required");
        templates.forEach(template -> notNull(template, "template cannot be null"));
    }

    @Override
    public List<T> emit() {
        if (fromTime == null) {
            fromTime = timer.nextTime();
        }

        var toTime = timer.nextTime();
        var events = templates.stream()
            .map(template -> template.create(fromTime, toTime))
            .toList();

        fromTime = toTime;
        return events;
    }

    @Override
    public int estimateNumberOfEvents(int iterationsNumber) {
        checkArgument(iterationsNumber >= 0, "iterationsNumber must be >= 0");
        return iterationsNumber * templates.size();
    }

}

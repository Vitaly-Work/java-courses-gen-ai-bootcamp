package org.bananalaba.datatraining.aws.testdata.event;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.Validate.notNull;

import org.bananalaba.datatraining.aws.testdata.event.sink.EventSink;
import org.bananalaba.datatraining.aws.testdata.event.source.EventSource;

public class EventGenerator {

    private final EventSource<Event> source;
    private final EventSink<Event> sink;

    @SuppressWarnings("unchecked")
    public <T extends Event> EventGenerator(EventSource<T> source, EventSink<T> sink) {
        this.source = (EventSource<Event>) notNull(source, "source required");
        this.sink = (EventSink<Event>) notNull(sink, "sink required");
    }

    public void generate(final int numberOfIterations) {
        checkArgument(numberOfIterations >= 0, "number of iterations must be >= 0");

        for (int i = 0; i < numberOfIterations; i++) {
            source.emit().forEach(sink::submit);
        }
    }

    public long estimateIterationLatencyMillis() {
        return sink.estimateSubmissionLatencyMillis();
    }

}

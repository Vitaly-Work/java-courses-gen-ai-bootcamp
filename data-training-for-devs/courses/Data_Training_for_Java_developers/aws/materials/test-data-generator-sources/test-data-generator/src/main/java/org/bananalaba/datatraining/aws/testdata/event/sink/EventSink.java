package org.bananalaba.datatraining.aws.testdata.event.sink;

import org.bananalaba.datatraining.aws.testdata.event.Event;

public interface EventSink<T extends Event> {

    void submit(T event);

    void close();

    long estimateSubmissionLatencyMillis();

}

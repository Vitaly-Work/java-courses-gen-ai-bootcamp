package org.bananalaba.datatraining.aws.testdata.event.source;

import java.util.List;
import org.bananalaba.datatraining.aws.testdata.event.Event;

public interface EventSource<T extends Event> {

    List<T> emit();

    int estimateNumberOfEvents(int iterationsNumber);

}

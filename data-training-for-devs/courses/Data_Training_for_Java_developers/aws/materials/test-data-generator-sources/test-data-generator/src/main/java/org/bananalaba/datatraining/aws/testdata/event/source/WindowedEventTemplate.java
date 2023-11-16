package org.bananalaba.datatraining.aws.testdata.event.source;

import java.time.Instant;
import org.bananalaba.datatraining.aws.testdata.event.Event;

public interface WindowedEventTemplate<T extends Event> {

    T create(Instant timeFrom, Instant timeTo);

}
package org.bananalaba.datatraining.aws.testdata.event.sink;

import java.util.List;
import org.bananalaba.datatraining.aws.testdata.event.Event;

public interface TextEventSchema<T extends Event> {

    List<String> getFieldNames();

    String getField(T event, String fieldName);

}

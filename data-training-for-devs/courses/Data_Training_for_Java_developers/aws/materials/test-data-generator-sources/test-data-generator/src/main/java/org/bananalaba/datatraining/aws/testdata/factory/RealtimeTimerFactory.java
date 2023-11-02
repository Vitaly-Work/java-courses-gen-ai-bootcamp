package org.bananalaba.datatraining.aws.testdata.factory;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.function.Function;
import org.bananalaba.datatraining.aws.testdata.definition.RealtimeTimerDefinition;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;
import org.bananalaba.datatraining.aws.testdata.event.time.RealtimeTimer;

public class RealtimeTimerFactory implements Function<RealtimeTimerDefinition, EventTimer> {

    @Override
    public EventTimer apply(RealtimeTimerDefinition definition) {
        notNull(definition, "definition required");
        return new RealtimeTimer(definition.getStartTime(), definition.getRateMillis());
    }

}

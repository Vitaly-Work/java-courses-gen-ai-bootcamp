package org.bananalaba.datatraining.aws.testdata.factory;

import java.util.function.Function;
import org.bananalaba.datatraining.aws.testdata.definition.SyntheticTimerDefinition;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;
import org.bananalaba.datatraining.aws.testdata.event.time.SyntheticEventTimer;

public class SyntheticTimerFactory implements Function<SyntheticTimerDefinition, EventTimer> {

    @Override
    public EventTimer apply(SyntheticTimerDefinition definition) {
        return new SyntheticEventTimer(
            definition.getStartTime(),
            definition.getStepMillis()
        );
    }

}

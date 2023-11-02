package org.bananalaba.datatraining.aws.testdata.definition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(name = "synthetic", value = SyntheticTimerDefinition.class),
    @JsonSubTypes.Type(name = "realtime", value = RealtimeTimerDefinition.class)
})
public interface TimerDefinition {
}

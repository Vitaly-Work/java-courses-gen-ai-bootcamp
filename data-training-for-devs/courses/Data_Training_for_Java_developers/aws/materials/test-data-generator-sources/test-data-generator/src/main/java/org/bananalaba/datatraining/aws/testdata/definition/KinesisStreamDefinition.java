package org.bananalaba.datatraining.aws.testdata.definition;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class KinesisStreamDefinition {

    private final String endpoint;
    @NonNull
    private final String region;
    @NonNull
    private final String streamName;

}

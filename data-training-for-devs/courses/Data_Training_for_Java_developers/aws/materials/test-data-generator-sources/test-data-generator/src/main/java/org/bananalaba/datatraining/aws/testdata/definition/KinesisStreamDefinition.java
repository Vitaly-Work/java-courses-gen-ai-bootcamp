package org.bananalaba.datatraining.aws.testdata.definition;

import static com.google.common.base.Preconditions.checkArgument;

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

    private final int timeoutMillis;

    @Builder
    private KinesisStreamDefinition(String endpoint, @NonNull String region, @NonNull String streamName,
            int timeoutMillis) {
        this.endpoint = endpoint;
        this.region = region;
        this.streamName = streamName;

        checkArgument(timeoutMillis > 0, "timeoutMillis must be > 0");
        this.timeoutMillis = timeoutMillis;
    }

}

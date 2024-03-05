package org.bananalaba.datatraining.aws.testdata.definition;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class AwsApiAuthDefinition {

    @NonNull
    private final String accessKeyId;
    @NonNull
    private final String secretKey;
    private final String sessionToken;

}

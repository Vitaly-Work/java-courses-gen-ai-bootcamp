package org.bananalaba.datatraining.aws.testdata.definition;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
@ToString
@EqualsAndHashCode
public class ServiceCallDefinition {

    @NonNull
    private final String serviceName;
    @NonNull
    private final String relativeUrl;
    @NonNull
    private final String httpMethod;

}

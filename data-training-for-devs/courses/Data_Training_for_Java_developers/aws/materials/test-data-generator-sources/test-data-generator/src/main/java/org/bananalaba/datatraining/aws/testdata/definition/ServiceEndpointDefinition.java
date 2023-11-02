package org.bananalaba.datatraining.aws.testdata.definition;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
public class ServiceEndpointDefinition {

    @NonNull
    private final String relativeUrl;
    @NonNull
    private final String httpMethod;
    @NonNull
    private final List<ServiceCallDefinition> dependencies;

}

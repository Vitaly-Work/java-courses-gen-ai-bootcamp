package org.bananalaba.datatraining.aws.testdata.definition;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
public class ServiceDefinition {

    @NonNull
    private final String name;
    @NonNull
    private final List<ServiceEndpointDefinition> endpoints;

}

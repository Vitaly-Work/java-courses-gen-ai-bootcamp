package org.bananalaba.datatraining.aws.testdata.factory;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ServiceCallGraph {

    @NonNull
    private final String serviceName;
    @NonNull
    private final String serviceAgent;
    @NonNull
    private final String serviceAddress;

    @NonNull
    private final String request;
    @NonNull
    private final String status;

    @NonNull
    private final List<ServiceCallGraph> dependencies;

}

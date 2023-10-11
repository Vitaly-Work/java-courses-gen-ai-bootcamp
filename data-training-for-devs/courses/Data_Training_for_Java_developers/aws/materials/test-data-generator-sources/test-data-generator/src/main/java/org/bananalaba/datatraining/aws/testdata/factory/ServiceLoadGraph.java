package org.bananalaba.datatraining.aws.testdata.factory;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ServiceLoadGraph {

    @NonNull
    private final String remoteUser;
    @NonNull
    private final String userAgent;
    @NonNull
    private final String userAddress;

    @NonNull
    private final ServiceCallGraph target;

}

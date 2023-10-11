package org.bananalaba.datatraining.aws.testdata.definition;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Builder
public class ServiceLoadDefinition {

    private final ServiceCallDefinition call;
    private final String userAgent;
    private final String userAddress;
    private final String remoteUser;

    @Builder
    public ServiceLoadDefinition(@NonNull ServiceCallDefinition call,
            @NonNull String userAgent,
            @NonNull String userAddress,
            @NonNull String remoteUser) {
        this.call = call;
        this.userAgent = userAgent;
        this.userAddress = userAddress;
        this.remoteUser = remoteUser;
    }

}

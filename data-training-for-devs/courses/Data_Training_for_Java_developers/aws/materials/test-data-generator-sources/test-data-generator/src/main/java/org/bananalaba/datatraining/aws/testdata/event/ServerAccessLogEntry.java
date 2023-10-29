package org.bananalaba.datatraining.aws.testdata.event;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ServerAccessLogEntry implements Event {

    @NonNull
    private final String serviceName;
    @NonNull
    private final String remoteAddress;
    @NonNull
    private final String remoteUser;
    @NonNull
    private final Instant time;
    @NonNull
    private final String request;
    @NonNull
    private final String status;
    @NonNull
    private final String userAgent;

}

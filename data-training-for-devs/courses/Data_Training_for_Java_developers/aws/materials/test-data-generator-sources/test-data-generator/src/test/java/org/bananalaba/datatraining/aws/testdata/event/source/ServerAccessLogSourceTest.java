package org.bananalaba.datatraining.aws.testdata.event.source;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import org.bananalaba.datatraining.aws.testdata.event.RandomValueGenerator;
import org.bananalaba.datatraining.aws.testdata.event.ServerAccessLogEntry;
import org.bananalaba.datatraining.aws.testdata.event.time.EventTimer;
import org.bananalaba.datatraining.aws.testdata.factory.ServiceCallGraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ServerAccessLogSourceTest {

    @Mock
    private EventTimer timer;
    @Mock
    private RandomValueGenerator networkLag;

    @Test
    public void shouldEmitEvents() {
        var userGetCall = ServiceCallGraph.builder()
            .serviceAddress("<masked>")
            .serviceAgent("Apache HTTP Client")
            .serviceName("user-service")
            .request("GET /api/v1/users/{id}")
            .status("200")
            .dependencies(List.of())
            .build();
        var paymentPostCall = ServiceCallGraph.builder()
            .serviceAddress("<masked>")
            .serviceAgent("Apache HTTP Client")
            .serviceName("payment-service")
            .request("POST /api/v1/payments/3ds")
            .status("200")
            .dependencies(List.of())
            .build();
        var orderPostCall = ServiceCallGraph.builder()
            .serviceAddress("<masked>")
            .serviceAgent("Apache HTTP Client")
            .serviceName("order-service")
            .request("POST /api/v1/orders")
            .status("200")
            .dependencies(List.of(userGetCall, paymentPostCall))
            .build();

        when(timer.nextTime()).thenReturn(
            Instant.ofEpochMilli(100),
            Instant.ofEpochMilli(200),
            Instant.ofEpochMilli(300)
        );

        when(networkLag.generate()).thenReturn(
            10.0
        );

        var source = ServerAccessLogSource.builder()
            .timer(timer)
            .networkLag(networkLag)
            .remoteUser("user")
            .userAgent("Chrome")
            .userAddress("some ip")
            .root(orderPostCall)
            .build();

        var actual = source.emit();
        var expected = List.of(
            ServerAccessLogEntry.builder()
                .serviceName("order-service")
                .remoteAddress("some ip")
                .remoteUser("user")
                .time(Instant.ofEpochMilli(100))
                .request("POST /api/v1/orders")
                .status("200")
                .userAgent("Chrome")
                .build(),
            ServerAccessLogEntry.builder()
                .serviceName("user-service")
                .remoteAddress("<masked>")
                .remoteUser("order-service")
                .time(Instant.ofEpochMilli(110))
                .request("GET /api/v1/users/{id}")
                .status("200")
                .userAgent("Apache HTTP Client")
                .build(),
            ServerAccessLogEntry.builder()
                .serviceName("payment-service")
                .remoteAddress("<masked>")
                .remoteUser("order-service")
                .time(Instant.ofEpochMilli(110))
                .request("POST /api/v1/payments/3ds")
                .status("200")
                .userAgent("Apache HTTP Client")
                .build()
        );
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void shouldEstimateNumberOfEvents() {
        var userGetCall = ServiceCallGraph.builder()
            .serviceAddress("<masked>")
            .serviceAgent("Apache HTTP Client")
            .serviceName("user-service")
            .request("GET /api/v1/users/{id}")
            .status("200")
            .dependencies(List.of())
            .build();
        var paymentPostCall = ServiceCallGraph.builder()
            .serviceAddress("<masked>")
            .serviceAgent("Apache HTTP Client")
            .serviceName("payment-service")
            .request("POST /api/v1/payments/3ds")
            .status("200")
            .dependencies(List.of())
            .build();
        var orderPostCall = ServiceCallGraph.builder()
            .serviceAddress("<masked>")
            .serviceAgent("Apache HTTP Client")
            .serviceName("order-service")
            .request("POST /api/v1/orders")
            .status("200")
            .dependencies(List.of(userGetCall, paymentPostCall))
            .build();

        var source = ServerAccessLogSource.builder()
            .timer(timer)
            .networkLag(networkLag)
            .remoteUser("user")
            .userAgent("Chrome")
            .userAddress("some ip")
            .root(orderPostCall)
            .build();

        var actual = source.estimateNumberOfEvents(3);
        assertThat(actual).isEqualTo(9);
    }

}

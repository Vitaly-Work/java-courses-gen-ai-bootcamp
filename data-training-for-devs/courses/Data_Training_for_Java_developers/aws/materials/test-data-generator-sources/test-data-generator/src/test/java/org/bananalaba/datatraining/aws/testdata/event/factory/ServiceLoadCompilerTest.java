package org.bananalaba.datatraining.aws.testdata.event.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.bananalaba.datatraining.aws.testdata.definition.ServiceCallDefinition;
import org.bananalaba.datatraining.aws.testdata.definition.ServiceDefinition;
import org.bananalaba.datatraining.aws.testdata.definition.ServiceEndpointDefinition;
import org.bananalaba.datatraining.aws.testdata.definition.ServiceLoadDefinition;
import org.bananalaba.datatraining.aws.testdata.factory.ServiceCallGraph;
import org.bananalaba.datatraining.aws.testdata.factory.ServiceLoadCompiler;
import org.bananalaba.datatraining.aws.testdata.factory.ServiceLoadGraph;
import org.junit.jupiter.api.Test;

public class ServiceLoadCompilerTest {

    private final ServiceLoadCompiler compiler = new ServiceLoadCompiler();

    @Test
    public void shouldNotAllowNullServiceDefinitions() {
        var loadDefinitions = List.<ServiceLoadDefinition>of();
        assertThatThrownBy(() -> compiler.compile(null, loadDefinitions)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldNotAllowNullLoadDefinitions() {
        var serviceDefinitions = List.<ServiceDefinition>of();
        assertThatThrownBy(() -> compiler.compile(serviceDefinitions, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldNotAllowCallsToUnknownServices() {
        var userService = ServiceDefinition.builder()
            .name("user-service")
            .endpoints(List.of(
                ServiceEndpointDefinition.builder()
                    .httpMethod("GET")
                    .relativeUrl("/api/v1/users/{id}")
                    .dependencies(List.of())
                    .build()
            ))
            .build();

        var paymentService = ServiceDefinition.builder()
            .name("payment-service")
            .endpoints(List.of(
                ServiceEndpointDefinition.builder()
                    .httpMethod("POST")
                    .relativeUrl("/api/v1/payments/3ds")
                    .dependencies(List.of())
                    .build()
            ))
            .build();

        var load = List.of(
            ServiceLoadDefinition.builder()
                .userAgent("Firefox")
                .userAddress("<some-ip>")
                .remoteUser("-")
                .call(ServiceCallDefinition.builder()
                    .serviceName("order-service")
                    .httpMethod("POST")
                    .relativeUrl("/api/v1/orders")
                    .build()
                )
                .build(),
            ServiceLoadDefinition.builder()
                .userAgent("Chrome")
                .userAddress("<other-ip>")
                .remoteUser("askljkjg1376")
                .call(ServiceCallDefinition.builder()
                    .serviceName("user-service")
                    .httpMethod("GET")
                    .relativeUrl("/api/v1/users/{id}")
                    .build()
                )
                .build()
        );

        assertThatThrownBy(() -> compiler.compile(List.of(userService, paymentService), load))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("order-service");
    }

    @Test
    public void shouldNotAllowCallsToUnknownEndpoints() {
        var userService = ServiceDefinition.builder()
            .name("user-service")
            .endpoints(List.of(
                ServiceEndpointDefinition.builder()
                    .httpMethod("GET")
                    .relativeUrl("/api/v1/users/{id}")
                    .dependencies(List.of())
                    .build()
            ))
            .build();

        var paymentService = ServiceDefinition.builder()
            .name("payment-service")
            .endpoints(List.of(
                ServiceEndpointDefinition.builder()
                    .httpMethod("POST")
                    .relativeUrl("/api/v1/payments/3ds")
                    .dependencies(List.of())
                    .build()
            ))
            .build();

        var load = List.of(
            ServiceLoadDefinition.builder()
                .userAgent("Chrome")
                .userAddress("<other-ip>")
                .remoteUser("askljkjg1376")
                .call(ServiceCallDefinition.builder()
                    .serviceName("user-service")
                    .httpMethod("DELETE")
                    .relativeUrl("/api/v1/users/{id}")
                    .build()
                )
                .build()
        );

        assertThatThrownBy(() -> compiler.compile(List.of(userService, paymentService), load))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("user-service")
            .hasMessageContaining("DELETE /api/v1/users/{id}");
    }

    @Test
    public void shouldNotAllowDuplicateEndpoints() {
        var orderService = ServiceDefinition.builder()
            .name("order-service")
            .endpoints(List.of(
                ServiceEndpointDefinition.builder()
                    .httpMethod("GET")
                    .relativeUrl("/api/v1/orders/{id}")
                    .dependencies(List.of(
                        ServiceCallDefinition.builder()
                            .serviceName("user-service")
                            .httpMethod("GET")
                            .relativeUrl("/api/v1/users/{id}")
                            .build()
                    ))
                    .build(),
                ServiceEndpointDefinition.builder()
                    .httpMethod("POST")
                    .relativeUrl("/api/v1/orders")
                    .dependencies(List.of(
                        ServiceCallDefinition.builder()
                            .serviceName("user-service")
                            .httpMethod("GET")
                            .relativeUrl("/api/v1/users/{id}")
                            .build(),
                        ServiceCallDefinition.builder()
                            .serviceName("payment-service")
                            .httpMethod("POST")
                            .relativeUrl("/api/v1/payments/3ds")
                            .build()
                    ))
                    .build()
            ))
            .build();

        var userService = ServiceDefinition.builder()
            .name("user-service")
            .endpoints(List.of(
                ServiceEndpointDefinition.builder()
                    .httpMethod("GET")
                    .relativeUrl("/api/v1/users/{id}")
                    .dependencies(List.of(
                        ServiceCallDefinition.builder()
                            .serviceName("order-service")
                            .httpMethod("POST")
                            .relativeUrl("/api/v1/orders")
                            .build()
                    ))
                    .build()
            ))
            .build();

        var paymentService = ServiceDefinition.builder()
            .name("payment-service")
            .endpoints(List.of(
                ServiceEndpointDefinition.builder()
                    .httpMethod("POST")
                    .relativeUrl("/api/v1/payments/3ds")
                    .dependencies(List.of())
                    .build()
            ))
            .build();

        var load = List.of(
            ServiceLoadDefinition.builder()
                .userAgent("Firefox")
                .userAddress("<some-ip>")
                .remoteUser("-")
                .call(ServiceCallDefinition.builder()
                    .serviceName("order-service")
                    .httpMethod("POST")
                    .relativeUrl("/api/v1/orders")
                    .build()
                )
                .build(),
            ServiceLoadDefinition.builder()
                .userAgent("Chrome")
                .userAddress("<other-ip>")
                .remoteUser("askljkjg1376")
                .call(ServiceCallDefinition.builder()
                    .serviceName("user-service")
                    .httpMethod("GET")
                    .relativeUrl("/api/v1/users/{id}")
                    .build()
                )
                .build()
        );

        assertThatThrownBy(() -> compiler.compile(List.of(orderService, userService, paymentService), load))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(
                "cyclic dependency: POST order-service /api/v1/orders ->" +
                    " GET user-service /api/v1/users/{id} ->" +
                    " POST order-service /api/v1/orders"
            );
    }

    @Test
    public void shouldCompileTask() {
        var orderService = ServiceDefinition.builder()
            .name("order-service")
            .endpoints(List.of(
                ServiceEndpointDefinition.builder()
                    .httpMethod("GET")
                    .relativeUrl("/api/v1/orders/{id}")
                    .dependencies(List.of(
                        ServiceCallDefinition.builder()
                            .serviceName("user-service")
                            .httpMethod("GET")
                            .relativeUrl("/api/v1/users/{id}")
                            .build()
                    ))
                    .build(),
                ServiceEndpointDefinition.builder()
                    .httpMethod("POST")
                    .relativeUrl("/api/v1/orders")
                    .dependencies(List.of(
                        ServiceCallDefinition.builder()
                            .serviceName("user-service")
                            .httpMethod("GET")
                            .relativeUrl("/api/v1/users/{id}")
                            .build(),
                        ServiceCallDefinition.builder()
                            .serviceName("payment-service")
                            .httpMethod("POST")
                            .relativeUrl("/api/v1/payments/3ds")
                            .build()
                    ))
                    .build()
            ))
            .build();

        var userService = ServiceDefinition.builder()
            .name("user-service")
            .endpoints(List.of(
                ServiceEndpointDefinition.builder()
                    .httpMethod("GET")
                    .relativeUrl("/api/v1/users/{id}")
                    .dependencies(List.of())
                    .build()
            ))
            .build();

        var paymentService = ServiceDefinition.builder()
            .name("payment-service")
            .endpoints(List.of(
                ServiceEndpointDefinition.builder()
                    .httpMethod("POST")
                    .relativeUrl("/api/v1/payments/3ds")
                    .dependencies(List.of())
                    .build()
            ))
            .build();

        var load = List.of(
            ServiceLoadDefinition.builder()
                .userAgent("Firefox")
                .userAddress("<some-ip>")
                .remoteUser("-")
                .call(ServiceCallDefinition.builder()
                    .serviceName("order-service")
                    .httpMethod("POST")
                    .relativeUrl("/api/v1/orders")
                    .build()
                )
                .build(),
            ServiceLoadDefinition.builder()
                .userAgent("Chrome")
                .userAddress("<other-ip>")
                .remoteUser("askljkjg1376")
                .call(ServiceCallDefinition.builder()
                    .serviceName("user-service")
                    .httpMethod("GET")
                    .relativeUrl("/api/v1/users/{id}")
                    .build()
                )
                .build()
        );

        var actual = compiler.compile(List.of(orderService, userService, paymentService), load);

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
        var expected = List.of(
            ServiceLoadGraph.builder()
                .target(orderPostCall)
                .remoteUser("-")
                .userAddress("<some-ip>")
                .userAgent("Firefox")
                .build(),
            ServiceLoadGraph.builder()
                .target(userGetCall)
                .remoteUser("askljkjg1376")
                .userAddress("<other-ip>")
                .userAgent("Chrome")
                .build()
        );
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

}

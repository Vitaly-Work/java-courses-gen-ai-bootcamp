package org.bananalaba.datatraining.aws.testdata.factory;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.bananalaba.datatraining.aws.testdata.definition.ServiceCallDefinition;
import org.bananalaba.datatraining.aws.testdata.definition.ServiceDefinition;
import org.bananalaba.datatraining.aws.testdata.definition.ServiceLoadDefinition;

public class ServiceLoadCompiler {

    public List<ServiceLoadGraph> compile(List<ServiceDefinition> serviceDefinitions,
            List<ServiceLoadDefinition> loadDefinitions) {
        notNull(serviceDefinitions, "service definitions required");
        notNull(loadDefinitions, "load definitions required");

        var context = new Context(serviceDefinitions.stream()
            .collect(toMap(ServiceDefinition::getName, Function.identity())));

        var loadItems = new ArrayList<ServiceLoadGraph>();
        for (var loadDefinition : loadDefinitions) {
            var rootCall = loadDefinition.getCall();
            var callGraph = compile(rootCall, context);

            var loadItem = ServiceLoadGraph.builder()
                .target(callGraph)
                .userAddress(loadDefinition.getUserAddress())
                .userAgent(loadDefinition.getUserAgent())
                .remoteUser(loadDefinition.getRemoteUser())
                .build();
            loadItems.add(loadItem);
        }

        return loadItems;
    }

    private ServiceCallGraph compile(ServiceCallDefinition callDefinition, Context context) {
        var cached = context.getCached(callDefinition);
        if (cached != null) {
            return cached;
        }

        context.putInProgress(callDefinition);

        var serviceDefinition = context.getServiceDefinition(callDefinition.getServiceName());
        checkArgument(
            serviceDefinition != null,
            "service definition for '" + callDefinition.getServiceName() + "' not found"
        );

        var matchingEndpoints = serviceDefinition.getEndpoints()
            .stream()
            .filter(endpoint -> endpoint.getHttpMethod().equals(callDefinition.getHttpMethod())
                && endpoint.getRelativeUrl().equals(callDefinition.getRelativeUrl()))
            .toList();
        checkArgument(
            matchingEndpoints.size() != 0,
            "endpoint " + callDefinition.getHttpMethod() + " " + callDefinition.getRelativeUrl()
                + " in " + callDefinition.getServiceName() + " not found"
        );
        checkArgument(
            matchingEndpoints.size() == 1,
            "endpoint " + callDefinition.getHttpMethod() + " " + callDefinition.getRelativeUrl()
                + " in " + callDefinition.getServiceName() + " is ambiguous"
        );

        var dependencies = matchingEndpoints.get(0)
            .getDependencies()
            .stream()
            .map(dependency -> compile(dependency, context))
            .toList();

        var graph = ServiceCallGraph.builder()
            .serviceName(callDefinition.getServiceName())
            .serviceAddress("<masked>")
            .serviceAgent("Apache HTTP Client")
            .request(callDefinition.getHttpMethod() + " " + callDefinition.getRelativeUrl())
            .status("200")
            .dependencies(dependencies)
            .build();
        context.complete(callDefinition, graph);

        return graph;
    }

    private static class Context {

        private final Map<String, ServiceDefinition> serviceDefinitions;

        private final Map<ServiceCallDefinition, ServiceCallGraph> compilationCache = new HashMap<>();
        private final List<ServiceCallDefinition> compilationQueue = new ArrayList<>();

        public Context(Map<String, ServiceDefinition> serviceDefinitions) {
            this.serviceDefinitions = serviceDefinitions;
        }

        void putInProgress(ServiceCallDefinition call) {
            if (compilationQueue.contains(call)) {
                var cycle = new ArrayList<>(compilationQueue);
                cycle.add(call);

                var formattedCycle = cycle.stream()
                    .map(item -> item.getHttpMethod() + " " + item.getServiceName() + " " + item.getRelativeUrl())
                    .collect(Collectors.joining(" -> "));

                throw new IllegalArgumentException("cyclic dependency: " + formattedCycle);
            }

            compilationQueue.add(call);
        }

        void complete(ServiceCallDefinition call, ServiceCallGraph compiled) {
            compilationCache.put(call, compiled);
            compilationQueue.remove(call);
        }

        ServiceCallGraph getCached(ServiceCallDefinition call) {
            return compilationCache.get(call);
        }

        ServiceDefinition getServiceDefinition(String name) {
            return serviceDefinitions.get(name);
        }

    }

}

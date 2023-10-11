package org.bananalaba.datatraining.aws.testdata.factory;

import static org.apache.commons.lang3.Validate.notNull;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GenericDispatcher<I> {

    private final Class<I> baseDefinitionType;
    private final Map<Class<?>, Consumer<Object>> concreteRunners;

    private GenericDispatcher(Class<I> baseDefinitionType, Map<Class<?>, Consumer<Object>> concreteRunners) {
        this.baseDefinitionType = baseDefinitionType;
        this.concreteRunners = concreteRunners;
    }

    public void run(I definition) {
        notNull(definition, "definition required");

        var runner = concreteRunners.get(definition.getClass());
        if (runner == null) {
            throw new IllegalArgumentException(
                "unknown " + baseDefinitionType.getName() + ": " + definition.getClass());
        }

        runner.accept(definition);
    }

    public static <I> Builder<I> builder(Class<I> baseDefinitionType) {
        return new Builder<>(baseDefinitionType);
    }

    public static class Builder<I> {

        private Class<I> baseDefinitionType;
        private Map<Class<?>, Consumer<Object>> concreteFactories = new HashMap<>();

        private Builder(Class<I> baseDefinitionType) {
            this.baseDefinitionType = notNull(baseDefinitionType, "base definition type required");
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public <IT extends I> Builder<I> register(Class<IT> definitionType, Consumer<IT> runner) {
            notNull(runner, "runner cannot be null");
            concreteFactories.put(definitionType, (Consumer) runner);

            return this;
        }

        public GenericDispatcher<I> build() {
            return new GenericDispatcher<>(baseDefinitionType, ImmutableMap.copyOf(concreteFactories));
        }

    }

}

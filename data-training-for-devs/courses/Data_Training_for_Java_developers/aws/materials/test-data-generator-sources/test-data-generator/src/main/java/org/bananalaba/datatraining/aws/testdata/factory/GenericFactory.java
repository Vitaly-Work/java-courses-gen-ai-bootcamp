package org.bananalaba.datatraining.aws.testdata.factory;

import static org.apache.commons.lang3.Validate.notNull;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GenericFactory<I, O> {

    private final Class<I> baseDefinitionType;
    private final Map<Class<?>, Function<Object, O>> concreteFactories;

    private GenericFactory(Class<I> baseDefinitionType, Map<Class<?>, Function<Object, O>> concreteFactories) {
        this.baseDefinitionType = baseDefinitionType;
        this.concreteFactories = concreteFactories;
    }

    public O create(I definition) {
        notNull(definition, "definition required");

        var factory = concreteFactories.get(definition.getClass());
        if (factory == null) {
            throw new IllegalArgumentException(
                "unknown " + baseDefinitionType.getName() + ": " + definition.getClass());
        }

        return factory.apply(definition);
    }

    public static <I, O> Builder<I, O> builder(Class<I> baseDefinitionType, Class<O> baseProductType) {
        return new Builder<>(baseDefinitionType);
    }

    public static class Builder<I, O> {

        private Class<I> baseDefinitionType;
        private Map<Class<?>, Function<Object, O>> concreteFactories = new HashMap<>();

        private Builder(Class<I> baseDefinitionType) {
            this.baseDefinitionType = notNull(baseDefinitionType, "base definition type required");
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public <IT extends I> Builder<I, O> register(Class<IT> definitionType, Function<IT, ? extends O> factory) {
            notNull(factory, "factory cannot be null");
            concreteFactories.put(definitionType, (Function) factory);

            return this;
        }

        public GenericFactory<I, O> build() {
            return new GenericFactory<>(baseDefinitionType, ImmutableMap.copyOf(concreteFactories));
        }

    }

}

package org.bananalaba.datatraining.aws.testdata.event.sink;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bananalaba.datatraining.aws.testdata.event.Event;

public class ExplicitTextEventSchema<T extends Event> implements TextEventSchema<T> {

    private final NavigableMap<String, Field<T>> fields;

    public ExplicitTextEventSchema(Field<T>... fields) {
        notNull(fields, "fields required");

        this.fields = new TreeMap<>(Stream.of(fields)
            .peek(field -> notNull(field, "field cannot be null"))
            .collect(toMap(Field::getName, Function.identity())));
    }

    @Override
    public List<String> getFieldNames() {
        return new ArrayList<>(fields.keySet());
    }

    @Override
    public String getField(T event, String fieldName) {
        var field = fields.get(fieldName);
        if (field == null) {
            throw new IllegalArgumentException("unknown field: " + fieldName);
        }

        return field.getExtractor().apply(event);
    }

    @RequiredArgsConstructor
    @Getter
    public static class Field<T extends Event> {

        @NonNull
        private final String name;
        @NonNull
        private final Function<T, String> extractor;

    }

}

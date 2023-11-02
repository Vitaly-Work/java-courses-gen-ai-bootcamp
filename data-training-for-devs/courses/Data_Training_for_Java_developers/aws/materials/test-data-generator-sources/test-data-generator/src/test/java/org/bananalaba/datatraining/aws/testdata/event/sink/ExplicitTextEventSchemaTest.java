package org.bananalaba.datatraining.aws.testdata.event.sink;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bananalaba.datatraining.aws.testdata.event.Event;
import org.bananalaba.datatraining.aws.testdata.event.sink.ExplicitTextEventSchema.Field;
import org.junit.jupiter.api.Test;

public class ExplicitTextEventSchemaTest {

    private ExplicitTextEventSchema<TestEvent> schema = new ExplicitTextEventSchema<>(
        new Field<>("name", TestEvent::getName),
        new Field<>("value", TestEvent::getValue)
    );

    @Test
    public void shouldNotAllowNullFieldList() {
        assertThatThrownBy(() -> new ExplicitTextEventSchema<>(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldNotAllowNullFields() {
        assertThatThrownBy(() -> new ExplicitTextEventSchema<>(
            new Field<>("name", TestEvent::getName),
            null,
            new Field<>("value", TestEvent::getValue)
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldGetFieldsList() {
        var actual = schema.getFieldNames();
        var expected = List.of("name", "value");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldGetFieldValue() {
        var event = new TestEvent("cpu", "75.4");

        assertThat(schema.getField(event, "name")).isEqualTo("cpu");
        assertThat(schema.getField(event, "value")).isEqualTo("75.4");
    }

    @Test
    public void shouldNotGetUnknownFieldValue() {
        var event = new TestEvent("cpu", "75.4");
        assertThatThrownBy(() -> schema.getField(event, "bad"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("unknown field: bad");
    }

    @RequiredArgsConstructor
    @Getter
    static class TestEvent implements Event {

        private final String name;
        private final String value;

    }

}

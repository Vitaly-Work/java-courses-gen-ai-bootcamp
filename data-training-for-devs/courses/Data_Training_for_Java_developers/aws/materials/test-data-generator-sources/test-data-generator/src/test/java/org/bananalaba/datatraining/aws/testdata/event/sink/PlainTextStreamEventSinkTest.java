package org.bananalaba.datatraining.aws.testdata.event.sink;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bananalaba.datatraining.aws.testdata.event.Event;
import org.junit.jupiter.api.Test;

public class PlainTextStreamEventSinkTest {

    private TextEventSchema<TestEvent> schema = new TextEventSchema<TestEvent>() {

        @Override
        public List<String> getFieldNames() {
            return List.of("name", "value");
        }

        @Override
        public String getField(TestEvent event, String fieldName) {
            return switch (fieldName) {
                case "name" -> event.getName();
                case "value" -> event.getValue();
                default -> throw new RuntimeException("unknown field: " + fieldName);
            };
        }

    };

    @Test
    public void shouldNotAllowNullSchema() {
        var output = new ByteArrayOutputStream();
        assertThatThrownBy(() -> new PlainTextStreamEventSink<>(null, output, ",", true))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldNotAllowNullOutput() {
        assertThatThrownBy(() -> new PlainTextStreamEventSink<>(schema, null, ",", true))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldWriteWithHeader() {
        var output = new ByteArrayOutputStream();
        var sink = new PlainTextStreamEventSink<>(schema, output, ",", true);

        sink.submit(new TestEvent("cpu", "15.5"));
        sink.submit(new TestEvent("memory", "35"));

        var actual = output.toString(StandardCharsets.UTF_8);
        var expected = "name,value\ncpu,15.5\nmemory,35";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldWriteWithoutHeader() {
        var output = new ByteArrayOutputStream();
        var sink = new PlainTextStreamEventSink<>(schema, output, " ", false);

        sink.submit(new TestEvent("cpu", "15.5"));
        sink.submit(new TestEvent("memory", "35"));

        var actual = output.toString(StandardCharsets.UTF_8);
        var expected = "cpu 15.5\nmemory 35";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPropagateIoException() throws IOException {
        var output = mock(OutputStream.class);

        var ioError = new IOException("file not found");
        doThrow(ioError).when(output).write(any(byte[].class));

        var sink = new PlainTextStreamEventSink<>(schema, output, ",", true);
        assertThatThrownBy(() -> sink.submit(new TestEvent("cpu", "80")))
            .isInstanceOf(TestDataSinkException.class)
            .hasCause(ioError);
    }

    @Test
    public void shouldCloseOutputStream() throws IOException {
        var output = mock(OutputStream.class);
        var sink = new PlainTextStreamEventSink<>(schema, output, ",", true);

        sink.close();

        verify(output).close();
    }

    @Test
    public void shouldEstimateSubmissionLatency() {
        var output = mock(OutputStream.class);
        var sink = new PlainTextStreamEventSink<>(schema, output, ",", true);

        var actual = sink.estimateSubmissionLatencyMillis();
        assertThat(actual).isEqualTo(0);
    }

    @Getter
    @RequiredArgsConstructor
    static class TestEvent implements Event {

        private final String name;
        private final String value;

    }

}

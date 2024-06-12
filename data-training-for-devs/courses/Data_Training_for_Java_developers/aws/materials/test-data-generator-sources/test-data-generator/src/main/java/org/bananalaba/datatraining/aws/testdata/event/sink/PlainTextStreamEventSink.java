package org.bananalaba.datatraining.aws.testdata.event.sink;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bananalaba.datatraining.aws.testdata.event.Event;

@RequiredArgsConstructor
public class PlainTextStreamEventSink<T extends Event> implements EventSink<T> {

    @NonNull
    private final TextEventSchema<T> schema;
    @NonNull
    private final OutputStream output;

    @NonNull
    private final String fieldDelimiter;
    private final boolean includeHeader;

    private boolean isFirst = true;

    @Override
    public void submit(T event) {
        notNull(event, "event cannot be null");

        if (includeHeader && isFirst) {
            writeLine(schema.getFieldNames());
        }

        var values = schema.getFieldNames()
            .stream()
            .map(name -> schema.getField(event, name))
            .toList();
        writeLine(values);
    }

    private void writeLine(List<String> fields) {
        var line = String.join(fieldDelimiter, fields);
        if (!isFirst) {
            line = "\n" + line;
        }

        try {
            output.write(line.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new TestDataSinkException("couldn't write to CSV", e);
        }

        isFirst = false;
    }

    @Override
    public void close() {
        try {
            output.close();
            isFirst = true;
        } catch (IOException e) {
            throw new EventSinkException("couldn't close the output stream", e);
        }
    }

    @Override
    public long estimateSubmissionLatencyMillis() {
        return 0;
    }

}

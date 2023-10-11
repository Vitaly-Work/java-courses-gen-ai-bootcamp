package org.bananalaba.datatraining.aws.testdata.application;

import static org.apache.commons.lang3.Validate.notNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;
import org.bananalaba.datatraining.aws.testdata.definition.DataGenerationTask;

@RequiredArgsConstructor
public class TestDataTaskParser {

    @NonNull
    private final ObjectMapper dtoMapper;
    @NonNull
    private final StringSubstitutor placeholderResolver;

    public TestDataTaskParser(@NonNull ObjectMapper dtoMapper, @NonNull Function<String, String> placeholderResolver) {
        this.dtoMapper = dtoMapper;
        this.placeholderResolver = new StringSubstitutor(placeholderResolver::apply, "${", "}", '\\');
    }

    public DataGenerationTask parse(InputStream input) {
        notNull(input, "input required");

        var inputText = toText(input);
        var resolvedInputText = resolve(inputText);
        return parse(resolvedInputText);
    }

    private String toText(InputStream input) {
        try {
            return IOUtils.toString(input, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new TestDataGeneratorConfigException("failed to read task config", e);
        }
    }

    private String resolve(String text) {
        try {
            return placeholderResolver.replace(text);
        } catch (IllegalArgumentException e) {
            throw new TestDataGeneratorConfigException("failed to pre-process task config", e);
        }
    }

    private DataGenerationTask parse(String text) {
        try {
            return dtoMapper.readValue(text, DataGenerationTask.class);
        } catch (JsonProcessingException e) {
            throw new TestDataGeneratorConfigException("failed to parse task config", e);
        }
    }

}

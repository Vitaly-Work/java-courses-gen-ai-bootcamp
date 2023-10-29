package org.bananalaba.datatraining.aws.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestResourcesManager {

    @NonNull
    private final ObjectMapper payloadMapper;

    public <T> List<T> readSampleList(String classPath, Class<T> elementType) {
        var type = payloadMapper.getTypeFactory().constructCollectionLikeType(List.class, elementType);
        try (var input = TestResourcesManager.class.getClassLoader().getResourceAsStream(classPath)) {
            return payloadMapper.readValue(input, type);
        } catch (IOException e) {
            throw new TestExecutionException("failed to read sample", e);
        }
    }

}

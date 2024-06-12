package org.datatraining4j.aws.stream;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.JobID;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.datatraining4j.aws.stream.JsonEncoder.JsonMapperFactory;
import org.datatraining4j.aws.stream.config.KinesisSourceConfig;
import org.datatraining4j.aws.stream.model.TestObject;

@RequiredArgsConstructor
public class StreamApplication {

    @NonNull
    private final KinesisSourceConfig sourceConfig;

    public JobID run() throws Exception {
        var env = StreamExecutionEnvironment.getExecutionEnvironment();

        // realize your logic here
        JsonMapperFactory jsonMapperFactory = () -> new ObjectMapper().registerModule(new JavaTimeModule());
        var jsonEncoder = new JsonEncoder(jsonMapperFactory);
        env.fromCollection(List.of(1, 2, 3))
            .map(value -> new TestObject(String.valueOf(value)))
            .map(jsonEncoder::encode)
            .print();

        return env.executeAsync().getJobID();
    }

}

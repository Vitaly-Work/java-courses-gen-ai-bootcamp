package org.datatraining4j.aws.stream;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.JobID;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.datatraining4j.aws.stream.config.KinesisSourceConfig;

import java.util.List;

@RequiredArgsConstructor
public class StreamApplication {

    @NonNull
    private final KinesisSourceConfig sourceConfig;

    public JobID run() throws Exception {
        var env = StreamExecutionEnvironment.getExecutionEnvironment();

        // realize your logic here
        env.fromCollection(List.of(1, 2, 3))
                .print();

        return env.executeAsync().getJobID();
    }

}

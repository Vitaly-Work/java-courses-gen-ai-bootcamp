package org.bananalaba.datatraining.aws.testdata.definition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(name = "windowed-metrics", value = WindowedMetricsGenerationTask.class),
    @JsonSubTypes.Type(name = "server-access-log", value = ServerAccessLogGenerationTask.class),
    @JsonSubTypes.Type(name = "kinesis-metrics-stream", value = KinesisMetricsGeneratorTask.class)
})
public interface DataGenerationTask {
}

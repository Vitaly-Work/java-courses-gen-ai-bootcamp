package org.bananalaba.datatraining.aws.testdata.event.sink;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.Validate.notNull;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.AmazonKinesisException;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.ByteBuffer;
import java.util.function.Function;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.bananalaba.datatraining.aws.testdata.event.Event;

@Builder
@Slf4j
public class AwsKinesisStreamsSink<T extends Event> implements EventSink<T> {

    private final AmazonKinesis kinesis;
    private final ObjectMapper payloadMapper;

    private final String streamName;
    private final Function<T, String> streamPartitioner;

    private final int timeoutMillis;

    @Override
    public void submit(T event) {
        notNull(event, "event required");

        var request = new PutRecordRequest();
        request.setStreamName(streamName);
        request.setPartitionKey(streamPartitioner.apply(event));
        request.setData(serialise(event));
        request.setSdkClientExecutionTimeout(timeoutMillis);

        try {
            log.info("sending {} to Kinesis Streams: streamName = {}, partitionKey = {}",
                event, streamName, request.getPartitionKey());
            kinesis.putRecord(request);
        } catch (AmazonKinesisException e) {
            throw new EventSinkException("failed to stream event", e);
        }
    }

    private ByteBuffer serialise(T event) {
        byte[] bytes;
        try {
            bytes = payloadMapper.writeValueAsBytes(event);
        } catch (JsonProcessingException e) {
            throw new EventSinkException("failed to serialize event", e);
        }
        return ByteBuffer.wrap(bytes);
    }

    @Override
    public void close() {
    }

    @Override
    public long estimateSubmissionLatencyMillis() {
        return timeoutMillis;
    }

    public static class AwsKinesisStreamsSinkBuilder<T extends Event> {

        private Function<T, String> streamPartitioner;
        private int timeoutMillis = 500;

        public AwsKinesisStreamsSinkBuilder<T> timeoutMillis(final int timeoutMillis) {
            checkArgument(timeoutMillis > 0, "timeout must be > 0");
            this.timeoutMillis = timeoutMillis;

            return this;
        }

    }

}

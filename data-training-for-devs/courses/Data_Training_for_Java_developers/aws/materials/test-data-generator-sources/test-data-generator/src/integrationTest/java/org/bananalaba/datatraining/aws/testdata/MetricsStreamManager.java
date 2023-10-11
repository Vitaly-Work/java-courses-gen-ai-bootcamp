package org.bananalaba.datatraining.aws.testdata;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.Validate.notBlank;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.AmazonKinesisException;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.ListShardsRequest;
import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bananalaba.datatraining.aws.testdata.application.TestDataGeneratorExecutionException;
import org.bananalaba.datatraining.aws.testdata.event.Metric;

@RequiredArgsConstructor
@Slf4j
public class MetricsStreamManager {

    @NonNull
    private final AmazonKinesis kinesis;
    @NonNull
    private final ObjectMapper payloadMapper;

    public List<Metric> pollMetrics(String streamName, long timeoutMillis) {
        notBlank(streamName, "stream name required");
        checkArgument(timeoutMillis > 0, "timeout must be > 0");

        var shardRequest = new ListShardsRequest();
        shardRequest.setStreamName(streamName);

        var shardResponse = kinesis.listShards(shardRequest);
        if (shardResponse.getShards().size() != 1) {
            throw new IllegalStateException("stream " + streamName + " is supposed to have 1 shard, but has "
                + shardResponse.getShards().size());
        }
        var shard = shardResponse.getShards().get(0);

        var shardIteratorRequest = new GetShardIteratorRequest();
        shardIteratorRequest.setStreamName(streamName);
        shardIteratorRequest.setShardId(shard.getShardId());
        shardIteratorRequest.setShardIteratorType("TRIM_HORIZON");

        var shardIteratorResponse = kinesis.getShardIterator(shardIteratorRequest);
        var shardIterator = shardIteratorResponse.getShardIterator();

        var request = new GetRecordsRequest();
        request.setLimit(100);
        var deadline = System.currentTimeMillis() + timeoutMillis;
        var result = new ArrayList<Metric>();
        while (deadline > System.currentTimeMillis() && (shardIterator != null)) {
            request.setShardIterator(shardIterator);

            var batch = kinesis.getRecords(request);
            result.addAll(map(batch.getRecords()));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new TestExecutionException("failed to read from stream " + streamName, e);
            }

            shardIterator = batch.getNextShardIterator();
        }

        return result;
    }

    private List<Metric> map(List<Record> records) {
        return records.stream()
            .map(this::map)
            .toList();
    }

    private Metric map(Record record) {
        try {
            return payloadMapper.readValue(record.getData().array(), Metric.class);
        } catch (IOException e) {
            throw new TestExecutionException("failed to map Kinesis record", e);
        }
    }

    public void createStream(String streamName) {
        notBlank(streamName, "stream name required");

        log.info("creating an AWS Kinesis Streams stream: " + streamName);
        try {
            kinesis.createStream(streamName, 1);
        } catch (AmazonKinesisException e) {
            throw new TestDataGeneratorExecutionException("failed to create stream " + streamName, e);
        }
    }

}

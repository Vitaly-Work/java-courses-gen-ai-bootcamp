package org.bananalaba.datatraining.aws.testdata.factory;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bananalaba.datatraining.aws.testdata.definition.AwsApiAuthDefinition;
import org.bananalaba.datatraining.aws.testdata.definition.KinesisStreamDefinition;
import org.bananalaba.datatraining.aws.testdata.event.Metric;
import org.bananalaba.datatraining.aws.testdata.event.MetricsWindow;
import org.bananalaba.datatraining.aws.testdata.event.ServerAccessLogEntry;
import org.bananalaba.datatraining.aws.testdata.event.sink.AwsKinesisStreamsSink;
import org.bananalaba.datatraining.aws.testdata.event.sink.EventSink;
import org.bananalaba.datatraining.aws.testdata.event.sink.EventSinkException;
import org.bananalaba.datatraining.aws.testdata.event.sink.ExplicitTextEventSchema;
import org.bananalaba.datatraining.aws.testdata.event.sink.ExplicitTextEventSchema.Field;
import org.bananalaba.datatraining.aws.testdata.event.sink.PartitionedEventSink;
import org.bananalaba.datatraining.aws.testdata.event.sink.PlainTextStreamEventSink;
import org.bananalaba.datatraining.aws.testdata.event.sink.TestDataSinkException;
import org.bananalaba.datatraining.aws.testdata.event.sink.TextEventSchema;

@Slf4j
@RequiredArgsConstructor
public class EventSinkFactory {

    private static final TextEventSchema<MetricsWindow> WINDOWED_METRICS_SCHEMA = new ExplicitTextEventSchema<>(
        new Field<>("metricName", MetricsWindow::getMetricName),
        new Field<>("componentName", MetricsWindow::getComponentName),
        new Field<>("fromTimestamp", event -> event.getFromTimestamp().toString()),
        new Field<>("toTimestamp", event -> event.getToTimestamp().toString()),
        new Field<>("minValue", event -> event.getMinValue().toString()),
        new Field<>("maxValue", event -> event.getMaxValue().toString()),
        new Field<>("unit", MetricsWindow::getUnit)
    );

    private static final TextEventSchema<ServerAccessLogEntry> SERVER_ACCESS_LOG_SCHEMA = new ExplicitTextEventSchema<>(
        new Field<>("remoteAddress", ServerAccessLogEntry::getRemoteAddress),
        new Field<>("remoteUser", entry -> " - " + entry.getRemoteUser()),
        new Field<>("time", event -> "[" + event.getTime() + "]"),
        new Field<>("request", event -> "\"" + event.getRequest() + "\""),
        new Field<>("status", ServerAccessLogEntry::getStatus),
        new Field<>("userAgent", event -> "\"" + event.getUserAgent() + "\"")
    );

    private final ObjectMapper metricPayloadMapper;

    public EventSink<MetricsWindow> createCsvMetricsWindowSink(final String outputDirectoryPath) {
        return new PartitionedEventSink<>(
            MetricsWindow::getComponentName,
            partition -> new PlainTextStreamEventSink<>(
                WINDOWED_METRICS_SCHEMA,
                createOutput(outputDirectoryPath, partition, "csv"),
                ",",
                true
            )
        );
    }

    public EventSink<ServerAccessLogEntry> createTxtServerLogSink(final String outputDirectoryPath) {
        return new PartitionedEventSink<>(
            entry -> entry.getServiceName() + "-"
                + DateTimeFormatter.ISO_LOCAL_DATE.format(entry.getTime().atOffset(ZoneOffset.UTC)),
            partition -> new PlainTextStreamEventSink<>(
                SERVER_ACCESS_LOG_SCHEMA,
                createOutput(outputDirectoryPath, partition, "txt"),
                " ",
                false
            )
        );
    }

    private OutputStream createOutput(final String outputFilePath, final String partitionName, final String extension) {
        try {
            Files.createDirectories(Path.of(outputFilePath));
        } catch (IOException e) {
            throw new TestDataSinkException("failed to create the output directory", e);
        }

        var fullPath = Path.of(outputFilePath, partitionName + "." + extension);
        log.info("opened metrics file: " + fullPath);

        try {
            return new FileOutputStream(fullPath.toFile());
        } catch (IOException e) {
            throw new EventSinkException("failed to open file for write", e);
        }
    }

    public EventSink<Metric> kinesisSink(AwsApiAuthDefinition auth, KinesisStreamDefinition stream) {
        notNull(stream, "stream definition required");

        var kinesisBuilder = AmazonKinesisClientBuilder.standard();
        if (auth != null) {
            var credentials = (auth.getSessionToken() == null)
                              ? new BasicAWSCredentials(auth.getAccessKeyId(), auth.getSecretKey())
                              : new BasicSessionCredentials(auth.getAccessKeyId(), auth.getSecretKey(), auth.getSessionToken());
            kinesisBuilder.setCredentials(new AWSStaticCredentialsProvider(credentials));
        }

        if (isNotBlank(stream.getEndpoint())) {
            kinesisBuilder.setEndpointConfiguration(new EndpointConfiguration(
                stream.getEndpoint(), stream.getRegion()
            ));
        } else {
            kinesisBuilder.setRegion(stream.getRegion());
        }

        return AwsKinesisStreamsSink.<Metric>builder()
            .kinesis(kinesisBuilder.build())
            .payloadMapper(metricPayloadMapper)
            .streamName(stream.getStreamName())
            .streamPartitioner(metric -> metric.getComponentName() + "_" + metric.getMetricName())
            .timeoutMillis(stream.getTimeoutMillis())
            .build();
    }

}

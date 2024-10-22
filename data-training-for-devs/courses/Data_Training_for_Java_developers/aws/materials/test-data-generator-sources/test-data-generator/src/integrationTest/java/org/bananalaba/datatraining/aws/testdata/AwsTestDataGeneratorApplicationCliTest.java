package org.bananalaba.datatraining.aws.testdata;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bananalaba.datatraining.aws.testdata.event.Metric;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class AwsTestDataGeneratorApplicationCliTest extends BaseTestDataGeneratorApplicationCliTest {

    private static final DockerImageName LOCAL_STACK_IMAGE = DockerImageName.parse("localstack/localstack:3.8.1");
    @Container
    private static final LocalStackContainer LOCAL_STACK =
        new LocalStackContainer(LOCAL_STACK_IMAGE).withServices(Service.KINESIS);

    private MetricsStreamManager metricsStreamManager;
    private TestResourcesManager testResourcesManager;

    @BeforeEach
    void setUp() {
        System.setProperty("com.amazonaws.sdk.disableCbor", "true");
        var kinesis = AmazonKinesisClientBuilder.standard()
            .withEndpointConfiguration(new EndpointConfiguration(
                LOCAL_STACK.getEndpoint().toString(),
                LOCAL_STACK.getRegion()
            ))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                LOCAL_STACK.getAccessKey(),
                LOCAL_STACK.getSecretKey()
            )))
            .build();

        var payloadMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        metricsStreamManager = new MetricsStreamManager(kinesis, payloadMapper);
        testResourcesManager = new TestResourcesManager(payloadMapper);
    }

    @Test
    public void shouldRunWindowedMetricsGenerationTask() throws IOException, InterruptedException {
        var streamName = "hardware-metrics";
        metricsStreamManager.createStream(streamName);

        var deployment = deploy();
        deployment.run("test-cases/metrics-stream-kinesis/task.json", Map.of(
            "aws_access_key", LOCAL_STACK.getAccessKey(),
            "aws_secret_key", LOCAL_STACK.getSecretKey(),
            "aws_region", LOCAL_STACK.getRegion(),
            "aws_kinesis_endpoint", LOCAL_STACK.getEndpoint().toString(),
            "aws_kinesis_stream", streamName
        ));

        var actual = metricsStreamManager.pollMetrics(streamName, 2000);
        actual.sort(Comparator.comparing(Metric::getComponentName)
            .thenComparing(Metric::getMetricName)
            .thenComparing(Metric::getPublicationTimestamp)
        );

        var expected = testResourcesManager.readSampleList(
            "test-cases/metrics-stream-kinesis/metrics.json", Metric.class);
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

}

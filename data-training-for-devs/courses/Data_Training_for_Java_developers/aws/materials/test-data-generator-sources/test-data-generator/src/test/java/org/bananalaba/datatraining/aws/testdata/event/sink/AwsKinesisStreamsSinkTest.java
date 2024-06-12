package org.bananalaba.datatraining.aws.testdata.event.sink;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.ByteBuffer;
import java.util.function.Function;
import org.bananalaba.datatraining.aws.testdata.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AwsKinesisStreamsSinkTest {

    @Mock
    private AmazonKinesis kinesis;
    @Mock
    private ObjectMapper payloadMapper;
    @Mock
    private Function<TestEvent, String> streamPartitioner;

    private AwsKinesisStreamsSink<TestEvent> sink;

    @BeforeEach
    public void setUp() {
        sink = AwsKinesisStreamsSink.<TestEvent>builder()
            .kinesis(kinesis)
            .payloadMapper(payloadMapper)
            .streamName("some-stream")
            .streamPartitioner(streamPartitioner)
            .timeoutMillis(100)
            .build();
    }

    @Test
    public void shouldSendEvents() throws JsonProcessingException {
        var event = new TestEvent();
        var bytes = new byte[] {1, 2};
        when(payloadMapper.writeValueAsBytes(event)).thenReturn(bytes);
        when(streamPartitioner.apply(event)).thenReturn("a");

        sink.submit(event);

        var request = ArgumentCaptor.forClass(PutRecordRequest.class);
        verify(kinesis).putRecord(request.capture());

        var expectedRequest = new PutRecordRequest();
        expectedRequest.setData(ByteBuffer.wrap(bytes));
        expectedRequest.setPartitionKey("a");
        expectedRequest.setStreamName("some-stream");
        expectedRequest.setSdkClientExecutionTimeout(100);
        assertThat(request.getValue()).usingRecursiveComparison().isEqualTo(expectedRequest);
    }

    @Test
    public void shouldNotAllowNullEvent() {
        assertThatThrownBy(() -> sink.submit(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldFailWithKinesisError() throws JsonProcessingException {
        var event = new TestEvent();
        var bytes = new byte[] {1, 2};
        when(payloadMapper.writeValueAsBytes(event)).thenReturn(bytes);
        when(streamPartitioner.apply(event)).thenReturn("a");

        var error = new ResourceNotFoundException("stream not found");
        when(kinesis.putRecord(any())).thenThrow(error);

        assertThatThrownBy(() -> sink.submit(event)).isInstanceOf(EventSinkException.class)
            .hasCause(error);
    }

    @Test
    public void shouldFailWithMappingException() throws JsonProcessingException {
        var event = new TestEvent();
        var error = new JsonGenerationException("failed");
        when(payloadMapper.writeValueAsBytes(event)).thenThrow(error);
        when(streamPartitioner.apply(event)).thenReturn("a");

        assertThatThrownBy(() -> sink.submit(event)).isInstanceOf(EventSinkException.class)
            .hasCause(error);
    }

    @Test
    public void shouldClose() {
        sink.close();

        verifyNoInteractions(kinesis);
    }

    @Test
    public void shouldEstimateSubmissionDelay() {
        var actual = sink.estimateSubmissionLatencyMillis();
        assertThat(actual).isEqualTo(100);
    }

    static class TestEvent implements Event {
    }

}

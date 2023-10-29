package org.bananalaba.datatraining.aws.testdata.event.source;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.Instant;
import org.bananalaba.datatraining.aws.testdata.event.RandomValueGenerator;
import org.bananalaba.datatraining.aws.testdata.event.MetricsWindow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MetricsWindowTemplateTest {

    @Mock
    private RandomValueGenerator diffGenerator;
    @Mock
    private RandomValueGenerator averageGenerator;

    @Test
    public void shouldNotAllowBlankComponentName() {
        assertThatThrownBy(() -> newTemplate().componentName(null).build())
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("component name");
    }

    @Test
    public void shouldNotAllowBlankMetricName() {
        assertThatThrownBy(() -> newTemplate().metricName(null).build())
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("metric name");
    }

    @Test
    public void shouldNotAllowBlankUnit() {
        assertThatThrownBy(() -> newTemplate().unit(null).build())
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("unit");
    }

    @Test
    public void shouldNotAllowNullAverageGenerator() {
        assertThatThrownBy(() -> newTemplate().averageGenerator(null).build())
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("average generator");
    }

    @Test
    public void shouldNotAllowNullDiffGenerator() {
        assertThatThrownBy(() -> newTemplate().diffGenerator(null).build())
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("diff generator");
    }

    @Test
    public void shouldGenerateEvents() {
        var template = newTemplate().build();

        when(averageGenerator.generate()).thenReturn(10.0);
        when(diffGenerator.generate()).thenReturn(5.0);

        var actual = template.create(Instant.ofEpochMilli(100), Instant.ofEpochMilli(200));
        var expected = MetricsWindow.builder()
            .componentName("user-service")
            .metricName("cpu-usage")
            .unit("percent")
            .minValue(5.0)
            .maxValue(15.0)
            .fromTimestamp(Instant.ofEpochMilli(100))
            .toTimestamp(Instant.ofEpochMilli(200))
            .build();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private MetricsWindowTemplate.MetricsWindowTemplateBuilder newTemplate() {
        return MetricsWindowTemplate.builder()
            .componentName("user-service")
            .metricName("cpu-usage")
            .unit("percent")
            .diffGenerator(diffGenerator)
            .averageGenerator(averageGenerator);
    }

}

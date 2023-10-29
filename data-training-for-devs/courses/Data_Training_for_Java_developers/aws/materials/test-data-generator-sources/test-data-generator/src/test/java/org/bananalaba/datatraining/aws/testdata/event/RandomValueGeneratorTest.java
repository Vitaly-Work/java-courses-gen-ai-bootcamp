package org.bananalaba.datatraining.aws.testdata.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Random;
import org.bananalaba.datatraining.aws.testdata.event.RandomValueGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RandomValueGeneratorTest {

    @Mock
    private Random randomSource;

    @Test
    public void shouldNotTakeMaxLessThanMin() {
        assertThatThrownBy(() -> new RandomValueGenerator(randomSource, 10, 9))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("max value").hasMessageContaining(" < min value");
    }

    @Test
    public void shouldNotAllowNullRandomSource() {
        assertThatThrownBy(() -> new RandomValueGenerator(null, 9, 10)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldGenerateMaxValue() {
        var generator = new RandomValueGenerator(randomSource, 10, 15);

        when(randomSource.nextDouble()).thenReturn(1.0);

        var actual = generator.generate();
        assertThat(actual).isEqualTo(15);
    }

    @Test
    public void shouldGenerateMinValue() {
        var generator = new RandomValueGenerator(randomSource, 10, 15);

        when(randomSource.nextDouble()).thenReturn(0.0);

        var actual = generator.generate();
        assertThat(actual).isEqualTo(10);
    }

    @Test
    public void shouldGenerateAvgValue() {
        var generator = new RandomValueGenerator(randomSource, 10, 15);

        when(randomSource.nextDouble()).thenReturn(0.5);

        var actual = generator.generate();
        assertThat(actual).isEqualTo(12.5);
    }

}

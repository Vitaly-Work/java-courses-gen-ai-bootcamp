package org.bananalaba.datatraining.aws.testdata.event;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Random;

public class RandomValueGenerator {

    private final Random randomSource;
    private final double minValue;
    private final double maxDelta;

    public RandomValueGenerator(Random randomSource, double minValue, double maxValue) {
        this.randomSource = notNull(randomSource, "random source cannot be null");
        this.minValue = minValue;

        checkArgument(maxValue >= minValue, "max value cannot be < min value");
        this.maxDelta = maxValue - minValue;
    }

    public double generate() {
        var random = randomSource.nextDouble();
        return minValue + (maxDelta * random);
    }

}

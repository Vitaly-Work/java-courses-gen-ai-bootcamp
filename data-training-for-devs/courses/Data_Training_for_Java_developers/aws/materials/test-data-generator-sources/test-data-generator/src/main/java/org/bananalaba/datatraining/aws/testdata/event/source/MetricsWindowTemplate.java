package org.bananalaba.datatraining.aws.testdata.event.source;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.time.Instant;
import lombok.Builder;
import org.bananalaba.datatraining.aws.testdata.event.RandomValueGenerator;
import org.bananalaba.datatraining.aws.testdata.event.MetricsWindow;

public class MetricsWindowTemplate implements WindowedEventTemplate<MetricsWindow> {

    private final String componentName;
    private final String metricName;
    private final String unit;

    private final RandomValueGenerator averageGenerator;
    private final RandomValueGenerator diffGenerator;

    @Builder
    private MetricsWindowTemplate(
            String componentName,
            String metricName,
            String unit,
            RandomValueGenerator averageGenerator,
            RandomValueGenerator diffGenerator) {
        this.componentName = notBlank(componentName, "component name required");
        this.metricName = notBlank(metricName, "metric name required");
        this.unit = notBlank(unit, "unit required");

        this.averageGenerator = notNull(averageGenerator, "average generator required");
        this.diffGenerator = notNull(diffGenerator, "diff generator required");
    }

    @Override
    public MetricsWindow create(Instant timeFrom, Instant timeTo) {
        var average = averageGenerator.generate();
        var diff = Math.abs(diffGenerator.generate());
        var min = average - diff;
        var max = average + diff;

        return MetricsWindow.builder()
            .fromTimestamp(timeFrom)
            .toTimestamp(timeTo)
            .componentName(componentName)
            .metricName(metricName)
            .unit(unit)
            .minValue(min)
            .maxValue(max)
            .build();
    }

}

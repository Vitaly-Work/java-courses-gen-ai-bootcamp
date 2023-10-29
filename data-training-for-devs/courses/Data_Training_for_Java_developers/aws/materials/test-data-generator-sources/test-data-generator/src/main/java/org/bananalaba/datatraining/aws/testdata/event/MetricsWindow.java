package org.bananalaba.datatraining.aws.testdata.event;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MetricsWindow implements Event {

    private final String metricName;
    private final String componentName;
    private final Instant fromTimestamp;
    private final Instant toTimestamp;

    private final Double minValue;
    private final Double maxValue;
    private final String unit;

    @Builder
    private MetricsWindow(String metricName,
            String componentName,
            Instant fromTimestamp,
            Instant toTimestamp,
            Double minValue,
            Double maxValue,
            String unit) {
        this.metricName = notBlank(metricName, "metric name required");
        this.componentName = notBlank(componentName, "component name required");

        this.fromTimestamp = notNull(fromTimestamp, "from timestamp required");
        this.toTimestamp = notNull(toTimestamp, "to timestamp required");
        checkArgument(fromTimestamp.compareTo(toTimestamp) < 0, "from timestamp must be < to timestamp");

        this.minValue = notNull(minValue, "min value required");
        this.maxValue = notNull(maxValue, "max value required");
        checkArgument(minValue <= maxValue, "min value must be <= max value");

        this.unit = notBlank(unit, "unit required");
    }

}

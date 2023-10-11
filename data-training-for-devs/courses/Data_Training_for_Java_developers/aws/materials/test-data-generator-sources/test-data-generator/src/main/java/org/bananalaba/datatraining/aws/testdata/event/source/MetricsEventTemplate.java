package org.bananalaba.datatraining.aws.testdata.event.source;

import java.time.Instant;
import lombok.Builder;
import lombok.NonNull;
import org.bananalaba.datatraining.aws.testdata.event.Metric;
import org.bananalaba.datatraining.aws.testdata.event.RandomValueGenerator;

@Builder
public class MetricsEventTemplate implements EventTemplate<Metric> {

    @NonNull
    private final RandomValueGenerator valueGenerator;

    @NonNull
    private final String componentName;
    @NonNull
    private final String metricName;
    @NonNull
    private final String unit;

    @Override
    public Metric create(Instant time) {
        return Metric.builder()
            .componentName(componentName)
            .metricName(metricName)
            .unit(unit)
            .value(valueGenerator.generate())
            .publicationTimestamp(time)
            .build();
    }

}

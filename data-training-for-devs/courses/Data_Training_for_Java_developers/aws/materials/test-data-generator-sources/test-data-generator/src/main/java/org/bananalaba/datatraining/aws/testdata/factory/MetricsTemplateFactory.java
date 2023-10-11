package org.bananalaba.datatraining.aws.testdata.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.bananalaba.datatraining.aws.testdata.definition.MetricDefinition;
import org.bananalaba.datatraining.aws.testdata.event.Metric;
import org.bananalaba.datatraining.aws.testdata.event.RandomValueGenerator;
import org.bananalaba.datatraining.aws.testdata.event.source.EventTemplate;
import org.bananalaba.datatraining.aws.testdata.event.source.MetricsEventTemplate;

@Slf4j
public class MetricsTemplateFactory {

    public Map<String, List<EventTemplate<Metric>>> createEventTemplates(
            final Map<String, Map<String, MetricDefinition>> componentDefinitions, final Long randomSeed) {
        var templates = new HashMap<String, List<EventTemplate<Metric>>>();
        for (var componentName : componentDefinitions.keySet()) {
            var metrics = componentDefinitions.get(componentName);
            var metricTemplates = new ArrayList<EventTemplate<Metric>>();
            for (var metricName : metrics.keySet()) {
                var metric = metrics.get(metricName);
                metricTemplates.add(createEventTemplate(componentName, metricName, metric, randomSeed));
            }

            templates.put(componentName, metricTemplates);
        }

        return templates;
    }

    private EventTemplate<Metric> createEventTemplate(final String componentName,
            final String metricName, final MetricDefinition definition, final Long randomSeed) {
        log.info("creating a metrics window event template for componentName = {} metricName = {}",
            componentName, metricName);

        var random = new Random();
        if (randomSeed != null) {
            random.setSeed(randomSeed);
        }

        var valueGenerator = new RandomValueGenerator(random, definition.getMinValue(), definition.getMaxValue());

        return MetricsEventTemplate.builder()
            .componentName(componentName)
            .metricName(metricName)
            .unit(definition.getUnit())
            .valueGenerator(valueGenerator)
            .build();
    }

}

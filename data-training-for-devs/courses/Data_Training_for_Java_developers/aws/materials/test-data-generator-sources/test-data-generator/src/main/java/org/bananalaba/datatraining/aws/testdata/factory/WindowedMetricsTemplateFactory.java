package org.bananalaba.datatraining.aws.testdata.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.bananalaba.datatraining.aws.testdata.definition.MetricAggregateDefinition;
import org.bananalaba.datatraining.aws.testdata.event.MetricsWindow;
import org.bananalaba.datatraining.aws.testdata.event.RandomValueGenerator;
import org.bananalaba.datatraining.aws.testdata.event.source.MetricsWindowTemplate;
import org.bananalaba.datatraining.aws.testdata.event.source.WindowedEventTemplate;

@Slf4j
public class WindowedMetricsTemplateFactory {

    public List<WindowedEventTemplate<MetricsWindow>> createEventTemplates(
            final Map<String, Map<String, MetricAggregateDefinition>> componentDefinitions, final Long randomSeed) {
        var templates = new ArrayList<WindowedEventTemplate<MetricsWindow>>();
        for (var componentName : componentDefinitions.keySet()) {
            var metrics = componentDefinitions.get(componentName);
            for (var metricName : metrics.keySet()) {
                var metric = metrics.get(metricName);
                templates.add(createEventTemplate(componentName, metricName, metric, randomSeed));
            }
        }

        return templates;
    }

    private WindowedEventTemplate<MetricsWindow> createEventTemplate(final String componentName,
            final String metricName, final MetricAggregateDefinition definition, final Long randomSeed) {
        log.info("creating a metrics window event template for componentName = {} metricName = {}",
            componentName, metricName);

        var random = new Random();
        if (randomSeed != null) {
            random.setSeed(randomSeed);
        }

        var averageGenerator = new RandomValueGenerator(random, definition.getAverageFrom(), definition.getAverageTo());
        var diffGenerator = new RandomValueGenerator(random, definition.getMinDiff(), definition.getMaxDiff());

        return MetricsWindowTemplate.builder()
            .componentName(componentName)
            .metricName(metricName)
            .unit(definition.getUnit())
            .averageGenerator(averageGenerator)
            .diffGenerator(diffGenerator)
            .build();
    }

}

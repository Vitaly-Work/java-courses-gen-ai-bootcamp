# Sub-task 3 - visualising with Grafana
![](../materials/diagrams/task3-focus.png)

**Goal:**
* familiarise with Grafana
* visualise an Athena datasource

**Instructions:**
* run Grafana instance locally [using Docker](https://grafana.com/docs/grafana/latest/setup-grafana/installation/docker/)
* configure the [Grafana Athena plugin](https://grafana.com/grafana/plugins/grafana-athena-datasource/) with credentials to access the Athena/Glue database created in sub-task 2
* create a Grafana dashboard which allows choosing a specific metric and component and showing a chart with average values for the metric over time

**Cost management recommendations:**
* in case you tested the ETL job in conjunction with Athena/Grafana, make sure the job is shut down
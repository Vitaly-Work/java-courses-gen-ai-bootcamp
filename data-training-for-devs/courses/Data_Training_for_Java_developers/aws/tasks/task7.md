# Sub-task 7 - reporting with EMR

![](../materials/diagrams/task7-focus.png)

**Learning prerequisites:**
* [Data engineering with AWS](https://learn.epam.com/detailsPage?id=b478ad0c-a4cc-4d27-a093-1d4661bfc19e) - **section 3** (EPAM Learn course, it will be assigned to you by the organizers)
* [Getting started with Amazon EMR](https://explore.skillbuilder.aws/learn/course/internal/view/elearning/8827/getting-started-with-amazon-emr) (AWS Skill Builder)

**Goal:**
* write a Spark batch job to parse server access logs and build a call graph based on them
* visualise the reports using Grafana graphs
* optional - enrich the call graph reports with the metrics data derived from the previously ingested stream

**Instructions:**
* create an S3 traffic report bucket
* create a Glue Data Table for the bucket
    * follow the _Server traffic report_ model
* write a Spark batch job that
    * reads a dataset from the S3 server access logs bucket
    * filters data for only the specified time range (latest 24 hours by default)
    * parses them (see the _Server access logs_ model)
    * calculates the number of calls per service pair (see the _Server traffic report_ model)
    * writes the reports in the JSON format into the S3 traffic report bucket
* cover the job with unit tests
* deploy the job using EMR in Serverless mode
* use the server access log task provided by the test data generator and copy the resulting files to the S3 server access logs bucket
* run the job, and make sure correct reports appear in the S3 traffic report bucket
* configure a graph dashboard in Grafana
    * follow the conventions from the [node graph panel documentation](https://grafana.com/docs/grafana/latest/panels-visualizations/visualizations/node-graph/)
    * check how to [use SQL-like data sources for graph visualisations](https://community.grafana.com/t/nodegraph-with-mysql/66338/3)
    * use an Athena datasource pointed at the S3 traffic report bucket as the _edges_ Grafana data frame

**Cost management recommendations:**
* make sure the EMR job is shut down
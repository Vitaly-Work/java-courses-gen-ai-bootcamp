# Sub-task 6 - delivering with Kinesis Data Firehose

![](../materials/diagrams/task6-focus.png)

**Goal:**
* setup a JSON-to-Parquet transformation
* write a Kinesis stream into a Parquet S3 bucket

**Instructions**:
* update the Flink application from sub-task 5
  * extend the windowing step to additionally group aggregate metrics by the `componentName` field
  * direct the output to another Kinesis Stream in the following format (one JSON object per component-metric-timestampFrom/To per line without an enclosing array object):
```json
{"componentName": "order-service","fromTimestamp": "2020-09-01T12:35:05.001Z","maxValue": 38.61436853525056,"metricName": "cpu","minValue": 18.542453763847536,"toTimestamp": "2020-09-01T12:35:15.001Z","unit": "percent"}
{"componentName": "order-service","fromTimestamp": "2020-09-01T12:35:05.001Z","maxValue": 71.90267207311402,"metricName": "ram","minValue": 60.24624059569559,"toTimestamp": "2020-09-01T12:35:15.001Z","unit": "percent"}
```
* update the CloudFormation template created in sub-tasks 4-5
    * create another Kinesis Stream (I) that will serve as the output for the Flink application - use 1 provisioned shard
    * create another S3 bucket (O)
    * create a Kinesis Data Firehose delivery stream
    * configure the delivery stream to
      * read from stream I
      * write the metric JSON groups to bucket O
    * point the Flink application created in sub-task 5 to write to stream O
* ingest some events to the Kinesis Data Stream and use Athena to make sure the data appears in the destination bucket
* create a table in AWS Glue Catalog
  * use the same database as in task 1
  * use the same schema definition as in task 1
  * use bucket O as the location
  * specify JSON as the format
* in Athena
  * partition the new table just like in task 1
  * try querying the new table

**Cost management recommendations:**
* make sure to dispose the resources create using CloudFormation - **Kinesis Streams are the most expensive component in the overall solution**

**Advanced sub-task**
* normally in production, you would write such data as aggregate metrics not as JSON files, but either to some database or at least using the Parquet format
* one option would be to use a [JSON-to-Parquet conversion](https://docs.aws.amazon.com/firehose/latest/dev/record-format-conversion.html), but it requires [at least 64 Mb of data](https://docs.aws.amazon.com/firehose/latest/APIReference/API_ParquetSerDe.html)
to form a Parquet block - putting enough data quickly in Kinesis Streams might be thus too expensive for education purposes
* a more appropriate option would be to modify the Flink application to directly write to S3 in the [Parquet bulk format using a rolling strategy](https://nightlies.apache.org/flink/flink-docs-release-1.18/docs/connectors/datastream/filesystem/#bulk-encoded-formats)
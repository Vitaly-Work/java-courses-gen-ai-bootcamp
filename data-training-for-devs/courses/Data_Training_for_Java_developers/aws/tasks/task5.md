# Sub-task 5 - analysing with Kinesis Data Analytics

![](../materials/diagrams/task5-focus.png)

**Goal:**
* write an Apache Flink application for computing windows on metrics events
* deploy the Apache Flink application to Kinesis Data Analytics
* connect the Apache Flink application to a Kinesis stream

**Instructions:**
* get to know or recap Apache Flink basics
    * [overview](https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/dev/datastream/overview/)
    * [execution mode](https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/dev/datastream/execution_mode/) - focus on streaming
    * [event time](https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/concepts/time/)
    * [event time - custom watermarks](https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/dev/datastream/event-time/generating_watermarks/)
* create an Apache Flink application that
    * reads events from a stream of events that correspond to the _Metrics stream_ model
    * applies [tumbling or sliding windows](https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/dev/datastream/operators/windows/#tumbling-windows) to the stream - 5 minutes per window based on the event publication timestamp
    * reduces each window into an aggregate event that corresponds to the _Windowed metrics_ model
* write some [unit tests](https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/dev/datastream/testing/) for the application
* create a temporary S3 bucket for storing JSON files
* deploy the application to Kinesis Data Analytics
    * extend the CloudFormation template created in sub-task 4
    * update your code to work with
        * [Kinesis Data Streams as a source](https://docs.aws.amazon.com/kinesisanalytics/latest/java/how-sources.html#input-streams)
        * [S3 bucket as a sink](https://docs.aws.amazon.com/kinesisanalytics/latest/java/how-sinks.html#sinks-s3)
    * [create an Data Analytics application](https://docs.aws.amazon.com/kinesisanalytics/latest/java/how-creating-apps.html) based on your code
    * connect the application to the stream created in sub-task 4 and to the temporary JSON bucket
* run your application and make sure correct JSON files appear in the temporary bucket
* remove the temporary JSON bucket

**Cost management recommendations:**
* make sure to dispose the resources create using CloudFormation - **Kinesis Stream is the most expensive component in the overall solution**
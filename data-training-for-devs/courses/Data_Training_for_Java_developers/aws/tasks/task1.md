# Sub-task 1 - ingesting with Glue ETL

![task1](../materials/diagrams/task1-focus.png)

**Learning prerequisites:**
* [Data engineering with AWS](https://learn.epam.com/detailsPage?id=b478ad0c-a4cc-4d27-a093-1d4661bfc19e) (EPAM Learn course, it will be assigned to you by the organizers)
  * **NOTE**: Although only sections 1-2 are required for this sub-task, this course gives a high-level overview of most services you will use
    in subsequent sub-tasks. It's very convenient to see the big picture beforehand, so take your time to complete it.
* [AWS Glue Getting Started](https://explore.skillbuilder.aws/learn/course/internal/view/elearning/8171/getting-started-with-aws-glue) (AWS Skill Builder)

**Goal:**
* write a Python script to transform windowed metric documents from CSV files to Parquet files
* run the script using Glue ETL
* optional - write another Glue ETL job to ingest plain metrics documents from CSV and calculating windows on them

**Instructions:**
* create an S3 bucket for storing the windowed metrics in the CSV format
* create an S3 bucket for storing the windowed metrics in the Parquet format
* use the CSV metrics task provided by the test data generator and copy the resulting files to the CSV bucket
* follow [this guide](https://docs.aws.amazon.com/prescriptive-guidance/latest/patterns/three-aws-glue-etl-job-types-for-converting-data-to-apache-parquet.html) and **pay attention to the points below**
    * use the CSV bucket as the _input_loc_ parameter
    * use the Parquet bucket as the _output_loc_ parameter
    * make sure to choose the **Python shell** job type and **0.0625 DPU** of compute
    * the instruction contains the code including the `setuptools.easy_install` Python library which is deprecated, so instead:
        * download the following package of the `pyarrow` library: [link](https://files.pythonhosted.org/packages/9f/c2/ae15d52e13a8274aaf113b28a401121d519267b590351b815346f3af4ca2/pyarrow-3.0.0-cp39-cp39-manylinux2014_x86_64.whl);
        * don't rename it and just upload it as-is into any S3 folder your Glue job role has access to;
        * go to Job Details -> Advanced Properties -> Libraries -> Python Library path and put the `pyarrow` S3 path to there, like `s3://bucket/prefix/pyarrow-3.0.0-cp39-cp39-manylinux2014_x86_64.whl`
        * remove the lines 7-12 selected on the screen from your script code: ![image.png](./image.png)
        * run the job

**Cost management recommendations:**
* make sure the ETL job is shut down

**Optional instructions:**
* create another S3 bucket for storing plain (non-windowed) metrics events
* use the CSV metrics task provided by the test data generator and copy the resulting files to the bucket
* create a Python shell Glue job similar to the CSV-Parquet converter above
* customise the new job code
    * aggregate the metrics into 5-minute windows
    * calculate min/max/average metrics values per window
    * write the resulting windows to theS3 Parquet bucket already used for the basic job version
* windowing operations provided by the Pandas library may be used
    * [window indexing](https://pandas.pydata.org/docs/user_guide/window.html#custom-window-rolling)
    * [rolling windows](https://pandas.pydata.org/docs/user_guide/window.html#rolling-apply)

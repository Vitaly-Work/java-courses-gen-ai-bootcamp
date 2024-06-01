# Sub-task 1 - ingesting with Glue ETL

![task1](../materials/diagrams/task1-focus.png)

# Learning prerequisites
* [Data engineering with AWS](https://learn.epam.com/detailsPage?id=b478ad0c-a4cc-4d27-a093-1d4661bfc19e) (EPAM Learn course, it will be assigned to you by the organizers)
  * **NOTE**: Although only sections 1-2 are required for this sub-task, this course gives a high-level overview of most services you will use
    in subsequent sub-tasks. It's very convenient to see the big picture beforehand, so take your time to complete it.
* [AWS Glue Getting Started](https://explore.skillbuilder.aws/learn/course/internal/view/elearning/8171/getting-started-with-aws-glue) (AWS Skill Builder)

# Goal
* write a Python script to transform windowed metric documents from CSV files to Parquet files
* run the script using Glue ETL
* optional - write another Glue ETL job to ingest plain metrics documents from CSV and calculating windows on them

# Instructions

## Step 0 - create shared artefacts
The following things are required as part of the final exam evaluation for **all the AWS practical tasks**. Also, this will help to keep things
organised and easy to track.
* Code project
  * create a code project in your favourite IDE
  * it's recommended to create one module/folder per task in it
  * put it under version control - use either EPAM GitLab or any online repo like GitHub - just be ready to share access with your mentor/peer
* S3 location for tracking code that will run in AWS - **we will refer to it as Source Code Bucket in upcoming tasks**
  * you may create multiple S3 buckets per type of code (Python, Java, CloudFormation, etc)
  * or you may choose to have one bucket with folders in it
  * **make sure to include your name** (e.g. `ybaranouski` or `Yauhen_B` or else) in the bucket name
  * you will upload the following things there
    * any Python scripts/libraries
    * Cloud Formation templates - if you choose to automate resource creation (optional)
    * pre-built JARs

## Step 1 - setting up the inputs and outputs
* create S3 buckets
  * **make sure the bucket names include your name** (e.g. `ybaranouski` or `Yauhen_B` or else)
  * create a bucket for storing the windowed metrics in the CSV format
  * create a bucket for storing the windowed metrics in the Parquet format
  * use the CSV metrics task provided by the test data generator and copy the resulting files to the CSV bucket

## Step 2 - create an ETL job
* follow [this guide](https://docs.aws.amazon.com/prescriptive-guidance/latest/patterns/three-aws-glue-etl-job-types-for-converting-data-to-apache-parquet.html) to create a Glue ETL job
* **pay attention to the following pitfalls**:
  * do not use nor Visual Editor, nor Notebooks - these will cost you money 
  * the instruction contains the code including the `setuptools.easy_install` Python library which is deprecated, so instead:
      * download the following package of the `pyarrow` library: [link](https://files.pythonhosted.org/packages/9f/c2/ae15d52e13a8274aaf113b28a401121d519267b590351b815346f3af4ca2/pyarrow-3.0.0-cp39-cp39-manylinux2014_x86_64.whl);
      * don't rename it and just upload it as-is into your **Source Code Bucket**
      * make sure your job has access to your **Source Code Bucket**
      * go to Job Details -> Advanced Properties -> Libraries -> Python Library path and put the `pyarrow` S3 path to there, like `s3://<your_source_code_bucket_folder>/pyarrow-3.0.0-cp39-cp39-manylinux2014_x86_64.whl`
      * remove the lines 7-12 selected on the screen from your script code: ![image.png](./image.png)

## Step 3 - run the ETL job
* run the job once for every generated CSV
  * use the CSV bucket as the _input_loc_ parameter for this
  * use the Parquet bucket as the _output_loc_ parameter
* make sure your _output_loc_ has the following format: `<bucket-name>/<component-name>/`, for example `ybaranouski-dt4j-metrics-table/order-service/`
* make sure you've chosen **Script editor** section for editing Python code (other options can bring about opening interactive session and be a cause of extra charges)
* make sure to choose the **Python shell** job type and **0.0625 DPU** of compute

# Cost management recommendations
* make sure the ETL job is shut down

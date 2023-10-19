# Sub-task 2 - querying with Glue DataCatalog and Athena

![](../materials/diagrams/task2-focus.png)

**Goal:**
* create a DataCatalog table for the windowed metrics dataset
* query the table using Athena

**Instructions:**
* create a Data Catalog database - this one will be used not only for this sub-task, so name it properly
* recap the _Windowed metrics_ model
* [manually create a Data Catalog table](https://docs.aws.amazon.com/glue/latest/dg/console-tables.html)
    * choose the _Parquet_ format
    * associate the table with the previously created database
    * point the table to the S3 windowed metrics (Parquet) bucket created in sub-task 1
    * partition by the _metricName_ and _componentName_ columns
    * add columns according to the model
* open Athena in the AWS Console and locate the Data Catalog database created above
* experiment and make some SQL queries to the Metrics table

**Cost management recommendations:**
* in case you tested the ETL job in conjunction with Athena, make sure the job is shut down
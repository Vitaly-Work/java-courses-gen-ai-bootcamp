# Spark SQL

## Topics to cover:

1. Spark SQL basics
2. Delta Lake and Delta Engine. Lakehouse
3. Table Versioning; Bronze/silver/gold layers
4. Operations under Delta Tables
5. Schema Enforcement and Evolution

## Materials:

If you're a part of the "Data training for developers" course, you're automatically assigned to the [Spark SQL](https://learn.epam.com/detailsPage?id=47fd0a7f-175b-4248-bf96-f13c8cf0161d) self-study course to cover this topic*. Otherwise, you can also start this course as a self-study one by yourself.

In case of any issues with access, please, contact [the L&D team](mailto:AskLearn@epam.com).

## *Read-only copy of the materials

### Lesson Introduction

Relational databases are used by most organizations for different tasks—from running and tracing a massive amount of information to processing transactions. Structured query language (SQL) is undoubtedly the most popular language when it comes to databases. It is easy to learn and helps you start the data extraction process. However, there is a significant challenge with SQL when dealing with massive datasets. This is where Spark SQL takes a front seat and bridges the gap.

### Spark SQL Basics

<img src="https://elearn.epam.com/assets/courseware/v1/c0cb52e5fa7b9f338e9190c339476393/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9001_image_Spark_SQL_Basic.svg" style="padding-bottom: 20px; height: 139px" alt="IMG">

Many data scientists, analysts, and business intelligence users in general rely on interactive SQL queries for exploring data. Spark SQL is a Spark module for structured data processing. It provides a programming abstraction called DataFrames and can also act as a distributed SQL query engine. It enables unmodified Hadoop Hive queries to run up to 100x faster on existing deployments and data. It also provides powerful integration with the rest of the Spark ecosystem (e.g., integrating SQL query processing with machine learning).

Spark SQL brings native support for SQL to Spark and streamlines the process of querying data stored in RDDs (Spark's distributed datasets) and external sources. Spark SQL conveniently blurs the line between RDDs and relational tables. Unifying these powerful abstractions makes it easy for developers to intermix SQL commands querying external data with complex analytics, all within in a single application.

- Spark SQL uses a Hive metastore to manage the metadata of persistent relational entities (e.g., databases, tables, columns, and partitions) in a relational database (for fast access).
- A Hive metastore warehouse (aka spark-warehouse) is the directory where Spark SQL persists tables, whereas a Hive metastore (aka metastore_db) is a relational database for managing the metadata of persistent relational entities, e.g., databases, tables, columns, and partitions.
- By default, Spark SQL uses the embedded deployment mode of a Hive metastore with an Apache Derby database.

### Data Lakehouse: The Evolution of Data Management

Over the past few years, a new data management paradigm has emerged independently across many customers and use cases: the cloud data platform. Let's consider the evolution of data management below.

<img src="https://elearn.epam.com/assets/courseware/v1/dc54e6033858a0dc84b4b988d45c9383/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_l7_9002_GIF_Late1980_2010_2020.gif" alt="image" style="display:block; margin:auto;  width: 1000px; max-width: 100%;" height="auto;">

Doug Laney's classic three Vs hit the data management industry with a tsunami of data, and with it came a sea change in the types of analytics we can do with that data. Old school analytical databases had too much data to handle affordably without valuable data falling through the cracks. We had business demand for analysis of types of data we'd never even tried to deal with in the past, semi-structured data like JSON and Avro, log files from sensors and components, geospatial data, click stream data, and so on. We had data coming at us too fast for the old technology to take it in, clean it up, combine it with other data sets, and present it to do business in a useful way.
|||
|-------|-------|
|Data Warehouses<img src="https://elearn.epam.com/assets/courseware/v1/e73d855a7ddb873da62b57c7f9400d38/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9003_dots_Data_Warehouses.svg" style="width: 150px;" alt="IMG"> | Data warehouses have a long history in decision support and business intelligence applications. After its inception in the late 1980s, data warehouse technology continued to evolve. Massively parallel processing (MPP) architectures led to systems being able to handle larger amounts of data. While warehouses excel in handling structured data, most enterprises have to deal with unstructured and semi-structured data and data with high variety, velocity, and volume. Data warehouses are not suited for many of these use cases, and they are certainly not the most cost-efficient.|
|Data Lakes<img src="https://elearn.epam.com/assets/courseware/v1/979b010f24f5b7af83ea35875f37971a/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9004_dots_Data_Lakes.svg" style="width: 150px;" alt="IMG">|As companies began to collect large amounts of data from many different sources, architects began envisioning a single system to house data for many different analytic products and workloads.About a decade ago, companies began building data lakes—repositories for raw data—in a variety of formats. While suitable for storing data, data lakes lack some critical features. They do not support ACID transactions, do not enforce data quality, and their lack of consistency/isolation makes it almost impossible to mix appends and reads and batch and stream jobs.|  
|Diverse Data Applications<img src="https://elearn.epam.com/assets/courseware/v1/eae271bf9571446f8aa5ccc21172071b/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9005_dots_Diverse_Data_Applications.svg" style="width: 150px;" alt="IMG">|Companies require systems for diverse data applications, including SQL analytics, real-time monitoring, data science, machine learning and artificial intelligence. Most of the recent advances in the modern data landscape have involved building better models to process unstructured data (text, images, video, and audio). These are precisely the types of data for which a data warehouse is suboptimal.|
|A Multitude of Systems<img src="https://elearn.epam.com/assets/courseware/v1/55d60bdc5e1a9505eaa19f08d2110759/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9006_dots_A_Multitude_of_Systems.svg" style="width: 150px;" alt="IMG">|A common approach to building these systems for diverse data applications is to use multiple systems: a data lake, several data warehouses, streaming systems, time-series, graph, and image databases. Having a multitude of systems increases complexity and, more importantly, introduces delay as data professionals invariably need to move or copy data between different systems.|


**What is a cloud data platform?**

Recent innovations in data system design make the cloud data platform design pattern possible. This design involves implementing data warehouse data structures and data management features directly on the kind of low-cost storage used for data lakes.

A *data lakehouse* is what you would get if you redesigned data warehouses in the modern world now that cheap and highly reliable storage (in the form of object stores) is available. 

Key features:

- Storage is decoupled from compute;
- Support for diverse workloads, including SQL and analytics, data science, and machine learning;
- Support for diverse data types ranging from unstructured to structured data;
- Open storage formats, tools, and processing engines;
- Storage is decoupled from compute;
- End-to-end streaming.

### Delta Lake

Delta Lake is a technology for building robust data lakes and is a component of building a cloud data platform.

>Delta Lake is a storage solution specifically designed to work with Apache Spark and is read from and written to using Apache Spark.

A data lake built using Delta Lake is ACID compliant, meaning that the data stored inside of the data lake has guaranteed consistency. Thanks to this, Delta Lake is considered a robust data store, whereas a traditional data lake is not.

Delta Lake is comprised of the following elements: a delta table, delta files, a delta engine, a delta lake storage layer, and a delta transaction log.

### Delta Table

A delta table is a collection of data kept using the Delta Lake technology and consists of three components:

1. Delta files contain data and are kept in object storage.
2. The delta table is registered in the metastore, making it possible to have different versions of the table—for example, managed and unmanaged tables. There are a couple of metastore options you can use like Hive metastore or the AWS Glue catalog. For more info, see docs.databricks.com/metastores.
3. The delta transaction log is kept with the delta files in object storage. We will go over the main idea behind this later in this chapter.

Delta files can be available for many object stores with built in support for AWS S3, Microsoft Azure Storage, and HDFS. Support of Google cloud storage, Oracle Cloud Infrastructure and IBM Cloud object storage will be available in the upcoming releases.

<img src="https://elearn.epam.com/assets/courseware/v1/c4964e172f276e13cd5f6191c127c4bc/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9032_Delta_Table.svg" alt="image" style="display:block; margin:auto;  width: 450px; max-width: 100%;" height="auto;">

### Delta Files
By design, Delta Lake uses Parquet files (sometimes referred to as delta files) to store an organization’s data in their object storage. 
Parquet files are a state-of-the-art file format for keeping tabular data. They are faster and considered more powerful than traditional methods for storing tabular data because they store data using columns, not rows.
Delta files leverage all of the technical capabilities of working with Parquet files. However, they also track data versioning and metadata and store transaction logs to keep track of all commits made to a table or object storage directory to provide ACID transactions.  
|||
|-------|-------|
|Atomicity<img src="https://elearn.epam.com/assets/courseware/v1/2b8c08fe9faf35b8daeef9402f269289/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9013_tabs_Atomicity.svg" style="width: 75%; max-width: 150px;" alt="image">|Transactions are often composed of multiple statements. Atomicity guarantees that each transaction is treated as a single "unit," which either succeeds completely or fails completely: If any of the statements constituting a transaction fails to complete, the entire transaction fails, and the database is left unchanged. An atomic system must guarantee atomicity in every situation, including power failures, errors, and crashes. A guarantee of atomicity prevents updates to the database from occurring only partially, which can cause greater problems than rejecting the whole series outright. As a consequence, the transaction cannot be observed to be in progress by another database client. At one moment in time, it has not yet happened, and at the next, it has already been completed (or nothing happened if the transaction was canceled in progress).An example of an atomic transaction is a monetary transfer from bank account A to account B. It consists of two operations, withdrawing money from account A and saving it to account B. Performing these operations in an atomic transaction ensures that the database remains in a consistent state, i.e., that money is neither withdrawn nor credited if either of those two operations fail.|
|Consistency<img src="https://elearn.epam.com/assets/courseware/v1/0e64cd35866d5fd23dca82785243e6c5/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9014_tabs_Consistency.svg" style="width: 75%; max-width: 150px;" alt="image">|Consistency ensures that a transaction can only bring the database from one valid state to another, maintaining database invariants: Any data written to the database must be valid according to all defined rules, including constraints, cascades, triggers, and any combination thereof. This prevents database corruption by an illegal transaction but does not guarantee that a transaction is correct. For example, in an application that transfers funds from one account to another, the consistency property ensures that the total value of funds in both accounts is the same at the start and end of each transaction.|
|Isolation<img src="https://elearn.epam.com/assets/courseware/v1/fa798443238aa19c97ee69e74892d5c3/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9015_tabs_Isolation.svg" style="width: 75%; max-width: 150px;" alt="image">|Transactions are often executed concurrently (e.g., multiple transactions reading and writing to a table at the same time). Isolation ensures that concurrent execution of transactions leaves the database in the same state that would have been obtained if the transactions had been executed sequentially. Isolation is the main goal of concurrency control; depending on the method used, the effects of an incomplete transaction might not even be visible to other transactions. For example, in an application that transfers funds from one account to another, the isolation property ensures that another transaction sees the transferred funds in one account or the other, but not in both or in neither.|
|Durability<img src="https://elearn.epam.com/assets/courseware/v1/45638948a1d280e547acb9f04916b4dd/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9016_tabs_Durability.svg" style="width: 75%; max-width: 150px;" alt="image">|Durability guarantees that once a transaction has been committed, it will remain committed even in the case of a system failure (e.g., power outage or crash). This usually means that completed transactions (or their effects) are recorded in non-volatile memory. For example, in an application that transfers funds from one account to another, the durability property ensures that the changes made to each account will not be reversed.|

### Delta Engine

Delta Engine is a high-performance, Apache Spark compatible query engine that provides an efficient way of processing data in data lakes, including data stored in the open-source Delta Lake. Delta Engine optimizations accelerate data lake operations, supporting a variety of workloads ranging from large-scale ETL processing to ad hoc interactive queries. Because Delta Lake is specifically designed to be used with Apache Spark, reads and writes made to delta tables benefit from the inherent massively parallel processing capabilities of Apache Spark.

When Apache Spark code is run to read and write to Delta Lake, the following optimizations are available: 
- File management optimizations, including compaction, data skipping, and localized data storage;
- Auto-optimized writes and file compaction;
- Performance optimization via delta caching.

### Delta Lake Storage Layer 

When building with Delta Lake, we store data using Delta Lake and then access it via Apache Spark. With this pattern, organizations have a highly performant, persistent storage layer built on low-cost, easily scalable object storage (Azure Data Lake Storage/ADLS, Amazon Web Services, or Simple Storage Service/S3).

>Keeping all of your data in files in object storage is the central design pattern that defines a data lake.

Using the Delta Lake storage layer design pattern ensures data consistency and allows for the flexibility of working with a data lake.

### Delta Transaction Log

The Delta Lake transaction log (also known as the DeltaLog) is an ordered record of every transaction that has ever been performed on a Delta Lake table since its inception. It is the Delta Lake central repository that serves as the single source of truth—the central repository that tracks all changes that users make to the table.

|||
|-------|-------|
|<img src="https://elearn.epam.com/assets/courseware/v1/910dbcaf6e9d40e9307356fb73aa5aa7/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9017_step-by-step_....no_interaction_from_the_user.svg" alt="Title" style="display:block; margin:auto;  width: 450px; max-width: 100%; padding-top: 10px;" height="auto;">|The process is seamless, with almost no interaction from the user. When a user creates a Delta Lake table, that table's transaction log is automatically created in the _delta_log subdirectory. As they make changes to the table, the changes are recorded as ordered atomic commits in the transaction log. Each commit is written out as a JSON file, starting with 000000.json. Additional changes to the table generate subsequent JSON files in ascending numerical order so that the next commit is written out as 000001.json, the following as 000002.json, and so on.|
||<img src="https://elearn.epam.com/assets/courseware/v1/558e8d23a968ca383677fbf8cabb5e92/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9018_image_step-by-step__...._interaction_from_the_user.svg" alt="image" style="display:block; margin:auto;  width: 450px; max-width: 100%; padding-top: 10px;" height="auto;">|
|<img src="https://elearn.epam.com/assets/courseware/v1/f349a1e6d7d4374280669553c1eaf105/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9021_step-by-step_1.parquet_and_2.parquet.svg" alt="Title" style="width: 350px;">|As an example, we might add additional records to our table from the data files 1.parquet and 2.parquet. This transaction would automatically be added to the transaction log and saved to the disk as commit 000000.json. Then, we might change our minds and decide to remove those files and add a new file instead (3.parquet). These actions would be recorded as the next commit in the transaction log, or as 000001.json, as shown below.|
||<img src="https://elearn.epam.com/assets/courseware/v1/d6b14ed3e2ffe05d2487e38b5a076c73/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9020_image_step-by-step__table.svg" alt="image" style="display:block; margin:auto;  width: 450px; max-width: 100%; padding-top: 10px;" height="auto;">|
|<img src="https://elearn.epam.com/assets/courseware/v1/82162bfdbcde84eafb04834824a0570b/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/Bd_S4_L7_9019_step-by-step_table.svg" alt="Title" style="width: 350px;">|Even though 1.parquet and 2.parquet are no longer part of our Delta Lake table, their addition and removal are still recorded in the transaction log because those operations were performed on our table, despite the fact that they ultimately canceled each other out. Delta Lake still retains atomic commits like these to ensure that in the event we need to audit our table or use "time travel" to see what our table looked like at a given point in time, we can do so accurately.|


### Delta Lake: Concepts and Features

Next, let's look at the fundamental concepts behind Delta Lake. Delta Lake is built upon three pillars that address reliability, performance, and engineering challenges. It provides clean, quality data; consistent views across batch and stream data workloads; and is optimized and easy to adopt.

**Pillar 1: Clean, Quality Data**
Delta Lake provides high quality and reliable data that is always ready for analytics through a range of features for ingesting, managing, and cleaning data.
According to this pillar, Delta Lake has the following features:
- "ACID transactions" ensure that only complete writes are committed.
- "Schema enforcement" automatically handles schema variations to prevent insertion of bad records during ingestion.
- "Time Travel," part of Delta Lake's built-in data versioning, enables rollbacks, full historical audit trails, and reproducible machine learning experiments.
- "Exactly once semantics" ensures that data is neither missed nor repeated erroneously.
**Pillar 2: Consistent Views Across Batch and Stream Data Workloads**
Delta Lake supports multiple simultaneous readers and writers for mixed batch and stream data.
According to this pillar, Delta Lake has the following features:
- "Snapshot isolation" provides support for multiple simultaneous writers and readers.
- "Mixed streaming and batching" data means that a table in Delta Lake is a batch table as well as a streaming source and sink. Streaming data ingestion, batch historic backfill, and interactive queries all work right out of the box.
**Pillar 3: Optimized and Easy to Adopt**
Delta Lake is easy to adopt and optimized for the cloud, and using Delta Lake avoids data lock-in. 
According to this pillar, Delta Lake has the following features:
- "Scalable metadata handling" leverages Spark's distributed processing power to handle all the metadata for petabyte-scale tables with billions of files with ease.
- "Effective on-premises" means that Delta Lake works well with Hadoop Distributed File System (HDFS) on-premises.
- "Compatibility with Spark APIs" means that Delta Lake is easy for Spark users to adopt.
- As an "open-source format," Delta Lake eliminates data lock-in. With Delta Lake, there is no requirement to use only Delta Lake.
- "Local development" means that Delta Lake supports laptop-based development and testing.
- "In-place import" allows efficient, fast import from Parquet to delta format.

### Time Travel
One of Delta Lake's most powerful features is time travel. This feature made the following use cases possible:

1. fixing mistakes in data using old versions of the data 
2. writing complex temporal queries
3. recreating analyses, reports, or outputs (for example, the output of a machine learning model), which could be useful for debugging or auditing, especially in regulated industries

Time travel also is shipped with easy-to-use API and SQL syntax. The following syntax is for time travel to a specific date or version for a table using SQL syntax:

```SQL
SELECT * FROM events TIMESTAMP AS OF '2018-10-18T22:15:12.013Z'
SELECT * FROM delta.`/delta/events` VERSION AS OF 123
```

This is equivalent to the syntax below using API calls:
```python
df1 = spark.read.format("delta").option("timestampAsOf", "2018-10-18T22:15:12.013Z").load("/delta/events")
df2 = spark.read.format("delta").option("versionAsOf", 123).load("/delta/events")
```
*To be able to use time travel for a pervious version, there is a simple rule: You must retain both the log and the data files for that version.*

*Running the vacuum command affects the above rule as you lose the ability to go back to a version older than the default 7-day data retention period since the vacuum removes the data files.*

### Schema Enforcement and Evolution
As business problems and requirements evolve over time, so does the structure of data. The two tools for controlling the schema of tables are schema enforcement and schema evolution. Schema enforcement prevents users from accidentally polluting their tables with mistakes or garbage data, while schema evolution enables them to automatically add new columns of rich data when those columns belong.

**Schema enforcement**
Schema enforcement, also known as schema validation, is a safeguard in Delta Lake that ensures data quality by rejecting writes to a table that do not match the table's schema. Like the front desk manager at a busy restaurant that only accepts reservations, it checks to see whether each column in data inserted into the table is on its list of expected columns (in other words, whether each one has a "reservation") and rejects any writes with columns that aren’t on the list.

Delta Lake uses schema validation on writes, which means that all new writes to a table are checked for compatibility with the target table's schema at write time. If the schema is not compatible, Delta Lake cancels the transaction altogether (no data is written) and raises an exception to let the user know about the mismatch.

To determine whether a write to a table is compatible, Delta Lake uses the following rules.

|||
|-------|-------|
|<img src="https://elearn.epam.com/assets/courseware/v1/ff3169f60525976148357cd17806d05d/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9022_dots_DataFrame_1_.svg" style="width: 250px;" alt="IMG">|The DataFrame to be written cannot contain any columns that are not present in the target table’s schema. Conversely, it’s OK if the incoming data doesn’t contain every column in the table; these columns will simply be assigned null values.|
|<img src="https://elearn.epam.com/assets/courseware/v1/ef60c58448985d8f719b5abae75716e4/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9023_dots_DataFrame_2_.svg" style="width: 250px;" alt="IMG">|The DataFrame to be written cannot have column data types that differ from the column data types in the target table. If a target table’s column contains StringType data but the corresponding column in the DataFrame contains IntegerType data, schema enforcement will raise an exception and prevent the write operation from taking place.|
|<img src="https://elearn.epam.com/assets/courseware/v1/e7d3a8db802152129284f36e99ad248c/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9024_dots_DataFrame_3_.svg" style="width: 250px;" alt="IMG">|*Can not contain column names that differ only by case.* This means that you cannot have columns such as ‘Foo’ and  ‘foo’ defined in the same table. While Spark can be used in case sensitive or insensitive (default) mode, Delta Lake is case-preserving but insensitive when storing the schema. Parquet is case sensitive when storing and returning column information. To avoid potential mistakes, data corruption or loss issues this restriction has been added.|

**Schema evolution**

Schema evolution is a feature that allows users to easily change a table's current schema to accommodate data that changes over time. Most commonly, it's used when performing an append or overwrite operation to automatically adapt the schema to include one or more new columns.

Following up on the example from the previous section, developers can easily use schema evolution to add the columns that were previously rejected due to a schema mismatch. Schema evolution is activated by adding  .option('mergeSchema', 'true') to your .write or .writeStream Spark command.

```python
# Add the mergeSchema option
loans.write.format("delta") \
           .option("mergeSchema", "true") \
           .mode("append") \
           .save(DELTALAKE_SILVER_PATH)
```
By including the mergeSchema option in your query, any columns that are present in the DataFrame but not in the target table are automatically added on to the end of the schema as part of a write transaction. Nested fields can also be added, and these fields will get added to the end of their respective structure columns as well.

|||
|-------|-------|
|Eligible changes<img src="https://elearn.epam.com/assets/courseware/v1/31d37dcae01ea914559c275a3a868b45/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9025_tabs_scheme.svg" style="width: 75%; max-width: 130px;" alt="image">|The following types of schema changes are eligible for schema evolution during table appends or overwrites: adding new columns (the most common scenario), changing of data types from NullType -> any other type, or upcasts from ByteType -> ShortType -> IntegerType|
|Ineligible changes<img src="https://elearn.epam.com/assets/courseware/v1/4beb2724c70b36baf64d51101c476b52/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9026_tabs_scheme_overwritting.svg" style="width: 75%; max-width: 150px;" alt="image">|Other changes, which are not eligible for schema evolution, require the *schema and data* to be overwritten by adding .option("overwriteSchema", "true"). For example, in the case where the column "Foo" was originally an integer data type and the new schema would be a StringType data, all of the Parquet (data) files would need to be rewritten. Those changes include: dropping a column, changing an existing column's data type (in place)б renaming column names that differ only by case (e.g., "Foo" and "foo")|

### Operations Under Delta Tables

There are many operations for which delta tables appear to be a good fit—whether you need to have batch deletes/reads/writes or even have your tables as sources/sinks for your streaming pipeline. The tight integration between Spark and delta format make such requirements possible with concise APIs and minimal changes.

**Spark SQL queries**
End users write Spark SQL queries against a table registered in the metastore. In other words, end users write simple queries against a delta table as if they were interacting with a standard SQL database.
<img src="https://elearn.epam.com/assets/courseware/v1/e75378d477e1414ea3a062f8b3676582/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9027_GIF_Operational_Data.gif" alt="image" style="display:block; margin:auto;  width: 650px; max-width: 100%;" height="auto;">

Delta Lake supports several statements to facilitate deleting data from and updating data in delta tables.

*Delete from a table*

You can remove data that matches a predicate from a delta table. For instance, to delete all events from before 2017, you can run the following:
```python
from delta.tables import *
from pyspark.sql.functions import *

deltaTable = DeltaTable.forPath(spark, "/data/events/")

deltaTable.delete("date < '2017-01-01'")        # predicate using SQL formatted string

deltaTable.delete(col("date") < "2017-01-01")   # predicate using Spark SQL functions
```
Note: "delete" removes data from the latest version of the delta table but does not remove it from the physical storage until the old versions are explicitly vacuumed; "vacuum" removes data files (not log files) no longer referenced by a delta table and that are older than the retention threshold by running the vacuum command on the table. "vacuum" is not triggered automatically. The default retention threshold for the files is 7 days.
```python
from delta.tables import *

deltaTable = DeltaTable.forPath(spark, pathToTable)  # path-based tables, or
deltaTable = DeltaTable.forName(spark, tableName)    # Hive metastore-based tables

deltaTable.vacuum()        # vacuum files not required by versions older than the default retention period

deltaTable.vacuum(100)     # vacuum files not required by versions more than 100 hours old
```
*Update a table*

You can update data that matches a predicate in a delta table. For example, to fix a spelling mistake in the eventType, you can run the following:
```python
from delta.tables import *
from pyspark.sql.functions import *

deltaTable = DeltaTable.forPath(spark, "/data/events/")

deltaTable.update("eventType = 'clck'", { "eventType": "'click'" } )   # predicate using SQL formatted string

deltaTable.update(col("eventType") == "clck", { "eventType": lit("click") } )   # predicate using Spark SQL functions
```
*To speed up the operations for update and delete queries, it's always a good idea to use predicates on the partition columns for a partitioned delta table.

*Upsert into a table using merge*

You can upsert data from a source table, view, or DataFrame into a target delta table using the MERGE SQL operation. Delta Lake supports inserts, updates, and deletes in MERGE and supports extended syntax beyond the SQL standards to facilitate advanced use cases.
Suppose you have a Spark DataFrame that contains new data for events with eventId. Some of these events may already be present in the events table. To merge the new data into the events table, you want to update the matching rows (that is, eventId, which is already present) and insert the new rows (that is, eventId, which is not present). You can run the following:
```python
from delta.tables import *

deltaTable = DeltaTable.forPath(spark, "/data/events/")

deltaTable.alias("events").merge(
    updatesDF.alias("updates"),
    "events.eventId = updates.eventId") \
  .whenMatchedUpdate(set = { "data" : "updates.data" } ) \
  .whenNotMatchedInsert(values =
    {
      "date": "updates.date",
      "eventId": "updates.eventId",
      "data": "updates.data"
    }
  ) \
  .execute()
```

*Table streaming reads and writes*

Delta Lake is deeply integrated with Spark Structured Streaming through readStream and writeStream. Delta Lake overcomes many of the limitations typically associated with streaming systems and files, including:

- coalescing small files produced by low-latency ingestion;
- maintaining "exactly-once" processing with more than one stream (or concurrent batch jobs);
- efficiently discovering which files are new when using files as the source for a stream;

There aren't many ways to change your existing streaming code to work with delta tables. As a matter of fact, all you need to do is change the option to read or write stream to delta format as follows:

```python
spark.readStream.format("delta").load("/mnt/delta/events")
spark.writeStream.format("delta").start("/mnt/delta/events")
```

### Delta Architecture
The delta architecture design pattern consists of landing data in successively cleaner Delta Lake tables from raw (bronze) to clean (silver) to aggregate (gold), as shown in the graphic below. 
<img src="https://elearn.epam.com/assets/courseware/v1/d96345e3e0c32e08055b5d2cd94223c7/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_BSG.gif" alt="image" style="display:block; margin:auto;  width: 640px; max-width: 100%;" height="auto;">
With delta architecture, multiple tables of data are kept in the same data lake. We write batch and stream data to the same table. Data is written to a series of progressively cleaner and more refined tables.
<img src="https://elearn.epam.com/assets/courseware/v1/15cff872934a874bba365ee289fc540b/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9029_step-by-step_Raw_data_ingested_into_a_bronze_data_lake_table.svg" alt="image" style="display:block; margin:auto;  width: 650px; max-width: 100%;" height="auto;">
This image depicts the first step in delta architecture, where a data lake ingests raw data. This data can come from stream or batch data sources and is stored in inexpensive commodity object storage, as shown by the bronze table in the image. The data in this table often has long retention (years) and can be saved "as-is," avoiding error-prone parsing at this stage. 
<img src="https://elearn.epam.com/assets/courseware/v1/238876c994964ca5bf08cf3f60fd976e/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9030_step-by-step_A_silver_data_lake_table_designated_as_the_single_source_of_truth__SSOT_.svg" alt="image" style="display:block; margin:auto;  width: 650px; max-width: 100%;" height="auto;">
This image depicts the second step in delta architecture, where the silver data lake is designed as the single source of truth. Data in the silver table is clean and, therefore, easily queryable and ready for insights. Because the table is easy to query, debugging is much easier. At this stage, bad records have been handled, and data types have been enforced already. 
<img src="https://elearn.epam.com/assets/courseware/v1/af5d8f680713670807b7a027f00cb8cb/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9031_step-by-step_The_query_layer_or_gold_table.svg" alt="image" style="display:block; margin:auto;  width: 650px; max-width: 100%;" height="auto;">
This is the third step in delta architecture, where cleaned aggregated data is available in a gold table, also known as a query layer. This data is ready for consumption via Apache Spark. 
At this stage, highly refined views and aggregate tables are presented mainly for the BI.

### The Power of Delta Lake
The true power of Delta Lake emerges when it is combined with Apache Spark. The benefits of combining Delta Lake with Apache Spark include:

*Separation of compute and storage*

A powerful paradigm in modern data storage and processing is the separation of compute and storage. Building systems with decoupled compute and storage provide benefits in terms of scalability, availability, and cost. In this system, Apache Spark loads and performs computations on the data. It does not handle permanent storage. Apache Spark works with Delta Lake, the first storage solution specifically designed to do so.

*Highly performant and reliable*

A data infrastructure built using Apache Spark and Delta Lake allows for the construction of a highly performant and reliable data lake. Delta Lake's ACID properties and the optimizations of the delta read-and-write engine make Delta Lake built on cloud-based object storage the best solution in its class.
<img src="https://elearn.epam.com/assets/courseware/v1/fe5d72d717b32144bdc62b37c05d0366/asset-v1:EPAM+BigData+0622_Spark+type@asset+block/BD_S4_L7_9033_dropdown_Highly_performant_and_reliable.svg" alt="img" style="display: block; height: 250px; margin: 15px auto;" class="dt-height"> 

*Support for Structured Streaming*

Delta Lake has native support for Structured Streaming via Apache Spark, allowing it to handle both batch and stream data sources. Data written to Delta Lake is immediately available for query. 

Support for immediate access to batch and stream data sources means that the delta architecture design pattern replaces the complex lambda architecture pattern. 

Building a data lake with Delta Lake at its core means that neither table repairs nor a complex lambda architecture is necessary.

### Lesson Summary

In this lesson, you learned the basics of Spark SQL and discovered the technologies that make it even more powerful. You considered the evolution of data management and Delta Lake, including its main components, concepts, and features. You also delved into schema enforcement and evolution, operations under delta tables, and delta architecture.

### Self-check
1. When a user issues a delete command on a delta table, data is removed from the physical storage immediately.
 - True
 - False

2. Which of the following is considered to be the single source of truth where data is cleaned, filtered, and augmented?
- Delta Lake bronze table
- Delta Lake silver table
- Delta Lake gold table

3. By default, delta tables have _______ of commit history retention.
- 7 days
- 14 days
- 21 days
- 30 days

4. Decide whether the following statement is true or false. 
Delta tables can be used as both a source and a destination in streaming pipelines.
- True
- False

5. A delta table transaction log is written in __________ format.
- Parquet
- JSON
- ORC
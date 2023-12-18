# GCP Big Data Practice Project

## Who is Data Engineer in GCP

According to the (https://cloud.google.com/learn/certification/data-engineer)

A Professional Data Engineer makes data usable and valuable for others by collecting, transforming, and publishing data. This individual evaluates and selects products and services to meet business and regulatory requirements. A Professional Data Engineer creates and manages robust data processing systems. This includes the ability to design, build, deploy, monitor, maintain, and secure data processing workloads.

The Professional Data Engineer exam assesses your ability to:

 - Design data processing systems
 - Ingest and process the data
 - Store the data
 - Prepare and use data for analysis
 - Maintain and automate data workloads

## Topics to cover
- Google Cloud Storage
  - Cloud Storage overview.
  - How to use to store data.
- Google BigQuery
  - BigQuesry storage overview. 
  - How to use to store data.
- Google Cloud Data Fusion
  - Cloud Data Fusion overview.
  - How to work with Data pipelines.
  - How to create and use Cloud Dataflow, error handling.
- Google Cloud Functions
  - Cloud functions overview.
  - How to use for data procesing.
- Google Looker Studio (Data Studio)
  - Google Looker Studio overview.
  - How to create report to visualize data.

## Summary
Architecture description for bigdata project with GCP services.

It provides a detailed solution to do just that using Data storage, Processing and Analytics with Google Cloud Platform.

In this project a Cloud Data Fusion Pipeline execution will be triggered automatically with a Cloud Function every time a new data file is uploaded to a Google Cloud Storage Bucket, this data pipeline will then perform some transformations on the data and load the results into a BigQuery table which feeds a report with a couple of visualizations created in Data Studio.

## Architecture
![gcp-practice drawio](./tasks/media/image59.png)

### Materials structure

Folders:

    /materials - scripts and test data for tasks

    /tasks     - tasks detailed description

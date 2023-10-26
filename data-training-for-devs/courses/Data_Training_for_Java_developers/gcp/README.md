# GCP Big Data Practice Project

## Summary
Architecture description for bigdata project with GCP services.

It provides a detailed solution to do just that using Data storage, Processing and Analytics with Google Cloud Platform.

In this project a Cloud Data Fusion Pipeline execution will be triggered automatically with a Cloud Function every time a new data file is uploaded to a Google Cloud Storage Bucket, this data pipeline will then perform some transformations on the data and load the results into a BigQuery table which feeds a report with a couple of visualizations created in Data Studio.

## Architecture
![gcp-practice drawio](/tasks/media/image59.png)

### Materials structure

Folders:

    /materials - scripts and test data for tasks

    /tasks     - tasks detailed description

### **Task 1: Create buckets that will be used for the data processing**

#### **Theory: Cloud storage** 

> ***Duration \[2h\]***
>
> [[Product overview of Cloud
> Storage]](https://cloud.google.com/storage/docs/introduction)

#### **Practice: Create buckets**

> ***Duration \[2h\]***
>
> Using the [[cloud
> shell]](https://cloud.google.com/shell/docs/launching-cloud-shell),
> create the following buckets in Cloud Storage. You can name them
> however you like, they just need to be unique, you will need to
> provide these later in future steps (remember to replace the bucket
> name in brackets \[\], with your own bucket names):
>
> gsutil mb gs://\[YOUR_DATA_SOURCE_BUCKET\]
>
> where is YOUR_DATA_SOURCE_BUCKET = csv-load-raw-source
>
> gsutil mb gs://csv-load-raw-source
>
> Bucket for raw CSV source files to be uploaded to GCP (files uploaded
> to this bucket will trigger the data transformation and data load
> process, this bucket will be used when writing the Cloud Function as
> the triggering event of the function).
>
> gsutil mb gs://\[YOUR_CDAP_TEMP_BUCKET\]
>
> where is YOUR_CDAP_TEMP_BUCKET = test-cdap-staging
>
> gsutil mb gs://test-cdap-staging
>
> Staging bucket that the Data Fusion pipeline will use as temporary
> store while inserting data into BigQuery.
>
> gsutil mb gs://\[YOUR_CDAP_ERRORS_BUCKET\] where is
> YOUR_CDAP_ERRORS_BUCKET = test-cdap-errors
>
> gsutil mb gs://test-cdap-errors
>
> Bucket that will hold the output of any errors during the data
> processing in the Cloud Data Fusion pipeline.
>
> Finally, we will upload a sample CSV file with a subset of data to
> make it easier to derive the data structure and schema using the
> Wrangler later on when we build the data pipeline with Data Fusion.
>
> gsutil cp SampleFile.csv gs://\[YOUR_DATA_SOURCE_BUCKET\] where is
> YOUR_DATA_SOURCE_BUCKET = csv-load-raw-source
>
> gsutil cp SampleFile.csv gs://csv-load-raw-source
>
> You can see at the file content:\
> gsutil cat -h gs://csv-load-raw-source/SampleFile.csv
>
> The source files that will feed the process has the following schema:

{

*// id*

**id**: **Long**,

*// code*

**status_code**: **Long**,

*// invoice*

**invoice_number**: **Long**,

*// category*

**item_category**: **String**,

*// channel*

**channel**: **String**,

*// order date*

**order_date**: **Datetime**,

*// delivery date*

**delivery_date**: **Datetime**,

*// amount*

**amount**: **Float**,

}

> There are some changes that need to be made prior to inserting the
> data into BigQuery, the date columns in the dataset are in the format
> dd-MM-yyyy H:mm and will need to be parsed to yyyy-MM-dd hh:mm in
> order for BigQuery to insert them correctly, also, in the float column
> the decimal separator are commas rather than points, we will also need
> to replace that to be able to insert the number as a float data type
> in BigQuery. We'll perform all these transformations later on in the
> Data Fusion pipeline.



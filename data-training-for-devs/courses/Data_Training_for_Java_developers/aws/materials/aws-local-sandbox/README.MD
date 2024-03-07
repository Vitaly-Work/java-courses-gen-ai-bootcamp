# Pre-requisites
- JDK 11 or 18 (Apache Flink focuses on JDK 11 support, JDK 17 is experimentally supported as of Flink 1.18)
- Python 3.9+
- Docker (or Rancher or similar - only basic containers + Docker Compose CLI required, no K8s needed)
- [PowerShell](https://learn.microsoft.com/en-us/powershell/scripting/install/installing-powershell-on-linux?view=powershell-7.4)
  - make sure to have PowerShell 7.x
  - available for Windows, Unix, and Mac OS

# One-time setup
## Create a Python virtual environment
- install [Virtualenv](https://virtualenv.pypa.io/en/latest/installation.html)
- open your CLI (terminal/shell/or else) in the `aws-loacl-sandbox` folder
- run `python -m virtualenv dt`
- run `.\dt\Scripts\activate` - **needed every time you run Python stuff for this project, or rely on session support in your CLI (e.g. in PowerShell)**
- confirm that `(dt)` appeared in front of your CLI input

## Install the necessary Python modules
- make sure the `dt` virtualenv is active (see the step above)
- `pip install boto3`
- `pip install pandas`
- `pip install python-dotenv`

## Configure environment variables
- create a `.env` file in the `local-infra` folder
- define the following values
```
S3_SECRET_KEY=<at_least_10_symbols>
S3_ACCESS_KEY=<at_least_10_symbols>
USER_HOME=<your user home directory, e.g. C:\Users\John_Doe - without a trailing slash>
AWS_ROLE_NAME=<firstname_lastname as in your EPAM email but in lowercase>
```

## Configure AWS credentials for Grafana
- make sure to complete `Configure environment variables` above
- open a PowerShell session in the `local-infra` folder
- if you use a personal AWS account
  - run `./manage-grafana.ps1 aws-account-refresh`
- if you use an EPAM AWS Sandbox
  - run `./manage-grafana.ps1 aws-sandbox-refresh`
- make sure `local-infra/dt4j-grafana/.aws/credentials` file is created

# Use cases

## Manage S3 buckets
- make sure your Docker daemon running and your virtualenv activated
- open a PowerShell session in the `local-infra` folder
- run `./manage-blob-storage.ps1 create` - may also be used to re-create the container and empty its buckets
- as a result, you will have a running MinIO container (S3 API compliant) with
  - two buckets: `metrics-batch-input` and `metrics-table`
  - authorisation set according to the `S3_SECRET_KEY` and `S3_ACCESS_KEY` from `.env`
- other commands
  - `./manage-blob-storage.ps1 start` - start the S3 container without re-creating data
  - `./manage-blob-storage.ps1 stop` - stop the S3 container but preserve its data
  - `./manage-blob-storage.ps1 destroy` - destroy the S3 container with all the data
  - `python upload-metrics-batch.py <absolute or relative file path>` - upload the given file to the `metrics-batch-input` local S3 bucket

## Manage AWS Glue ETL
- make sure your Docker daemon running and your virtualenv activated
- make sure to create the local S3 buckets (see **Manage S3 buckets**)
- open a PowerShell session in the `local-infra` folder
- run `./manage-glue.ps1 start`
- use the AWS Glue by either
  - opening the `http://127.0.0.1:8888/lab` URL in a browser - this is a Jupyter Lab UI, good for quick experimenting with Spark code in realtime
  - submitting batch jobs
    - prepare your code in `spark-applications/<job-name>/main.py` - note that the `spark-applications` folder is in `aws-local-sandbox` root folder, not `local-infra`
    - while still in the PowerShell in `local-infra`, run `./submit-glue-job.ps1 <job-name>`
- other commands
  - `./manage-glue.ps1 stop` - stop the Glue ETL container without destroying its data
  - `./manage-glue.ps1 destroy` - destroy the Glue ETL container with all its data

## Manage AWS EMR-like cluster
- make sure your Docker daemon running and your virtualenv activated
- make sure to create the local S3 buckets (see **Manage S3 buckets**)
- open a PowerShell session in the `local-infra` folder
- run `./manage-spark-cluster.ps1 start`
- submit jobs
  - prepare your code in `spark-applications/<job-name>/main.py` - note that the `spark-applications` folder is in `aws-local-sandbox` root folder, not `local-infra`
  - while still in the PowerShell in `local-infra`, run `./submit-spark-job.ps1 <job-name>`
- other commands
  - `./manage-spark-cluster.ps1 stop` - stop the Glue ETL container without destroying its data
  - `./manage-spark-cluster.ps1 destroy` - destroy the Glue ETL container with all its data

## Manage Grafana
- open a PowerShell session in the `local-infra` folder
- run `./manage-grafana.ps1 start`
- open `localhost:3000` in a browser
- use temporary credentials to login
  - user - `admin`
  - password - `admin`
- change your admin password (but do not forget it)
- to use the Athena plugin (this one is installed automatically with this setup)
  - add an Athena data source
  - in its configuration
    - use `Credentials file`
    - use `default` profile
    - use the region you have your AWS Glue/Athena resources created
    - use the output bucket path which you used to test AWS Athena with AWS Glue Catalog
- **pitfalls**
  - if you forgot your admin password destroy and start the Grafana container (see the commands below)
  - if you encounter errors while configuring or querying Athena data sources
    - this might be related to expired AWS credentials
    - try re-creating AWS credentials for Grafana by following `One-time steup > Configure AWS credentials for Grafana` above
- other commands
  - `./manage-grafana.ps1 stop` - stop the Grafana container without destroying its data
  - `./manage-grafana.ps1 destroy` - stop the Grafana container and fully remove all its data

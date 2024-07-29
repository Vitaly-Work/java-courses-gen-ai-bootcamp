# Step 1 - NeonDB setup
Account setup - follow the [docs here](https://neon.tech/docs/get-started-with-neon/signing-up#step-2-onboarding-in-the-neon-console)
- visit NeonDB and create an account
- use the free tier - it will be more than enough for this practical task

**Note:** If you already have a NeonDB account, just make sure you have some free disk storage capacity to complete the task.

Database setup
- create a database named **metrics** - follow the [docs here](https://neon.tech/docs/get-started-with-neon/signing-up#step-2-onboarding-in-the-neon-console)
- create a branch named **Dev** - follow the [docs here](https://neon.tech/docs/get-started-with-neon/signing-up#step-5-create-a-dedicated-development-branch)

Test the connectivity
- use your favourite DB explorer tool (for example, DBeaver)
- get connection credentials from NeonDB - follow the [docs here](https://neon.tech/docs/get-started-with-neon/connect-neon)

# Step 2 - Airflow project setup
Prerequisites
- install [Astro CLI](https://www.astronomer.io/docs/astro/cli/install-cli) - available for all platforms

Initialise the project structure:
```
./
-- dags/ # this host your Airflow code and data
---- data/
------ processed/
-- include/ # just keep empty
-- inputs/ # just keep empty
-- plugins/ # just keep empty
-- .dockerignore # for Docker builds, see below
-- .env # for env variables, may be empty
-- .gitignore # the usual, see an example below
-- Dockerfile # the Airflow image, see the details below
-- packages.txt # just keep empty
-- requirements.txt # extra dependencies for Airflow, see the details below
```

The `.dockerignore` recommended contents:
```
.astro
.git
.env
airflow_settings.yaml
logs/
```

The `.gitignore` recommended contents:
```
.astro/
.env
airflow_settings.yaml
dags/data/
dags/__pycache__/
```

The `Dockerfile`:
```
FROM quay.io/astronomer/astro-runtime:6.0.2
```

The `requirements.txt` has to specify a DBT Cloud integration dependency:
```
apache-airflow-providers-dbt-cloud==2.3.1
```

Run Airflow
- put a sample DAG to the `dags` folder - for example, use [this snippet](https://github.com/sungchun12/airflow-dbt-cloud/blob/main/dags/example-dag.py)
- from the root folder of you project, execute the following command: `astro dev start`
- wait until all the images are downloaded and Airflow is started - may take up to 10 minutes for the first time
- test that Airflow admin UI is available at `localhost:8080`
- try running your sample DAG

# Step 3 - Airflow-NeonDB integration
Create a role for Airflow:
- open [NeonDB SQL editor](https://neon.tech/docs/get-started-with-neon/query-with-neon-sql-editor) - make sure to choose your Dev branch and metrics DB
- create a **airflow-agent** role
- make sure to grant it the CREATE privileges on the metrics schema

Create a DB connection in Airflow
- recap or learn about connection management in Airflow - [the docs](https://airflow.apache.org/docs/apache-airflow/stable/howto/connection.html)
- follow [this guide](https://debruyn.dev/2024/connecting-neon-with-dbt-cloud/) to find out how to properly create a NeonDB connection
    - the DB login will be the name of the role created just above
- test the connection from Airflow UI and make sure no errors pop up

Prepare a test data model
- learn about the test data model [here](../../aws/TEST_DATA.md) - CSV-formatted metrics

Create an `init-db` DAG in Airflow
- use the following Python code or similar
```python
import psycopg2
import subprocess
from datetime import datetime

from airflow.operators.python import PythonOperator
from airflow.operators.bash import BashOperator
from airflow import DAG
from airflow.models import Connection

def install_dependencies():
    subprocess.run(['pip','install','psycopg2'])

def create_database_with_tables():
    connectionConfig = Connection.get_connection_from_secrets("neon-metrics-db")
    conn = psycopg2.connect(database=connectionConfig.schema,user=connectionConfig.login,password=connectionConfig.password,
                            host=connectionConfig.host,port=connectionConfig.port,sslmode=connectionConfig.extra_dejson["sslmode"])
    print('DB connected successfully')

    cursor = conn.cursor()
    cursor.execute("""
        CREATE TABLE metrics (
          componentName text,
          fromTimestamp text,
          "maxValue" double precision,
          metricName text,
          "minValue" double precision,
          toTimestamp text,
          unit text
        );
    """)

    conn.commit()
    conn.close()

database_init_dag = DAG(dag_id='database-init',
                         description='DAG for initialising a Postgre database',
                         schedule_interval=None,
                         start_date=datetime(2024,1,4))

task0 = PythonOperator(task_id='Install-dependencies',
                       python_callable=install_dependencies,
                       dag=database_init_dag)
task1 = PythonOperator(task_id='Create-Database-With-Tables',
                       python_callable=create_database_with_tables,
                       dag=database_init_dag)
```
- examine the code
    - notice the use of [connections API](https://airflow.apache.org/docs/apache-airflow/stable/_api/airflow/models/connection/index.html) for getting the DB URL and credentials securely
    - replace `neon-metrics-db` with the actual name of your DB connection in Airflow
    - to avoid automatic scheduling, `schedule_interval=None` is specified
- run the DAG and make sure the new table appears in NeonDB

# Step 4 - data ingestion setup
Get test data
- generate the test data
    - find out how to run the test data generator [here](../../aws/TEST_DATA.md)
    - run the test data generator from [here](../../aws/materials/test-data-generator-prebuilt) - use or customise the `metrics-batch.json` task config
- copy the resulting data to the `dags/data` folder of your Airflow project

Implement ingestion
- create a new DAG - `data-quality-pipeline`
    - add a step for installing Python dependencies
    - add a step which
        - connects to the DB - see the sample code from step 3
        - iterates over the files in the `dags/data` folder
        - copies each file to the DB, commits, and moves the file to the processed folder
    - hints
        - use the [COPY statement](https://www.postgresql.org/docs/current/sql-copy.html) with [this from the Psycopg library](https://www.psycopg.org/docs/cursor.html#cursor.copy_expert)
        - do not forget to commit the transaction
        - use [this](https://medium.com/@rajatbelgundi/efficient-etl-cleaning-transforming-and-loading-csv-data-in-postgresql-with-airflow-in-a-0bf062a0ed41) to see an example of how to upload CSVs to Postgre
- run the DAG and make sure the data appears in NeonDB by querying it there

# Step 5 - DBT model setup
Create a role for DBT:
- make sure to choose your Dev branch and metrics DB
- create a **dbt-agent-role** role
- make sure to grant it the CREATE privileges on the metrics schema

Set up a DBT Cloud project
- create a free [DBT Cloud account](https://www.getdbt.com/product/dbt-cloud)
- follow [this guide](https://debruyn.dev/2024/connecting-neon-with-dbt-cloud/) to see how to connect DBT Cloud to NeonDB
- initialise a project
    - familiarise with the [DBT project structure](https://docs.getdbt.com/docs/build/projects)
    - open your project in the [DBT Cloud IDE](https://docs.getdbt.com/docs/cloud/dbt-cloud-ide/develop-in-the-cloud) and explore it

Create environments
- create a [production environment](https://docs.getdbt.com/docs/deploy/deploy-environments) - required for running jobs
- create a `data-quality-check` job
    - follow [the docs](https://docs.getdbt.com/docs/deploy/deploy-jobs)
    - use the prod env created just above
    - disable scheduling - we will use only manual trigger
    - specify the following command to run: `dbt test`

Create a DBT model
- read about [models](https://docs.getdbt.com/docs/build/models) and [SQL models](https://docs.getdbt.com/docs/build/sql-models) in particular
- open your DBT project and create a development branch
- add a model that is based on a view of the metrics table
- commit the branch
- merge to main

Create a [data test](https://docs.getdbt.com/docs/build/data-tests)
- read about [data tests](https://docs.getdbt.com/docs/build/data-tests)
- switch to the development branch
- add a test that validates min/max values against some threshold
    - make sure the test fails if the threshold is set to some high value - run the job to check that
    - rollback
    - run the job again and ensure the test passes
# Step 6 - Airflow-DBT integration
- create a [DBT service account token](https://docs.getdbt.com/docs/dbt-cloud-apis/service-tokens) for Airflow
    - make sure to specify the `Job admin` permissions
    - **PITFALL:** do not forget to copy the token right after creating - it will not be visible afterwards
- add a connection in Airflow UI
    - choose DBT Cloud as the connection type
    - look up the domain/account values from the URL you used to work with your DBT Cloud project
- add another step to your `data-quality-pipeline` DAG from step 4
    - add the following import
    ```python
    from airflow.providers.dbt.cloud.operators.dbt import (
        DbtCloudRunJobOperator,
    )
    ```
    - add the following code
```python
    trigger_dbt_cloud_job_run = DbtCloudRunJobOperator(
           task_id="trigger_dbt_cloud_job_run",
           # your DBT connection name in Airflow
           dbt_cloud_conn_id="dbt-quality-pipeline",
           # your job ID
           job_id=123,
           check_interval=10,
           timeout=300,
       )
```
- run the DAG and make sure the DBT job is successfully triggered
# Optional step - automatic Airflow trigger
- explore Airflow sensors - special tasks that listen for external events - e.g. [file system](https://airflow.apache.org/docs/apache-airflow/stable/howto/operator/file.html#howto-operator-filesensor)
- use a sensor to make your `data-quality-pipeline` DAG run every time some files change in the `dags/data` folder
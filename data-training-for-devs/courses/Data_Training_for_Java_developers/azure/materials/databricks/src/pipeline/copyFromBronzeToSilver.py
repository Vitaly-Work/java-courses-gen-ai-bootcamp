# Databricks notebook source
# MAGIC %md
# MAGIC ## 1. Setup dependencies
# MAGIC - Import required libraries
# MAGIC

# COMMAND ----------

print('1. Setup dependencies...')

# COMMAND ----------

# MAGIC %md 
# MAGIC ## 2. Mounting
# MAGIC - List all available mounts
# MAGIC - Make sure that `/mnt/datalake_mount` is availble and points to your datalake storage.

# COMMAND ----------

print('2. Mounting...')

# COMMAND ----------

# MAGIC %md 
# MAGIC ## 3. Preparation
# MAGIC
# MAGIC - Import properties.py file using magic command. 
# MAGIC    The file contains usefull constants you might find handy for your code.
# MAGIC > Hint: use `%run magic` command
# MAGIC
# MAGIC - Read *destination* parameter passed from within Data Factory pipeline.
# MAGIC > Hint: use `dbutils.widgets.get` method.
# MAGIC
# MAGIC - Import patient schema `../pipeline/schema/patient.py`

# COMMAND ----------

print('3. Preparing...')

# COMMAND ----------

# MAGIC %md
# MAGIC ## 4. Reading
# MAGIC - Read json data from bronze layer. As a path use parameter read from Data Factory prefixed with mount endpoint.

# COMMAND ----------

print('4. Reading...')

# COMMAND ----------

# MAGIC %md
# MAGIC ## 5.Transformation
# MAGIC
# MAGIC - Select following columns and apply transformations for dataframe:
# MAGIC   >id => id
# MAGIC
# MAGIC   >gender => gender
# MAGIC
# MAGIC   >active => active
# MAGIC
# MAGIC   >resourceType => resource_type
# MAGIC
# MAGIC   >birthDate => to_date => birth_date
# MAGIC
# MAGIC   >name[0].family => family_name
# MAGIC
# MAGIC   >name[0].given => given_name
# MAGIC
# MAGIC   >telecom[1].value => work_phone
# MAGIC
# MAGIC   >telecom[2].value => mobile_phone
# MAGIC
# MAGIC   >address[0].city => city
# MAGIC
# MAGIC   >address[0].district => district
# MAGIC
# MAGIC   >address[0].state => state
# MAGIC
# MAGIC   >address[0].line[0] => line
# MAGIC
# MAGIC   >address[0].postalCode => postal_code
# MAGIC
# MAGIC   >add new column 'ingestion_date' with the value equal current timestamp.
# MAGIC
# MAGIC - Create new delta table (if not exists):
# MAGIC   >use patient schema defined above.
# MAGIC
# MAGIC   >location - silver_table_location from properties file.
# MAGIC
# MAGIC   >table name - silver_table_name from properties file.
# MAGIC
# MAGIC   >add new update_date column.
# MAGIC
# MAGIC - Merge existing patient data with received updates. Use 'id' to match records. If record is updated change the value of 'update_date' to current timestamp, otherwise leave it blank.
# MAGIC
# MAGIC - Execute *select* command using SQL syntax. Check if data is added.

# COMMAND ----------

print('5. Transforming...')

# COMMAND ----------

# MAGIC %md
# MAGIC ## 6. Storing
# MAGIC We need to pass id of the patient back to Data Factory pipeline. Return arrays of patient's ids that were modified. Use `dbutils.notebook.exit` method. The returned parameter should be array of ids converted to string.
# MAGIC
# MAGIC > Hint: use json.dumps method

# COMMAND ----------

print('6. Storing...')

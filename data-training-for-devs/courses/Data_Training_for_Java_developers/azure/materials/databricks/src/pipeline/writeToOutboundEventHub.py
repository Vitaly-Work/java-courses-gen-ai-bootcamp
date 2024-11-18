# Databricks notebook source
# MAGIC %md
# MAGIC ## 1. Import required libraries

# COMMAND ----------

# MAGIC %md 
# MAGIC ## 2. Make sure that change data feed support is enabled for the *silver_observations* table. 
# MAGIC It had to be enabled during table creation, otherwise alter table.

# COMMAND ----------

# MAGIC %md 
# MAGIC ## 3. Read data stream from silver_observations delta table.

# COMMAND ----------

# MAGIC %md 
# MAGIC ## 4. Configure eventhub connection string for outbound event hub topic. 
# MAGIC Use the following setting to read the string:
# MAGIC - scope: `keyvault-managed`
# MAGIC - key: `target-eventhub-connection-string`

# COMMAND ----------

# MAGIC %md 
# MAGIC ## 5. Select observation fields from inbound stream and join with the patient data
# MAGIC
# MAGIC * Select following observation fields from inbound stream:
# MAGIC   * systolic_pressure_value
# MAGIC   * systolic_interpretation
# MAGIC   * diastolic_pressure_value
# MAGIC   * diastolic_interpretation
# MAGIC   * ingestion_date
# MAGIC
# MAGIC * Join following data from patient table:
# MAGIC   * patient_id
# MAGIC   * family_name
# MAGIC   * given_name

# COMMAND ----------

# MAGIC %md 
# MAGIC ## 6. Prepare outbound event hub data as json
# MAGIC
# MAGIC Outbound event hub expects data to be in json format as *body* property. Transform your dataframe accordingly.
# MAGIC
# MAGIC >Hint: use the following expression `df.select(to_json(struct(*[c for c in df.columns])).alias("body"))`

# COMMAND ----------

# MAGIC %md 
# MAGIC ## 7. Write data stream to outbound event hub.

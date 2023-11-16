
  
# Task 8 - Consumption. Read Patient Gold Data.
> **Time to complete**: 1 hour
## Objective 
As soons as data ends up in Gold layer it's ready to power analytics, machine learning and production application. It contains not just information but also knowledge. Lakehouse architecture allows to query data directly from Delta lake, however sometimes it might not provide desired performance. In this case data is ingested to warehouse platform and fetched from that layer. For our reference architecture we will rely on Azure Synapse Serverless SQL Pool.
> Serverless SQL Pool is used as the cheapest and simple option for our case. However for better and predictable performance it's recommended to go with Dedicated SQL Pool.
 
![objective](../../materials/images/task8-objective.png)

## Steps
1. Go to Azure Portal and navigate to Azure Synapse Analytics.
2. From within workspace go to Synapse Studio.
3. Go to *Data -> Linked*.
4. Select *datalake->gold->patient_observation_table* folder.
5. Select *New SQL Script -> Select TOP 100 rows*.
6. Select *Delta* format as *file type*.
7. Run the generated query
8. Update the query and filter the result to return only record for patient with id *patient_01_id*.
## Validation

1. Make sure you've got result similar to this:
![result](../../materials/images/task8-result.png)

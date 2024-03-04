from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder \
   .appName("My App") \
   .getOrCreate()

rdd = spark.sparkContext.parallelize(range(1, 100))

print("THE SUM IS HERE: ", rdd.sum())
spark.stop()

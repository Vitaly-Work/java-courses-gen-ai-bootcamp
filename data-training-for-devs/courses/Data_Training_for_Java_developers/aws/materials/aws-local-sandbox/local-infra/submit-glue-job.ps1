param ($appName, $argument)

docker exec dt4j-glue-etl-glue-instance-1 `
    /home/glue_user/spark/bin/spark-submit "/home/glue_user/jobs/$appName/main.py" $argument

param ($appName)
Write-Host $appName

docker exec dt4j-spark-cluster-spark-master-1 `
    spark-submit --master spark://host.docker.internal:7077 "apps/$appName/main.py"

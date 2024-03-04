param ($action)
Write-Host $action

if ($action -eq "start")
{
    docker compose --project-directory dt4j-spark-cluster up -d
} elseif ($action -eq "stop")
{
    docker compose --project-directory dt4j-spark-cluster stop
} elseif ($action -eq "destroy")
{
    docker compose --project-directory dt4j-spark-cluster down
} elseif ($action -eq "recreate")
{
    docker compose --project-directory dt4j-spark-cluster down
    docker compose --project-directory dt4j-spark-cluster up -d
} else {
    throw "unknown action: $action"
}

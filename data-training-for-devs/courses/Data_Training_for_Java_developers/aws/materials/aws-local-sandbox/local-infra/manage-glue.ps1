param ($action)
Write-Host $action

if ($action -eq "start")
{
    docker compose --project-directory dt4j-glue-etl --env-file .env up -d
} elseif ($action -eq "stop")
{
    docker compose --project-directory dt4j-glue-etl stop
} elseif ($action -eq "destroy")
{
    docker compose --project-directory dt4j-glue-etl down
} elseif ($action -eq "recreate")
{
    docker compose --project-directory dt4j-glue-etl down
    docker compose --project-directory dt4j-glue-etl --env-file .env up -d
} else {
    throw "unknown action: $action"
}

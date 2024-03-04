param ($action)
Write-Host $action

if ($action -eq "start")
{
    docker compose --project-directory dt4j-blob-storage --env-file .env up -d
} elseif ($action -eq "stop")
{
    docker compose --project-directory dt4j-blob-storage stop
} elseif ($action -eq "destroy")
{
    docker compose --project-directory dt4j-blob-storage down
} elseif ($action -eq "create")
{
    docker compose --project-directory dt4j-blob-storage down
    docker compose --project-directory dt4j-blob-storage --env-file .env up -d
    Set-Location ".\dt4j-blob-storage"
    python create-buckets.py
    Set-Location "..\"
} else {
    throw "unknown action: $action"
}

param ($action)

. ./aws-auth.ps1

if ($action -Eq "aws-sandbox-refresh")
{
    $credentials = Get-Aws-Credentials
    $accessKeyId = $credentials.roleCredentials.accessKeyId
    $secretAccessKey = $credentials.roleCredentials.secretAccessKey
    $sessionToken = $credentials.roleCredentials.sessionToken

    $credentialsFile = ".\dt4j-grafana\.aws\credentials"

    Clear-Content $credentialsFile
    Out-File -FilePath $credentialsFile -Append -InputObject "[default]" -Encoding utf8NoBOM
    Out-File -FilePath $credentialsFile -Append -InputObject "aws_access_key_id=$accessKeyId" -Encoding utf8NoBOM
    Out-File -FilePath $credentialsFile -Append -InputObject "aws_secret_access_key=$secretAccessKey" -Encoding utf8NoBOM
    Out-File -FilePath $credentialsFile -Append -InputObject "aws_session_token=$sessionToken" -Encoding utf8NoBOM
    Out-File -FilePath $credentialsFile -Append -InputObject "region=us-east-1" -Encoding utf8NoBOM

    (Get-Content $credentialsFile -Raw).Replace("`r`n", "`n") | Set-Content $credentialsFile -Force
} elseif ($action -Eq "aws-account-refresh")
{
    $config = @{ }
    Get-Content -Path ".\.env" | Foreach `
    {
        $key, $value = $_.split('=')
        $config.add($key, $value)
    }

    $userHome = $config["USER_HOME"]
    $credentialsPath = "$userHome\.aws\credentials"
    Write-Host "copying $credentialsPath"

    $credentialsCopyPath = "dt4j-grafana\.aws\credentials"
    Copy-Item -Path $credentialsPath -Destination $credentialsCopyPath
    (Get-Content $credentialsCopyPath -Raw).Replace("`r`n", "`n") | Set-Content $credentialsCopyPath -Force
} elseif ($action -Eq "start")
{
    docker compose --project-directory dt4j-grafana up -d
}elseif ($action -Eq "stop")
{
    docker compose --project-directory dt4j-grafana stop
}elseif ($action -Eq "start")
{
    docker compose --project-directory dt4j-grafana down
} else
{
    throw "unknown action: $action"
}
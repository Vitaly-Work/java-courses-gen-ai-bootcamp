$config = @{}
aws sso login --profile sandx

Get-Content -Path ".\.env" | Foreach `
{
    $key, $value = $_.split('=')
    $config.add($key, $value)
}

$userHome = $config["USER_HOME"]
Write-Host "working with USER_HOME = $userHome"

$awsHome = "$userHome\.aws"

$credentialsPath = "$awsHome\credentials"
$awsSandboxProfile = @{}
$awsSandboxProfileFound = $false

Get-Content $credentialsPath | Foreach `
{
    if ($_ -eq "[sandx]")
    {
        $awsSandboxProfileFound = $true
    } elseif ($awsSandboxProfileFound)
    {
        $key, $value = $_.split('=')
        if ($key -And $value)
        {
            $awsSandboxProfile.add($key.Trim(), $value.Trim())
        }
    }
}

if (-Not $awsSandboxProfileFound)
{
    throw "Sandx profile not found in $credentialsPath"
}

$ssoCachePath = "$awsHome\sso\cache"
$latestSsoCache = (Get-ChildItem -Attributes !Directory -Path $ssoCachePath | Sort-Object -Descending -Property LastWriteTime | select -First 1)
$latestSsoCachePath = "$ssoCachePath\$latestSsoCache"
Write-Host "processing SSO cache $latestSsoCachePath"

$ssoJson = Get-Content $latestSsoCachePath -Raw | ConvertFrom-Json

aws sso get-role-credentials --account-id $awsSandboxProfile["sso_account_id"] --role-name $config["AWS_ROLE_NAME"] --access-token $ssoJson.accessToken --region $ssoJson.region
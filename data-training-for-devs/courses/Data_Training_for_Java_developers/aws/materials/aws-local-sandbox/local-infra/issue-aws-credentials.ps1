. ./aws-auth.ps1
Write-Host (Get-Aws-Credentials | ConvertTo-Json)

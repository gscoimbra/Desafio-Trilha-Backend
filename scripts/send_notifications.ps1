# Sends subscription lifecycle notifications to the API.
# Flow: PURCHASED -> CANCELED -> PURCHASED (reativa)
# Usage: .\scripts\send_notifications.ps1

$baseUrl = "http://localhost:8080/api/subscriptions/notifications"
$subscriptionId = 1
$types = @("SUBSCRIPTION_PURCHASED", "SUBSCRIPTION_CANCELED", "SUBSCRIPTION_PURCHASED")

Write-Host "Sending subscription lifecycle notifications..."

foreach ($type in $types) {
    try {
        $body = @{ subscriptionId = $subscriptionId; type = $type } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri $baseUrl -Method Post -Body $body -ContentType "application/json" -TimeoutSec 10
        Write-Host "  $type`: 202 Accepted"
    }
    catch {
        Write-Host "  $type`: ERROR - $_" -ForegroundColor Red
        exit 1
    }
}

Write-Host "Done. Check subscription status and event_history in the database."

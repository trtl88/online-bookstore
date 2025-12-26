$base = 'http://localhost:8080'
Write-Host '--- LOGIN ---'
try {
    $body = @"
{"username":"john","password":"pass"}
"@
    $resp = Invoke-RestMethod -Method Post -Uri "$base/api/users/login" -ContentType 'application/json' -Body $body -ErrorAction Stop
    Write-Host 'LOGIN RESPONSE:'
    $resp | ConvertTo-Json -Depth 5
    $username = $resp.username
} catch {
    Write-Error "Login failed: $($_.Exception.Message)"
    exit 1
}

Write-Host '--- ADD TO CART ---'
try {
    $uri = "$base/api/cart/add?username=$username&isbn=978-0451524935&quantity=1"
    $add = Invoke-RestMethod -Method Post -Uri $uri -ErrorAction Stop
    Write-Host 'ADD RESPONSE:'; $add
} catch {
    Write-Error "Add to cart failed: $($_.Exception.Message)"
}

Write-Host '--- GET CART ---'
try {
    $cart = Invoke-RestMethod -Method Get -Uri "$base/api/cart/$username" -ErrorAction Stop
    $cart | ConvertTo-Json -Depth 5
} catch {
    Write-Error "Get cart failed: $($_.Exception.Message)"
}

Write-Host '--- CHECKOUT ---'
try {
    $uri2 = "$base/api/orders/checkout?username=$username&cc=4111111111111111&expiry=12/25"
    $co = Invoke-RestMethod -Method Post -Uri $uri2 -ErrorAction Stop
    Write-Host 'CHECKOUT RESPONSE:'; $co
} catch {
    Write-Error "Checkout failed: $($_.Exception.Message)"
}

Write-Host '--- ORDER HISTORY ---'
try {
    $orders = Invoke-RestMethod -Method Get -Uri "$base/api/orders/history/$username" -ErrorAction Stop
    $orders | ConvertTo-Json -Depth 10
    if ($orders.Count -gt 0) {
        $first = $orders[0].orderId
        Write-Host '--- ORDER DETAILS for' $first
        $d = Invoke-RestMethod -Method Get -Uri "$base/api/orders/details/$first" -ErrorAction Stop
        $d | ConvertTo-Json -Depth 10
    }
} catch {
    Write-Error "Order history/details failed: $($_.Exception.Message)"
}

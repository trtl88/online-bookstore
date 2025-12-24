<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Confirm Publisher Orders</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <style>
        .order-card { display: flex; justify-content: space-between; align-items: center; background: white; padding: 1.5rem; margin-bottom: 1rem; border-radius: 8px; border-left: 5px solid #f59e0b; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .order-info h4 { margin-bottom: 0.5rem; }
        .badge { background: #fef3c7; color: #d97706; padding: 0.2rem 0.6rem; border-radius: 4px; font-size: 0.8rem; font-weight: bold; }
    </style>
</head>
<body>

    <nav class="navbar">
        <a href="admin_dashboard.php" class="logo">Admin Panel</a>
        <div class="nav-links">
            <a href="admin_dashboard.php">Back to Dashboard</a>
        </div>
    </nav>

    <div class="container">
        <h1>Pending Publisher Orders</h1>
        <p style="margin-bottom: 2rem;">Confirm receipt of stock from publishers.</p>

        <div class="order-card">
            <div class="order-info">
                <h4>Order #9921 - Pearson Education</h4>
                <p>Book: <strong>Database Systems</strong> (ISBN: 978-3-16-148410-0)</p>
                <p>Quantity Ordered: 50 Copies</p>
                <span class="badge">Pending Arrival</span>
            </div>
            <form action="../backend/confirm_order_logic.php" method="POST">
                <input type="hidden" name="order_id" value="9921">
                <button type="submit" class="btn-submit" style="background: #10b981; margin:0;">Confirm Receipt</button>
            </form>
        </div>

        <div class="order-card">
            <div class="order-info">
                <h4>Order #9922 - O'Reilly Media</h4>
                <p>Book: <strong>Learning PHP & MySQL</strong> (ISBN: 978-1-491-91866-1)</p>
                <p>Quantity Ordered: 20 Copies</p>
                <span class="badge">Pending Arrival</span>
            </div>
            <form action="../backend/confirm_order_logic.php" method="POST">
                <input type="hidden" name="order_id" value="9922">
                <button type="submit" class="btn-submit" style="background: #10b981; margin:0;">Confirm Receipt</button>
            </form>
        </div>

    </div>

</body>
</html>
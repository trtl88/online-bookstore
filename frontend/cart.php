<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Shopping Cart</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <style>
        /* Specific styles for the Cart Table */
        .cart-table { width: 100%; border-collapse: collapse; margin-top: 1rem; background: white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .cart-table th, .cart-table td { padding: 1rem; text-align: left; border-bottom: 1px solid #ddd; }
        .cart-table th { background-color: var(--primary); color: white; }
        .cart-summary { background: white; padding: 1.5rem; margin-top: 2rem; text-align: right; box-shadow: 0 2px 4px rgba(0,0,0,0.1); border-radius: 8px; }
        .btn-remove { background: #ef4444; color: white; padding: 0.5rem 1rem; border: none; border-radius: 4px; cursor: pointer; }
    </style>
</head>
<body>

    <nav class="navbar">
        <a href="index.php" class="logo">Alexandria Books</a>
        <div class="nav-links">
            <a href="index.php">Home</a>
            <a href="index.php">Continue Shopping</a>
        </div>
    </nav>

    <div class="container">
        <h1>Your Shopping Cart</h1>

        <table class="cart-table">
            <thead>
                <tr>
                    <th>Book Title</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Total</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Database Systems</td>
                    <td>$50.00</td>
                    <td>1</td>
                    <td>$50.00</td>
                    <td><button class="btn-remove">Remove</button></td>
                </tr>
                <tr>
                    <td>Modern Art History</td>
                    <td>$35.99</td>
                    <td>2</td>
                    <td>$71.98</td>
                    <td><button class="btn-remove">Remove</button></td>
                </tr>
            </tbody>
        </table>

        <div class="cart-summary">
            <h3>Total Amount: <span style="color: var(--primary); font-size: 1.5rem;">$121.98</span></h3>
            <p style="color: #666; margin-bottom: 1rem;">Taxes and shipping calculated at checkout</p>
            <a href="checkout.php" class="btn-submit" style="display: inline-block; width: auto; text-decoration: none; padding: 0.8rem 2rem;">
                Proceed to Checkout
            </a>
        </div>
    </div>

</body>
</html>
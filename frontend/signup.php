<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up - Online Bookstore</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>

    <nav class="navbar">
        <a href="index.php" class="logo">BookStore</a>
        <div class="nav-links">
            <a href="index.php">Home</a>
            <a href="login.php">Login</a>
        </div>
    </nav>

    <div class="container">
        <div class="form-container">
            <h2>Create New Account</h2>
            <form id="signupForm" action="../backend/register_user.php" method="POST">
                
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" name="username" required>
                </div>

                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" required>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <div class="form-group">
                        <label>First Name</label>
                        <input type="text" name="first_name" required>
                    </div>
                    <div class="form-group">
                        <label>Last Name</label>
                        <input type="text" name="last_name" required>
                    </div>
                </div>

                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" name="email" required>
                </div>

                <div class="form-group">
                    <label>Phone Number</label>
                    <input type="tel" name="phone" required>
                </div>

                <div class="form-group">
                    <label>Shipping Address</label>
                    <input type="text" name="address" required>
                </div>

                <button type="submit" class="btn-submit">Sign Up</button>
            </form>
            <p style="margin-top: 1rem; text-align: center;">
                Already have an account? <a href="login.php">Login here</a>
            </p>
        </div>
    </div>

</body>
</html>
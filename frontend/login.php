<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Alexandria Bookstore</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>

    <nav class="navbar">
        <a href="index.php" class="logo">Alexandria Books</a>
        <div class="nav-links">
            <a href="index.php">Home</a>
            <a href="signup.php" class="btn">Sign Up</a>
        </div>
    </nav>

    <div class="container">
        <div class="form-container">
            <h2 style="text-align: center; margin-bottom: 1.5rem;">Welcome Back</h2>
            
            <form action="../backend/login_logic.php" method="POST">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" name="username" required>
                </div>

                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" required>
                </div>

                <button type="submit" class="btn-submit">Login</button>
            </form>

            <p style="margin-top: 1rem; text-align: center;">
                New here? <a href="signup.php">Create an account</a>
            </p>
        </div>
    </div>

</body>
</html>
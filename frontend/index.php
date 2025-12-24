<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Alexandria Bookstore</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>

    <nav class="navbar">
        <a href="index.php" class="logo">Alexandria Books</a>
        <div class="nav-links">
            <a href="index.php">Home</a>
            <a href="cart.php">Cart (0)</a>
            <a href="login.php" class="btn">Login</a>
            <a href="signup.php" class="btn">Sign Up</a>
        </div>
    </nav>

    <div class="container">
        <div class="search-section" style="background: white; padding: 1.5rem; border-radius: 8px; margin-bottom: 2rem; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
            <form action="index.php" method="GET" style="display: flex; gap: 1rem; flex-wrap: wrap;">
                <input type="text" name="search" placeholder="Search by Title, ISBN, or Author..." style="flex: 2; padding: 0.8rem; border: 1px solid #ccc; border-radius: 4px;">
                
                <select name="category" style="flex: 1; padding: 0.8rem; border: 1px solid #ccc; border-radius: 4px;">
                    <option value="">All Categories</option>
                    <option value="Science">Science</option>
                    <option value="Art">Art</option>
                    <option value="Religion">Religion</option>
                    <option value="History">History</option>
                    <option value="Geography">Geography</option>
                </select>

                <button type="submit" class="btn-submit" style="width: auto; padding: 0 2rem;">Search</button>
            </form>
        </div>

        <h2 style="margin-bottom: 1rem;">Featured Books</h2>
        <div class="book-grid">
            
            <div class="book-card">
                <div style="height: 200px; background: #ddd; display: flex; align-items: center; justify-content: center; margin-bottom: 1rem;">
                    <span style="color: #666;">No Cover Image</span>
                </div>
                <h3>Database Systems</h3>
                <p style="color: #666; font-size: 0.9rem;">By Prof. Yousry Taha</p>
                <p style="font-weight: bold; color: var(--primary); margin: 0.5rem 0;">$50.00</p>
                <div style="display: flex; gap: 0.5rem;">
                    <button class="btn-submit">Add to Cart</button>
                    <button class="btn-submit" style="background: #64748b;">Details</button>
                </div>
            </div>

            <div class="book-card">
                <div style="height: 200px; background: #ddd; display: flex; align-items: center; justify-content: center; margin-bottom: 1rem;">
                    <span style="color: #666;">No Cover Image</span>
                </div>
                <h3>Modern Art History</h3>
                <p style="color: #666; font-size: 0.9rem;">By John Doe</p>
                <p style="font-weight: bold; color: var(--primary); margin: 0.5rem 0;">$35.99</p>
                <div style="display: flex; gap: 0.5rem;">
                    <button class="btn-submit">Add to Cart</button>
                    <button class="btn-submit" style="background: #64748b;">Details</button>
                </div>
            </div>
            
            <div class="book-card">
                <div style="height: 200px; background: #ddd; display: flex; align-items: center; justify-content: center; margin-bottom: 1rem;">
                    <span style="color: #666;">No Cover Image</span>
                </div>
                <h3>World Geography</h3>
                <p style="color: #666; font-size: 0.9rem;">By Jane Smith</p>
                <p style="font-weight: bold; color: var(--primary); margin: 0.5rem 0;">$24.50</p>
                <div style="display: flex; gap: 0.5rem;">
                    <button class="btn-submit">Add to Cart</button>
                    <button class="btn-submit" style="background: #64748b;">Details</button>
                </div>
            </div>

        </div>
    </div>

    <script src="assets/js/main.js"></script>
</body>
</html>
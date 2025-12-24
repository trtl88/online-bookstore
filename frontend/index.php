<body>

    <nav class="navbar">
        <a href="index.php" class="logo">Alexandria<span style="color: var(--dark);">Books</span></a>
        <div class="nav-links">
            <a href="index.php">Home</a>
            <a href="cart.php">Cart (0)</a>
            <a href="login.php">Login</a>
            <a href="signup.php" class="btn">Get Started</a>
        </div>
    </nav>

    <header class="hero">
        <h1>Find Your Next Adventure</h1>
        <p>Browse thousands of books from Science to History at the best prices.</p>
        
        <form action="index.php" method="GET" style="max-width: 700px; margin: 0 auto; display: flex; gap: 0.5rem; background: white; padding: 0.5rem; border-radius: 50px;">
            <input type="text" name="search" placeholder="Search for books, authors, ISBN..." style="flex: 1; border: none; padding: 0 1.5rem; font-size: 1rem; outline: none;">
            <select name="category" style="border: none; border-left: 1px solid #ddd; padding: 0 1rem; outline: none; background: transparent;">
                <option value="">All Categories</option>
                <option value="Science">Science</option>
                <option value="Art">Art</option>
                <option value="History">History</option>
            </select>
            <button type="submit" class="btn-submit" style="width: auto; margin: 0; border-radius: 40px; padding: 0.8rem 2rem;">Search</button>
        </form>
    </header>

    <div class="container">
        <h2 style="margin-bottom: 1.5rem; font-weight: 600; color: var(--dark);">Trending Now</h2>
        <div class="book-grid">
            
            <div class="book-card">
                <img src="https://images.unsplash.com/photo-1544947950-fa07a98d237f?auto=format&fit=crop&w=400&q=80" alt="Book Cover">
                <div class="card-content">
                    <h3>Database Systems</h3>
                    <p style="color: #666; font-size: 0.9rem; margin-bottom: 0.5rem;">Prof. Yousry Taha</p>
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-weight: 700; color: var(--primary); font-size: 1.2rem;">$50.00</span>
                        <button class="btn-submit" style="width: auto; margin: 0; padding: 0.5rem 1rem; font-size: 0.9rem;">Add</button>
                    </div>
                </div>
            </div>

            <div class="book-card">
                <img src="https://images.unsplash.com/photo-1543002588-bfa74002ed7e?auto=format&fit=crop&w=400&q=80" alt="Book Cover">
                <div class="card-content">
                    <h3>Modern Art History</h3>
                    <p style="color: #666; font-size: 0.9rem; margin-bottom: 0.5rem;">John Doe</p>
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-weight: 700; color: var(--primary); font-size: 1.2rem;">$35.99</span>
                        <button class="btn-submit" style="width: auto; margin: 0; padding: 0.5rem 1rem; font-size: 0.9rem;">Add</button>
                    </div>
                </div>
            </div>

            <div class="book-card">
                <img src="https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=400&q=80" alt="Book Cover">
                <div class="card-content">
                    <h3>World Geography</h3>
                    <p style="color: #666; font-size: 0.9rem; margin-bottom: 0.5rem;">Jane Smith</p>
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-weight: 700; color: var(--primary); font-size: 1.2rem;">$24.50</span>
                        <button class="btn-submit" style="width: auto; margin: 0; padding: 0.5rem 1rem; font-size: 0.9rem;">Add</button>
                    </div>
                </div>
            </div>

        </div>
    </div>
    
    <script src="assets/js/main.js"></script>
</body>
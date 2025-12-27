// frontend/assets/js/books.js
const BOOK_API = "http://localhost:8080/api/books";

document.addEventListener("DOMContentLoaded", () => {
    loadBooks(); // Load all books by default

    // Listen for Search Form Submission
    const searchForm = document.getElementById("search-form");
    if(searchForm) {
        searchForm.addEventListener("submit", (e) => {
            e.preventDefault();
            const query = document.getElementById("search-input").value;
            const category = document.getElementById("category-select").value;
            loadBooks(query, category);
        });
    }
});

async function loadBooks(query = "", category = "") {
    const grid = document.querySelector(".book-grid");
    if (!grid) return;

    grid.innerHTML = "<p>Loading books...</p>";

    try {
        let url = BOOK_API;
        
        // Logic to determine which API endpoint to call
        if (query && category) {
            url = `${BOOK_API}/search?query=${encodeURIComponent(query)}&category=${encodeURIComponent(category)}`;
        } else if (query) {
            url = `${BOOK_API}/search?query=${encodeURIComponent(query)}`;
        } else if (category) {
            url = `${BOOK_API}/category/${encodeURIComponent(category)}`;
        }

        const response = await fetch(url);
        const books = await response.json();

        grid.innerHTML = ""; // Clear loading message

        if (books.length === 0) {
            grid.innerHTML = "<p>No books found.</p>";
            return;
        }

        books.forEach(book => {
            const card = document.createElement("div");
            card.className = "book-card";
            // Check stock logic
            const stockStatus = book.stockQuantity > 0 
                ? `<span style="color: green; font-size: 0.8rem;">In Stock (${book.stockQuantity})</span>` 
                : `<span style="color: red; font-size: 0.8rem;">Out of Stock</span>`;
            
            // Disable button if out of stock
            const disabled = book.stockQuantity > 0 ? "" : "disabled style='background: grey; cursor: not-allowed;'";

            card.innerHTML = `
                <img src="assets/img/book_placeholder.jpg" alt="${book.title}">
                <h3>${book.title}</h3>
                <p style="color: #666; font-size: 0.9rem;">${book.category}</p>
                <p style="font-weight: bold; color: var(--primary); margin: 0.5rem 0;">$${book.price}</p>
                ${stockStatus}
                <div style="display: flex; gap: 0.5rem; margin-top: 0.5rem;">
                    <button class="btn-submit" onclick="addToCart('${book.isbn}')" ${disabled}>Add to Cart</button>
                    <a href="book_details.html?isbn=${book.isbn}" class="btn-submit" style="background: #64748b; text-align: center; text-decoration: none;">Details</a>
                </div>
            `;
            grid.appendChild(card);
        });
    } catch (error) {
        console.error(error);
        grid.innerHTML = "<p>Error loading books. Is the backend running?</p>";
    }
}
// frontend/assets/js/books.js
const BOOK_API = "http://localhost:8080/api/books";

document.addEventListener("DOMContentLoaded", () => {
    loadBooks(); // Load all books by default

    // Listen for Search Form Submission
    const searchForm = document.getElementById("search-form");
    if(searchForm) {
        searchForm.addEventListener("submit", (e) => {
            e.preventDefault();
            const query = document.getElementById("search-input").value;
            const category = document.getElementById("category-select").value;
            loadBooks(query, category);
        });
    }
});

async function loadBooks(query = "", category = "") {
    const grid = document.querySelector(".book-grid");
    if (!grid) return;

    grid.innerHTML = "<p>Loading books...</p>";

    try {
        let url = BOOK_API;
        
        // Logic to determine which API endpoint to call
        if (query && category) {
            url = `${BOOK_API}/search?query=${encodeURIComponent(query)}&category=${encodeURIComponent(category)}`;
        } else if (query) {
            url = `${BOOK_API}/search?query=${encodeURIComponent(query)}`;
        } else if (category) {
            url = `${BOOK_API}/category/${encodeURIComponent(category)}`;
        }

        const response = await fetch(url);
        const books = await response.json();

        grid.innerHTML = ""; // Clear loading message

        if (books.length === 0) {
            grid.innerHTML = "<p>No books found.</p>";
            return;
        }

        books.forEach(book => {
            const card = document.createElement("div");
            card.className = "book-card";
            // Check stock logic
            const stockStatus = book.stockQuantity > 0 
                ? `<span style="color: green; font-size: 0.8rem;">In Stock (${book.stockQuantity})</span>` 
                : `<span style="color: red; font-size: 0.8rem;">Out of Stock</span>`;
            
            // Disable button if out of stock
            const disabled = book.stockQuantity > 0 ? "" : "disabled style='background: grey; cursor: not-allowed;'";

            card.innerHTML = `
                <img src="assets/img/book_placeholder.jpg" alt="${book.title}">
                <h3>${book.title}</h3>
                <p style="color: #666; font-size: 0.9rem;">${book.category}</p>
                <p style="font-weight: bold; color: var(--primary); margin: 0.5rem 0;">$${book.price}</p>
                ${stockStatus}
                <div style="display: flex; gap: 0.5rem; margin-top: 0.5rem;">
                    <button class="btn-submit" onclick="addToCart('${book.isbn}')" ${disabled}>Add to Cart</button>
                    <a href="book_details.html?isbn=${book.isbn}" class="btn-submit" style="background: #64748b; text-align: center; text-decoration: none;">Details</a>
                </div>
            `;
            grid.appendChild(card);
        });
    } catch (error) {
        console.error(error);
        grid.innerHTML = "<p>Error loading books. Is the backend running?</p>";
    }
}
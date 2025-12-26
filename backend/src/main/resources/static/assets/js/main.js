// assets/js/main.js
document.addEventListener('DOMContentLoaded', async function() {
    console.log("Bookstore Frontend Loaded");

    // Check server start time to detect a restart; if changed, clear stored user (force logout)
    try {
        const infoRes = await fetch('http://localhost:8080/api/server/info');
        if (infoRes.ok) {
            const info = await infoRes.json();
            const serverStart = String(info.serverStart || '');
            const seen = localStorage.getItem('serverStart');
            if (seen !== serverStart) {
                localStorage.removeItem('user');
                localStorage.setItem('serverStart', serverStart);
                if (!window.location.pathname.endsWith('index.html') && window.location.pathname !== '/') {
                    window.location.href = 'index.html';
                    return;
                }
            }
        }
    } catch (e) { console.warn('Could not fetch server info', e); }

    const grid = document.querySelector('.book-grid');
    if (!grid) return;

    // Fetch books from backend API and render them
    fetch('http://localhost:8080/api/books')
        .then(res => {
            if (!res.ok) throw new Error('Network response was not ok');
            return res.json();
        })
        .then(books => {
            // clear existing static cards
            grid.innerHTML = '';
            books.forEach(book => {
                const card = document.createElement('div');
                card.className = 'book-card';

                const img = document.createElement('img');
                img.src = book.coverImage || 'assets/img/book1.jpg';
                img.alt = book.title || 'Book';

                const h3 = document.createElement('h3');
                h3.textContent = book.title || '';

                const authors = document.createElement('p');
                authors.style.color = '#666';
                authors.style.fontSize = '0.9rem';
                authors.textContent = (book.authors && book.authors.join(', ')) || '';

                const price = document.createElement('p');
                price.style.fontWeight = 'bold';
                price.style.color = 'var(--primary)';
                price.style.margin = '0.5rem 0';
                price.textContent = '$' + (book.price != null ? book.price.toFixed(2) : '0.00');

                const controls = document.createElement('div');
                controls.style.display = 'flex';
                controls.style.gap = '0.5rem';

                const addBtn = document.createElement('button');
                addBtn.className = 'btn-submit';
                addBtn.textContent = 'Add to Cart';
                // Wire up add to cart (calls global addToCart function provided by cart.js)
                addBtn.addEventListener('click', () => {
                    if (typeof addToCart === 'function') {
                        addToCart(book.isbn);
                        // optimistic increment cart count
                        try {
                            const countEl = document.getElementById('cart-count');
                            if (countEl) countEl.textContent = String(Number(countEl.textContent||'0') + 1);
                        } catch(e) {}
                    } else {
                        alert('Cart functionality not available.');
                    }
                });

                const details = document.createElement('a');
                details.className = 'btn-submit';
                details.style.background = '#64748b';
                details.style.textAlign = 'center';
                details.style.textDecoration = 'none';
                details.href = 'book_details.html?isbn=' + encodeURIComponent(book.isbn);
                details.textContent = 'Details';

                controls.appendChild(addBtn);
                controls.appendChild(details);

                card.appendChild(img);
                card.appendChild(h3);
                card.appendChild(authors);
                card.appendChild(price);
                card.appendChild(controls);

                grid.appendChild(card);
            });
            // update cart count if user logged in
            try {
                const user = JSON.parse(localStorage.getItem('user'));
                if (user && user.username) {
                    fetch(`http://localhost:8080/api/cart/${encodeURIComponent(user.username)}`)
                        .then(r => r.json())
                        .then(items => {
                            const countEl = document.getElementById('cart-count');
                            if (countEl) countEl.textContent = items.length;
                        }).catch(()=>{});
                }
            } catch(e) {}
        })
        .catch(err => {
            console.error('Failed to load books:', err);
        });
});
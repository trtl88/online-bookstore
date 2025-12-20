import React, { useState, useEffect } from 'react';
import { bookApi, cartApi } from '../services/api';
import './Shop.css';

function Shop({ user }) {
  const [books, setBooks] = useState([]);
  const [filteredBooks, setFilteredBooks] = useState([]);
  const [categories] = useState(['All', 'Science', 'Art', 'Religion', 'History', 'Geography']);
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [cartMessage, setCartMessage] = useState('');

  useEffect(() => {
    loadBooks();
  }, []);

  useEffect(() => {
    filterBooks();
  }, [books, selectedCategory, searchTerm]);

  const loadBooks = async () => {
    try {
      const response = await bookApi.getAll();
      setBooks(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading books:', error);
      setLoading(false);
    }
  };

  const filterBooks = () => {
    let filtered = books;
    
    if (selectedCategory !== 'All') {
      filtered = filtered.filter(book => book.category === selectedCategory);
    }
    
    if (searchTerm) {
      filtered = filtered.filter(book => 
        book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        book.isbn.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    
    setFilteredBooks(filtered);
  };

  const addToCart = async (bookId) => {
    try {
      await cartApi.addItem(user.userId, bookId, 1);
      setCartMessage('Added to cart!');
      setTimeout(() => setCartMessage(''), 2000);
    } catch (error) {
      setCartMessage(error.response?.data?.error || 'Failed to add to cart');
      setTimeout(() => setCartMessage(''), 2000);
    }
  };

  const getStockStatus = (book) => {
    if (book.quantityInStock === 0) return { text: 'Out of Stock', class: 'out-of-stock' };
    if (book.quantityInStock < book.threshold) return { text: 'Low Stock', class: 'low-stock' };
    return { text: 'In Stock', class: 'in-stock' };
  };

  if (loading) {
    return <div className="loading">Loading books...</div>;
  }

  return (
    <div className="shop-container">
      <div className="shop-header">
        <h1>📚 Browse Books</h1>
        
        <div className="shop-filters">
          <input
            type="text"
            placeholder="Search by title or ISBN..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
          
          <div className="category-filters">
            {categories.map(category => (
              <button
                key={category}
                className={`category-btn ${selectedCategory === category ? 'active' : ''}`}
                onClick={() => setSelectedCategory(category)}
              >
                {category}
              </button>
            ))}
          </div>
        </div>
      </div>

      {cartMessage && (
        <div className={`cart-message ${cartMessage.includes('Failed') ? 'error' : 'success'}`}>
          {cartMessage}
        </div>
      )}

      <div className="books-grid">
        {filteredBooks.length === 0 ? (
          <p className="no-books">No books found matching your criteria.</p>
        ) : (
          filteredBooks.map(book => {
            const stockStatus = getStockStatus(book);
            return (
              <div key={book.bookId} className="book-card">
                <div className="book-category">{book.category}</div>
                <h3 className="book-title">{book.title}</h3>
                <p className="book-isbn">ISBN: {book.isbn}</p>
                {book.publicationYear && (
                  <p className="book-year">Published: {book.publicationYear}</p>
                )}
                <p className="book-price">${book.sellingPrice?.toFixed(2)}</p>
                <span className={`stock-badge ${stockStatus.class}`}>
                  {stockStatus.text}
                </span>
                <button
                  className="add-to-cart-btn"
                  onClick={() => addToCart(book.bookId)}
                  disabled={book.quantityInStock === 0}
                >
                  {book.quantityInStock === 0 ? 'Out of Stock' : 'Add to Cart'}
                </button>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}

export default Shop;

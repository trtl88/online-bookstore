import React, { useState, useEffect } from 'react';
import { bookApi, authorApi, publisherApi } from '../services/api';
import './AdminBooks.css';

function AdminBooks() {
  const [books, setBooks] = useState([]);
  const [authors, setAuthors] = useState([]);
  const [publishers, setPublishers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingBook, setEditingBook] = useState(null);
  const [formData, setFormData] = useState({
    isbn: '',
    title: '',
    publisherId: '',
    authorIds: [],
    publicationYear: '',
    sellingPrice: '',
    category: 'Science',
    quantityInStock: 0,
    threshold: 5
  });
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [booksRes, authorsRes, publishersRes] = await Promise.all([
        bookApi.getAll(),
        authorApi.getAll(),
        publisherApi.getAll()
      ]);
      setBooks(booksRes.data);
      setAuthors(authorsRes.data);
      setPublishers(publishersRes.data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading data:', error);
      setLoading(false);
    }
  };

  const openModal = (book = null) => {
    if (book) {
      setEditingBook(book);
      setFormData({
        isbn: book.isbn,
        title: book.title,
        publisherId: book.publisher?.publisherId || '',
        authorIds: book.authors?.map(a => a.authorId) || [],
        publicationYear: book.publicationYear || '',
        sellingPrice: book.sellingPrice,
        category: book.category,
        quantityInStock: book.quantityInStock,
        threshold: book.threshold
      });
    } else {
      setEditingBook(null);
      setFormData({
        isbn: '',
        title: '',
        publisherId: '',
        authorIds: [],
        publicationYear: '',
        sellingPrice: '',
        category: 'Science',
        quantityInStock: 0,
        threshold: 5
      });
    }
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setEditingBook(null);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleAuthorChange = (e) => {
    const selectedOptions = Array.from(e.target.selectedOptions, option => parseInt(option.value));
    setFormData({ ...formData, authorIds: selectedOptions });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const bookData = {
        ...formData,
        publisherId: formData.publisherId ? parseInt(formData.publisherId) : null,
        publicationYear: formData.publicationYear ? parseInt(formData.publicationYear) : null,
        sellingPrice: parseFloat(formData.sellingPrice),
        quantityInStock: parseInt(formData.quantityInStock),
        threshold: parseInt(formData.threshold)
      };

      if (editingBook) {
        await bookApi.update(editingBook.bookId, bookData);
        setMessage({ type: 'success', text: 'Book updated successfully!' });
      } else {
        await bookApi.create(bookData);
        setMessage({ type: 'success', text: 'Book added successfully!' });
      }
      closeModal();
      loadData();
    } catch (error) {
      setMessage({ type: 'error', text: error.response?.data?.error || 'Operation failed' });
    }
    setTimeout(() => setMessage({ type: '', text: '' }), 3000);
  };

  const handleDelete = async (bookId) => {
    if (window.confirm('Are you sure you want to delete this book?')) {
      try {
        await bookApi.delete(bookId);
        setMessage({ type: 'success', text: 'Book deleted successfully!' });
        loadData();
      } catch (error) {
        setMessage({ type: 'error', text: 'Failed to delete book' });
      }
      setTimeout(() => setMessage({ type: '', text: '' }), 3000);
    }
  };

  const getStockClass = (book) => {
    if (book.quantityInStock === 0) return 'out';
    if (book.quantityInStock < book.threshold) return 'low';
    return 'ok';
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="admin-books">
      <div className="page-header">
        <h1>📖 Manage Books</h1>
        <button className="add-btn" onClick={() => openModal()}>+ Add Book</button>
      </div>

      {message.text && (
        <div className={`message ${message.type}`}>{message.text}</div>
      )}

      <div className="books-table-container">
        <table className="books-table">
          <thead>
            <tr>
              <th>ISBN</th>
              <th>Title</th>
              <th>Category</th>
              <th>Price</th>
              <th>Stock</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {books.map(book => (
              <tr key={book.bookId}>
                <td>{book.isbn}</td>
                <td>{book.title}</td>
                <td><span className="category-badge">{book.category}</span></td>
                <td>${book.sellingPrice?.toFixed(2)}</td>
                <td>
                  <span className={`stock-badge ${getStockClass(book)}`}>
                    {book.quantityInStock}
                  </span>
                </td>
                <td>
                  <button className="edit-btn" onClick={() => openModal(book)}>Edit</button>
                  <button className="delete-btn" onClick={() => handleDelete(book.bookId)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>{editingBook ? 'Edit Book' : 'Add New Book'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-grid">
                <div className="form-group">
                  <label>ISBN *</label>
                  <input type="text" name="isbn" value={formData.isbn} onChange={handleChange} required />
                </div>
                
                <div className="form-group">
                  <label>Title *</label>
                  <input type="text" name="title" value={formData.title} onChange={handleChange} required />
                </div>
                
                <div className="form-group">
                  <label>Publisher</label>
                  <select name="publisherId" value={formData.publisherId} onChange={handleChange}>
                    <option value="">Select Publisher</option>
                    {publishers.map(p => (
                      <option key={p.publisherId} value={p.publisherId}>{p.name}</option>
                    ))}
                  </select>
                </div>
                
                <div className="form-group">
                  <label>Authors</label>
                  <select multiple name="authorIds" value={formData.authorIds} onChange={handleAuthorChange}>
                    {authors.map(a => (
                      <option key={a.authorId} value={a.authorId}>{a.name}</option>
                    ))}
                  </select>
                  <small>Hold Ctrl/Cmd to select multiple</small>
                </div>
                
                <div className="form-group">
                  <label>Publication Year</label>
                  <input type="number" name="publicationYear" value={formData.publicationYear} onChange={handleChange} />
                </div>
                
                <div className="form-group">
                  <label>Price *</label>
                  <input type="number" step="0.01" name="sellingPrice" value={formData.sellingPrice} onChange={handleChange} required />
                </div>
                
                <div className="form-group">
                  <label>Category *</label>
                  <select name="category" value={formData.category} onChange={handleChange} required>
                    <option value="Science">Science</option>
                    <option value="Art">Art</option>
                    <option value="Religion">Religion</option>
                    <option value="History">History</option>
                    <option value="Geography">Geography</option>
                  </select>
                </div>
                
                <div className="form-group">
                  <label>Quantity in Stock</label>
                  <input type="number" name="quantityInStock" value={formData.quantityInStock} onChange={handleChange} />
                </div>
                
                <div className="form-group">
                  <label>Threshold</label>
                  <input type="number" name="threshold" value={formData.threshold} onChange={handleChange} />
                </div>
              </div>
              
              <div className="modal-actions">
                <button type="button" className="cancel-btn" onClick={closeModal}>Cancel</button>
                <button type="submit" className="submit-btn">
                  {editingBook ? 'Update' : 'Add'} Book
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default AdminBooks;

import React, { useState, useEffect } from 'react';
import { bookOrderApi, bookApi } from '../services/api';
import './AdminBookOrders.css';

function AdminBookOrders() {
  const [orders, setOrders] = useState([]);
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    bookId: '',
    quantity: 10
  });
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [ordersRes, booksRes] = await Promise.all([
        bookOrderApi.getAll(),
        bookApi.getAll()
      ]);
      setOrders(ordersRes.data);
      setBooks(booksRes.data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading data:', error);
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await bookOrderApi.place(parseInt(formData.bookId), parseInt(formData.quantity));
      setMessage({ type: 'success', text: 'Order placed successfully!' });
      setShowModal(false);
      loadData();
    } catch (error) {
      setMessage({ type: 'error', text: error.response?.data?.error || 'Failed to place order' });
    }
    setTimeout(() => setMessage({ type: '', text: '' }), 3000);
  };

  const confirmOrder = async (orderId) => {
    try {
      await bookOrderApi.confirm(orderId);
      setMessage({ type: 'success', text: 'Order confirmed! Stock has been updated.' });
      loadData();
    } catch (error) {
      setMessage({ type: 'error', text: error.response?.data?.error || 'Failed to confirm order' });
    }
    setTimeout(() => setMessage({ type: '', text: '' }), 3000);
  };

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="admin-book-orders">
      <div className="page-header">
        <h1>📦 Publisher Orders</h1>
        <button className="add-btn" onClick={() => setShowModal(true)}>+ Place Order</button>
      </div>

      {message.text && (
        <div className={`message ${message.type}`}>{message.text}</div>
      )}

      <div className="info-box">
        <p>📌 These are orders placed to publishers to replenish book stock.</p>
        <p>When stock drops below the threshold, an automatic order is placed (via database trigger).</p>
      </div>

      <div className="orders-table-container">
        <table className="orders-table">
          <thead>
            <tr>
              <th>Order ID</th>
              <th>Book</th>
              <th>Quantity</th>
              <th>Order Date</th>
              <th>Status</th>
              <th>Confirmed Date</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {orders.length === 0 ? (
              <tr>
                <td colSpan="7" className="no-data">No orders found</td>
              </tr>
            ) : (
              orders.map(order => (
                <tr key={order.orderId}>
                  <td>#{order.orderId}</td>
                  <td>{order.book?.title || 'Unknown'}</td>
                  <td>{order.quantity}</td>
                  <td>{formatDate(order.orderDate)}</td>
                  <td>
                    <span className={`status-badge ${order.status?.toLowerCase()}`}>
                      {order.status}
                    </span>
                  </td>
                  <td>{formatDate(order.confirmedDate)}</td>
                  <td>
                    {order.status === 'PENDING' && (
                      <button 
                        className="confirm-btn"
                        onClick={() => confirmOrder(order.orderId)}
                      >
                        ✓ Confirm
                      </button>
                    )}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>Place Order from Publisher</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Select Book *</label>
                <select name="bookId" value={formData.bookId} onChange={handleChange} required>
                  <option value="">Choose a book...</option>
                  {books.map(book => (
                    <option key={book.bookId} value={book.bookId}>
                      {book.title} (Stock: {book.quantityInStock})
                    </option>
                  ))}
                </select>
              </div>
              
              <div className="form-group">
                <label>Quantity *</label>
                <input
                  type="number"
                  name="quantity"
                  value={formData.quantity}
                  onChange={handleChange}
                  min="1"
                  required
                />
              </div>
              
              <div className="modal-actions">
                <button type="button" className="cancel-btn" onClick={() => setShowModal(false)}>
                  Cancel
                </button>
                <button type="submit" className="submit-btn">
                  Place Order
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default AdminBookOrders;

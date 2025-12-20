import React, { useState, useEffect } from 'react';
import { orderApi } from '../services/api';
import './AdminCustomerOrders.css';

function AdminCustomerOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [orderDetails, setOrderDetails] = useState(null);
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    loadOrders();
  }, []);

  const loadOrders = async () => {
    try {
      const response = await orderApi.getAll();
      setOrders(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading orders:', error);
      setLoading(false);
    }
  };

  const viewOrderDetails = async (orderId) => {
    if (selectedOrder === orderId) {
      setSelectedOrder(null);
      setOrderDetails(null);
      return;
    }
    try {
      const response = await orderApi.getById(orderId);
      setOrderDetails(response.data);
      setSelectedOrder(orderId);
    } catch (error) {
      console.error('Error loading order details:', error);
    }
  };

  const updateStatus = async (orderId, status) => {
    try {
      await orderApi.updateStatus(orderId, status);
      setMessage({ type: 'success', text: 'Status updated successfully!' });
      loadOrders();
      if (selectedOrder === orderId) {
        viewOrderDetails(orderId);
      }
    } catch (error) {
      setMessage({ type: 'error', text: 'Failed to update status' });
    }
    setTimeout(() => setMessage({ type: '', text: '' }), 3000);
  };

  const getStatusBadge = (status) => {
    const statusMap = {
      PENDING: { color: '#ffc107', label: 'Pending' },
      PROCESSING: { color: '#17a2b8', label: 'Processing' },
      SHIPPED: { color: '#007bff', label: 'Shipped' },
      DELIVERED: { color: '#28a745', label: 'Delivered' }
    };
    return statusMap[status] || { color: '#6c757d', label: status };
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return <div className="loading">Loading orders...</div>;
  }

  return (
    <div className="admin-customer-orders">
      <h1>🛒 Customer Orders</h1>

      {message.text && (
        <div className={`message ${message.type}`}>{message.text}</div>
      )}

      <div className="orders-table-container">
        <table className="orders-table">
          <thead>
            <tr>
              <th>Order ID</th>
              <th>Customer</th>
              <th>Date</th>
              <th>Total</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {orders.length === 0 ? (
              <tr>
                <td colSpan="6" className="no-data">No orders found</td>
              </tr>
            ) : (
              orders.map(order => {
                const status = getStatusBadge(order.status);
                return (
                  <React.Fragment key={order.orderId}>
                    <tr>
                      <td>#{order.orderId}</td>
                      <td>{order.user?.firstName} {order.user?.lastName}</td>
                      <td>{formatDate(order.orderDate)}</td>
                      <td className="amount">${order.totalAmount?.toFixed(2)}</td>
                      <td>
                        <span className="status-badge" style={{ background: status.color }}>
                          {status.label}
                        </span>
                      </td>
                      <td>
                        <button 
                          className="view-btn"
                          onClick={() => viewOrderDetails(order.orderId)}
                        >
                          {selectedOrder === order.orderId ? 'Hide' : 'View'}
                        </button>
                        <select
                          className="status-select"
                          value={order.status}
                          onChange={(e) => updateStatus(order.orderId, e.target.value)}
                        >
                          <option value="PENDING">Pending</option>
                          <option value="PROCESSING">Processing</option>
                          <option value="SHIPPED">Shipped</option>
                          <option value="DELIVERED">Delivered</option>
                        </select>
                      </td>
                    </tr>
                    {selectedOrder === order.orderId && orderDetails && (
                      <tr className="details-row">
                        <td colSpan="6">
                          <div className="order-details">
                            <div className="details-section">
                              <h4>Order Items</h4>
                              {orderDetails.items?.map((item, index) => (
                                <div key={index} className="detail-item">
                                  <span>{item.title}</span>
                                  <span>x{item.quantity}</span>
                                  <span>${item.subtotal?.toFixed(2)}</span>
                                </div>
                              ))}
                            </div>
                            <div className="details-section">
                              <h4>Shipping Info</h4>
                              <p><strong>Address:</strong> {orderDetails.shippingAddress}</p>
                              <p><strong>Card:</strong> **** **** **** {orderDetails.creditCardLastFour}</p>
                            </div>
                          </div>
                        </td>
                      </tr>
                    )}
                  </React.Fragment>
                );
              })
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default AdminCustomerOrders;

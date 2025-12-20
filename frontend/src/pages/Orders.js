import React, { useState, useEffect } from 'react';
import { orderApi } from '../services/api';
import './Orders.css';

function Orders({ user }) {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [orderDetails, setOrderDetails] = useState(null);

  useEffect(() => {
    loadOrders();
  }, [user]);

  const loadOrders = async () => {
    try {
      const response = await orderApi.getByUser(user.userId);
      setOrders(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading orders:', error);
      setLoading(false);
    }
  };

  const viewOrderDetails = async (orderId) => {
    try {
      const response = await orderApi.getById(orderId);
      setOrderDetails(response.data);
      setSelectedOrder(orderId);
    } catch (error) {
      console.error('Error loading order details:', error);
    }
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
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return <div className="loading">Loading orders...</div>;
  }

  return (
    <div className="orders-container">
      <h1>📦 My Orders</h1>

      {orders.length === 0 ? (
        <div className="no-orders">
          <p>You haven't placed any orders yet.</p>
        </div>
      ) : (
        <div className="orders-list">
          {orders.map(order => {
            const status = getStatusBadge(order.status);
            return (
              <div key={order.orderId} className="order-card">
                <div className="order-header">
                  <div>
                    <span className="order-id">Order #{order.orderId}</span>
                    <span className="order-date">{formatDate(order.orderDate)}</span>
                  </div>
                  <span 
                    className="status-badge"
                    style={{ background: status.color }}
                  >
                    {status.label}
                  </span>
                </div>
                
                <div className="order-info">
                  <div className="info-row">
                    <span>Total Amount:</span>
                    <span className="amount">${order.totalAmount?.toFixed(2)}</span>
                  </div>
                  {order.creditCardLastFour && (
                    <div className="info-row">
                      <span>Card:</span>
                      <span>**** **** **** {order.creditCardLastFour}</span>
                    </div>
                  )}
                </div>
                
                <button 
                  className="view-details-btn"
                  onClick={() => viewOrderDetails(order.orderId)}
                >
                  {selectedOrder === order.orderId ? 'Hide Details' : 'View Details'}
                </button>
                
                {selectedOrder === order.orderId && orderDetails && (
                  <div className="order-details">
                    <h4>Items Ordered</h4>
                    {orderDetails.items?.map((item, index) => (
                      <div key={index} className="detail-item">
                        <span className="item-title">{item.title}</span>
                        <span className="item-qty">x{item.quantity}</span>
                        <span className="item-price">${item.subtotal?.toFixed(2)}</span>
                      </div>
                    ))}
                    <div className="shipping-info">
                      <strong>Shipping Address:</strong>
                      <p>{orderDetails.shippingAddress}</p>
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}

export default Orders;

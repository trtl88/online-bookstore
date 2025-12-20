import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './OrderSuccess.css';

function OrderSuccess() {
  const location = useLocation();
  const navigate = useNavigate();
  const order = location.state?.order;

  if (!order) {
    navigate('/shop');
    return null;
  }

  return (
    <div className="success-container">
      <div className="success-box">
        <div className="success-icon">✅</div>
        <h1>Order Placed Successfully!</h1>
        <p className="order-message">{order.message}</p>
        
        <div className="order-details">
          <div className="detail-row">
            <span>Order ID:</span>
            <span>#{order.orderId}</span>
          </div>
          <div className="detail-row">
            <span>Total Amount:</span>
            <span className="amount">${order.totalAmount?.toFixed(2)}</span>
          </div>
          <div className="detail-row">
            <span>Status:</span>
            <span className="status">{order.status}</span>
          </div>
        </div>
        
        <div className="success-actions">
          <button onClick={() => navigate('/orders')} className="view-orders-btn">
            View My Orders
          </button>
          <button onClick={() => navigate('/shop')} className="continue-btn">
            Continue Shopping
          </button>
        </div>
      </div>
    </div>
  );
}

export default OrderSuccess;

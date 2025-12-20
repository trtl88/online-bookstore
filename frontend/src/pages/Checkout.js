import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { cartApi, orderApi } from '../services/api';
import './Checkout.css';

function Checkout({ user }) {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [processing, setProcessing] = useState(false);
  const [formData, setFormData] = useState({
    creditCardNumber: '',
    shippingAddress: user?.shippingAddress || ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    loadCart();
  }, [user]);

  const loadCart = async () => {
    try {
      const response = await cartApi.get(user.userId);
      if (response.data.items.length === 0) {
        navigate('/cart');
        return;
      }
      setCart(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading cart:', error);
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    let value = e.target.value;
    
    // Format credit card number
    if (e.target.name === 'creditCardNumber') {
      value = value.replace(/\D/g, '').slice(0, 16);
    }
    
    setFormData({
      ...formData,
      [e.target.name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (formData.creditCardNumber.length !== 16) {
      setError('Credit card number must be 16 digits');
      return;
    }

    if (!formData.shippingAddress.trim()) {
      setError('Shipping address is required');
      return;
    }

    setProcessing(true);

    try {
      const response = await orderApi.checkout(user.userId, formData);
      navigate('/order-success', { state: { order: response.data } });
    } catch (error) {
      setError(error.response?.data?.error || 'Checkout failed. Please try again.');
      setProcessing(false);
    }
  };

  const formatCardNumber = (value) => {
    return value.replace(/(\d{4})/g, '$1 ').trim();
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="checkout-container">
      <h1>💳 Checkout</h1>

      <div className="checkout-content">
        <div className="checkout-form">
          <h2>Payment & Shipping</h2>
          
          {error && <div className="error-message">{error}</div>}
          
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Credit Card Number</label>
              <input
                type="text"
                name="creditCardNumber"
                value={formatCardNumber(formData.creditCardNumber)}
                onChange={handleChange}
                placeholder="1234 5678 9012 3456"
                required
              />
              <small>Enter 16 digits (test: use any 16 digits)</small>
            </div>
            
            <div className="form-group">
              <label>Shipping Address</label>
              <textarea
                name="shippingAddress"
                value={formData.shippingAddress}
                onChange={handleChange}
                placeholder="Enter your full shipping address"
                rows="3"
                required
              />
            </div>
            
            <button type="submit" className="pay-btn" disabled={processing}>
              {processing ? 'Processing...' : `Pay $${cart?.total?.toFixed(2)}`}
            </button>
          </form>
        </div>

        <div className="order-summary">
          <h2>Order Summary</h2>
          
          <div className="summary-items">
            {cart?.items.map(item => (
              <div key={item.cartItemId} className="summary-item">
                <span className="item-name">{item.title}</span>
                <span className="item-qty">x{item.quantity}</span>
                <span className="item-price">${item.subtotal?.toFixed(2)}</span>
              </div>
            ))}
          </div>
          
          <div className="summary-total">
            <span>Total ({cart?.itemCount} items)</span>
            <span className="total-amount">${cart?.total?.toFixed(2)}</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Checkout;

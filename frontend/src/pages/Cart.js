import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { cartApi } from '../services/api';
import './Cart.css';

function Cart({ user }) {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    loadCart();
  }, [user]);

  const loadCart = async () => {
    try {
      const response = await cartApi.get(user.userId);
      setCart(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading cart:', error);
      setLoading(false);
    }
  };

  const updateQuantity = async (bookId, quantity) => {
    try {
      await cartApi.updateItem(user.userId, bookId, quantity);
      loadCart();
    } catch (error) {
      alert(error.response?.data?.error || 'Failed to update quantity');
    }
  };

  const removeItem = async (bookId) => {
    try {
      await cartApi.removeItem(user.userId, bookId);
      loadCart();
    } catch (error) {
      alert(error.response?.data?.error || 'Failed to remove item');
    }
  };

  const clearCart = async () => {
    if (window.confirm('Are you sure you want to clear your cart?')) {
      try {
        await cartApi.clear(user.userId);
        loadCart();
      } catch (error) {
        alert(error.response?.data?.error || 'Failed to clear cart');
      }
    }
  };

  if (loading) {
    return <div className="loading">Loading cart...</div>;
  }

  return (
    <div className="cart-container">
      <h1>🛒 Shopping Cart</h1>

      {!cart || cart.items.length === 0 ? (
        <div className="empty-cart">
          <p>Your cart is empty</p>
          <button onClick={() => navigate('/shop')} className="shop-btn">
            Browse Books
          </button>
        </div>
      ) : (
        <>
          <div className="cart-items">
            {cart.items.map(item => (
              <div key={item.cartItemId} className="cart-item">
                <div className="item-info">
                  <h3>{item.title}</h3>
                  <p className="isbn">ISBN: {item.isbn}</p>
                  <p className="price">${item.price?.toFixed(2)} each</p>
                </div>
                
                <div className="item-quantity">
                  <button 
                    onClick={() => updateQuantity(item.bookId, item.quantity - 1)}
                    disabled={item.quantity <= 1}
                  >
                    -
                  </button>
                  <span>{item.quantity}</span>
                  <button onClick={() => updateQuantity(item.bookId, item.quantity + 1)}>
                    +
                  </button>
                </div>
                
                <div className="item-subtotal">
                  ${item.subtotal?.toFixed(2)}
                </div>
                
                <button 
                  className="remove-btn"
                  onClick={() => removeItem(item.bookId)}
                >
                  🗑️
                </button>
              </div>
            ))}
          </div>

          <div className="cart-summary">
            <div className="summary-row">
              <span>Total Items:</span>
              <span>{cart.itemCount}</span>
            </div>
            <div className="summary-row total">
              <span>Total:</span>
              <span>${cart.total?.toFixed(2)}</span>
            </div>
            
            <div className="cart-actions">
              <button onClick={clearCart} className="clear-btn">
                Clear Cart
              </button>
              <button onClick={() => navigate('/checkout')} className="checkout-btn">
                Proceed to Checkout
              </button>
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default Cart;

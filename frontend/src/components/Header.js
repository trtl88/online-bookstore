import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import './Header.css';

function Header() {
  const navigate = useNavigate();
  const location = useLocation();
  const user = JSON.parse(localStorage.getItem('user'));
  const isAdmin = user?.role === 'ADMIN';

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/login');
  };

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  return (
    <header className="header">
      <div className="header-container">
        <Link to="/" className="logo">
          📚 Online Bookstore
        </Link>

        <nav className="nav-links">
          {!user ? (
            <>
              <Link to="/shop" className={isActive('/shop')}>Shop</Link>
              <Link to="/login" className={isActive('/login')}>Login</Link>
              <Link to="/register" className={isActive('/register')}>Register</Link>
            </>
          ) : isAdmin ? (
            <>
              <Link to="/admin/dashboard" className={isActive('/admin/dashboard')}>Dashboard</Link>
              <Link to="/admin/books" className={isActive('/admin/books')}>Manage Books</Link>
              <Link to="/admin/book-orders" className={isActive('/admin/book-orders')}>Publisher Orders</Link>
              <Link to="/admin/customer-orders" className={isActive('/admin/customer-orders')}>Customer Orders</Link>
              <Link to="/admin/reports" className={isActive('/admin/reports')}>Reports</Link>
            </>
          ) : (
            <>
              <Link to="/shop" className={isActive('/shop')}>Shop</Link>
              <Link to="/cart" className={isActive('/cart')}>🛒 Cart</Link>
              <Link to="/orders" className={isActive('/orders')}>My Orders</Link>
            </>
          )}
        </nav>

        <div className="user-section">
          {user ? (
            <>
              <span className="user-name">
                {isAdmin ? '👨‍💼' : '👤'} {user.firstName}
              </span>
              <button onClick={handleLogout} className="logout-btn">
                Logout
              </button>
            </>
          ) : (
            <span className="guest-label">Welcome, Guest</span>
          )}
        </div>
      </div>
    </header>
  );
}

export default Header;

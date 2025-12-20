import React, { useState, useEffect } from 'react';
import { bookApi, bookOrderApi, reportApi } from '../services/api';
import './AdminDashboard.css';

function AdminDashboard() {
  const [stats, setStats] = useState({
    totalBooks: 0,
    lowStockBooks: 0,
    pendingOrders: 0,
    monthlySales: null
  });
  const [topBooks, setTopBooks] = useState([]);
  const [topCustomers, setTopCustomers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      const [booksRes, lowStockRes, pendingRes, salesRes, topBooksRes, topCustomersRes] = 
        await Promise.all([
          bookApi.getAll(),
          bookApi.getLowStock(),
          bookOrderApi.getPending(),
          reportApi.getSalesPreviousMonth(),
          reportApi.getTop10Books(),
          reportApi.getTop5Customers()
        ]);

      setStats({
        totalBooks: booksRes.data.length,
        lowStockBooks: lowStockRes.data.length,
        pendingOrders: pendingRes.data.length,
        monthlySales: salesRes.data
      });
      setTopBooks(topBooksRes.data.slice(0, 5));
      setTopCustomers(topCustomersRes.data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading dashboard:', error);
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading dashboard...</div>;
  }

  return (
    <div className="admin-dashboard">
      <h1>📊 Admin Dashboard</h1>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon">📚</div>
          <div className="stat-info">
            <span className="stat-value">{stats.totalBooks}</span>
            <span className="stat-label">Total Books</span>
          </div>
        </div>
        
        <div className="stat-card warning">
          <div className="stat-icon">⚠️</div>
          <div className="stat-info">
            <span className="stat-value">{stats.lowStockBooks}</span>
            <span className="stat-label">Low Stock</span>
          </div>
        </div>
        
        <div className="stat-card info">
          <div className="stat-icon">📦</div>
          <div className="stat-info">
            <span className="stat-value">{stats.pendingOrders}</span>
            <span className="stat-label">Pending Publisher Orders</span>
          </div>
        </div>
        
        <div className="stat-card success">
          <div className="stat-icon">💰</div>
          <div className="stat-info">
            <span className="stat-value">
              ${stats.monthlySales?.totalSalesAmount?.toFixed(2) || '0.00'}
            </span>
            <span className="stat-label">Last Month Sales</span>
          </div>
        </div>
      </div>

      <div className="dashboard-grid">
        <div className="dashboard-card">
          <h2>🏆 Top 5 Selling Books (Last 3 Months)</h2>
          {topBooks.length === 0 ? (
            <p className="no-data">No sales data available</p>
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Title</th>
                  <th>Copies Sold</th>
                </tr>
              </thead>
              <tbody>
                {topBooks.map((book, index) => (
                  <tr key={book.bookId}>
                    <td>{index + 1}</td>
                    <td>{book.title}</td>
                    <td>{book.totalCopiesSold}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        <div className="dashboard-card">
          <h2>👥 Top 5 Customers (Last 3 Months)</h2>
          {topCustomers.length === 0 ? (
            <p className="no-data">No customer data available</p>
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Customer</th>
                  <th>Total Spent</th>
                </tr>
              </thead>
              <tbody>
                {topCustomers.map((customer, index) => (
                  <tr key={customer.userId}>
                    <td>{index + 1}</td>
                    <td>{customer.customerName}</td>
                    <td>${customer.totalPurchaseAmount?.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      <div className="quick-actions">
        <h2>Quick Actions</h2>
        <div className="actions-grid">
          <a href="/admin/books" className="action-btn">
            <span>📖</span>
            Manage Books
          </a>
          <a href="/admin/book-orders" className="action-btn">
            <span>📦</span>
            Publisher Orders
          </a>
          <a href="/admin/reports" className="action-btn">
            <span>📈</span>
            View Reports
          </a>
          <a href="/admin/customer-orders" className="action-btn">
            <span>🛒</span>
            Customer Orders
          </a>
        </div>
      </div>
    </div>
  );
}

export default AdminDashboard;

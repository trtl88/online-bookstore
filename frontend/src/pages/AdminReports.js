import React, { useState, useEffect } from 'react';
import { reportApi, bookApi } from '../services/api';
import './AdminReports.css';

function AdminReports() {
  const [monthlySales, setMonthlySales] = useState(null);
  const [dailySales, setDailySales] = useState(null);
  const [topCustomers, setTopCustomers] = useState([]);
  const [topBooks, setTopBooks] = useState([]);
  const [bookOrderCount, setBookOrderCount] = useState(null);
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [books, setBooks] = useState([]);
  const [selectedBookId, setSelectedBookId] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadInitialData();
  }, []);

  const loadInitialData = async () => {
    try {
      const [monthlyRes, customersRes, booksRes, bookListRes] = await Promise.all([
        reportApi.getSalesPreviousMonth(),
        reportApi.getTop5Customers(),
        reportApi.getTop10Books(),
        bookApi.getAll()
      ]);
      setMonthlySales(monthlyRes.data);
      setTopCustomers(customersRes.data);
      setTopBooks(booksRes.data);
      setBooks(bookListRes.data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading reports:', error);
      setLoading(false);
    }
  };

  const loadDailySales = async () => {
    try {
      const response = await reportApi.getSalesForDate(selectedDate);
      setDailySales(response.data);
    } catch (error) {
      console.error('Error loading daily sales:', error);
    }
  };

  const loadBookOrderCount = async () => {
    if (!selectedBookId) return;
    try {
      const response = await reportApi.getBookOrderCount(selectedBookId);
      setBookOrderCount(response.data);
    } catch (error) {
      console.error('Error loading book order count:', error);
    }
  };

  if (loading) {
    return <div className="loading">Loading reports...</div>;
  }

  return (
    <div className="admin-reports">
      <h1>📈 Reports</h1>

      <div className="reports-grid">
        {/* Monthly Sales Report */}
        <div className="report-card">
          <h2>📅 Previous Month Sales</h2>
          {monthlySales ? (
            <div className="report-content">
              <div className="report-stat">
                <span className="stat-label">Total Orders</span>
                <span className="stat-value">{monthlySales.totalOrders}</span>
              </div>
              <div className="report-stat">
                <span className="stat-label">Total Sales</span>
                <span className="stat-value success">${monthlySales.totalSalesAmount?.toFixed(2)}</span>
              </div>
              <div className="report-stat">
                <span className="stat-label">Books Sold</span>
                <span className="stat-value">{monthlySales.totalBooksSold}</span>
              </div>
            </div>
          ) : (
            <p className="no-data">No data available</p>
          )}
        </div>

        {/* Daily Sales Report */}
        <div className="report-card">
          <h2>📆 Daily Sales</h2>
          <div className="date-picker">
            <input
              type="date"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
            />
            <button onClick={loadDailySales}>Get Report</button>
          </div>
          {dailySales && (
            <div className="report-content">
              <div className="report-stat">
                <span className="stat-label">Orders</span>
                <span className="stat-value">{dailySales.totalOrders}</span>
              </div>
              <div className="report-stat">
                <span className="stat-label">Sales</span>
                <span className="stat-value success">${dailySales.totalSalesAmount?.toFixed(2)}</span>
              </div>
              <div className="report-stat">
                <span className="stat-label">Books Sold</span>
                <span className="stat-value">{dailySales.totalBooksSold}</span>
              </div>
            </div>
          )}
        </div>

        {/* Top 5 Customers */}
        <div className="report-card full-width">
          <h2>👥 Top 5 Customers (Last 3 Months)</h2>
          {topCustomers.length === 0 ? (
            <p className="no-data">No customer data available</p>
          ) : (
            <table className="report-table">
              <thead>
                <tr>
                  <th>Rank</th>
                  <th>Customer</th>
                  <th>Email</th>
                  <th>Orders</th>
                  <th>Total Spent</th>
                </tr>
              </thead>
              <tbody>
                {topCustomers.map((customer, index) => (
                  <tr key={customer.userId}>
                    <td>#{index + 1}</td>
                    <td>{customer.customerName}</td>
                    <td>{customer.email}</td>
                    <td>{customer.totalOrders}</td>
                    <td className="success">${customer.totalPurchaseAmount?.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {/* Top 10 Books */}
        <div className="report-card full-width">
          <h2>🏆 Top 10 Selling Books (Last 3 Months)</h2>
          {topBooks.length === 0 ? (
            <p className="no-data">No sales data available</p>
          ) : (
            <table className="report-table">
              <thead>
                <tr>
                  <th>Rank</th>
                  <th>Title</th>
                  <th>Copies Sold</th>
                </tr>
              </thead>
              <tbody>
                {topBooks.map((book, index) => (
                  <tr key={book.bookId}>
                    <td>#{index + 1}</td>
                    <td>{book.title}</td>
                    <td>{book.totalCopiesSold}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {/* Book Order Count */}
        <div className="report-card">
          <h2>📦 Book Replenishment Orders</h2>
          <div className="book-picker">
            <select
              value={selectedBookId}
              onChange={(e) => setSelectedBookId(e.target.value)}
            >
              <option value="">Select a book...</option>
              {books.map(book => (
                <option key={book.bookId} value={book.bookId}>
                  {book.title}
                </option>
              ))}
            </select>
            <button onClick={loadBookOrderCount}>Get Count</button>
          </div>
          {bookOrderCount && (
            <div className="report-content">
              <div className="report-stat">
                <span className="stat-label">Times Ordered</span>
                <span className="stat-value">{bookOrderCount.timesOrdered}</span>
              </div>
              <div className="report-stat">
                <span className="stat-label">Total Quantity</span>
                <span className="stat-value">{bookOrderCount.totalQuantityOrdered}</span>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default AdminReports;

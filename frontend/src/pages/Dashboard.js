import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Spinner } from 'react-bootstrap';
import { FaUsers, FaTruck, FaBox, FaShoppingCart, FaExclamationTriangle } from 'react-icons/fa';
import { customerApi, supplierApi, productApi, orderApi, inventoryApi } from '../services/api';

function Dashboard() {
  const [stats, setStats] = useState({
    customers: 0,
    suppliers: 0,
    products: 0,
    orders: 0,
    pendingOrders: 0,
    lowStockItems: 0,
  });
  const [loading, setLoading] = useState(true);
  const [recentOrders, setRecentOrders] = useState([]);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [customers, suppliers, products, orders, lowStock] = await Promise.all([
        customerApi.getAll(),
        supplierApi.getAll(),
        productApi.getAll(),
        orderApi.getAll(),
        inventoryApi.getLowStock(),
      ]);

      const pendingOrders = orders.data.filter(o => o.status === 'PENDING').length;

      setStats({
        customers: customers.data.length,
        suppliers: suppliers.data.length,
        products: products.data.length,
        orders: orders.data.length,
        pendingOrders,
        lowStockItems: lowStock.data.length,
      });

      setRecentOrders(orders.data.slice(0, 5));
      setLoading(false);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="loading-spinner">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  return (
    <>
      <div className="page-header">
        <h1>Dashboard</h1>
        <p className="text-muted">Overview of your order processing system</p>
      </div>

      <Row className="mb-4">
        <Col md={3}>
          <Card className="dashboard-card bg-primary text-white">
            <Card.Body>
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <h6 className="mb-1">Customers</h6>
                  <h2 className="mb-0">{stats.customers}</h2>
                </div>
                <FaUsers className="dashboard-icon" />
              </div>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="dashboard-card bg-success text-white">
            <Card.Body>
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <h6 className="mb-1">Products</h6>
                  <h2 className="mb-0">{stats.products}</h2>
                </div>
                <FaBox className="dashboard-icon" />
              </div>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="dashboard-card bg-info text-white">
            <Card.Body>
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <h6 className="mb-1">Total Orders</h6>
                  <h2 className="mb-0">{stats.orders}</h2>
                </div>
                <FaShoppingCart className="dashboard-icon" />
              </div>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="dashboard-card bg-warning text-dark">
            <Card.Body>
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <h6 className="mb-1">Suppliers</h6>
                  <h2 className="mb-0">{stats.suppliers}</h2>
                </div>
                <FaTruck className="dashboard-icon" />
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="mb-4">
        <Col md={6}>
          <Card className="dashboard-card bg-danger text-white">
            <Card.Body>
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <h6 className="mb-1">Low Stock Items</h6>
                  <h2 className="mb-0">{stats.lowStockItems}</h2>
                  <small>Needs attention</small>
                </div>
                <FaExclamationTriangle className="dashboard-icon" />
              </div>
            </Card.Body>
          </Card>
        </Col>
        <Col md={6}>
          <Card className="dashboard-card bg-secondary text-white">
            <Card.Body>
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <h6 className="mb-1">Pending Orders</h6>
                  <h2 className="mb-0">{stats.pendingOrders}</h2>
                  <small>Awaiting processing</small>
                </div>
                <FaShoppingCart className="dashboard-icon" />
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col md={12}>
          <Card>
            <Card.Header>
              <h5 className="mb-0">Recent Orders</h5>
            </Card.Header>
            <Card.Body>
              {recentOrders.length === 0 ? (
                <p className="text-muted text-center">No orders yet</p>
              ) : (
                <table className="table">
                  <thead>
                    <tr>
                      <th>Order ID</th>
                      <th>Customer</th>
                      <th>Date</th>
                      <th>Status</th>
                      <th>Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    {recentOrders.map((order) => (
                      <tr key={order.orderId}>
                        <td>#{order.orderId}</td>
                        <td>{order.customerName}</td>
                        <td>{new Date(order.orderDate).toLocaleDateString()}</td>
                        <td>
                          <span className={`status-badge status-${order.status.toLowerCase()}`}>
                            {order.status}
                          </span>
                        </td>
                        <td>${order.totalAmount?.toFixed(2)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </>
  );
}

export default Dashboard;

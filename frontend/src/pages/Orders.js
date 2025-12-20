import React, { useState, useEffect } from 'react';
import { Card, Table, Button, Modal, Form, Spinner, Badge } from 'react-bootstrap';
import { FaPlus, FaEye, FaTrash, FaCheck } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { orderApi, customerApi, productApi, inventoryApi } from '../services/api';

function Orders() {
  const [orders, setOrders] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [currentOrder, setCurrentOrder] = useState(null);
  const [orderItems, setOrderItems] = useState([]);
  const [newOrder, setNewOrder] = useState({
    customerId: '',
    shippingAddress: '',
    shippingCity: '',
    shippingState: '',
    shippingZipCode: '',
    shippingCountry: 'USA',
    orderItems: [{ productId: '', quantity: 1 }],
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [ordersRes, customersRes, productsRes] = await Promise.all([
        orderApi.getAll(),
        customerApi.getAll(),
        productApi.getAll(),
      ]);
      setOrders(ordersRes.data);
      setCustomers(customersRes.data);
      setProducts(productsRes.data);
      setLoading(false);
    } catch (error) {
      toast.error('Failed to fetch data');
      setLoading(false);
    }
  };

  const handleShowModal = () => {
    setNewOrder({
      customerId: '',
      shippingAddress: '',
      shippingCity: '',
      shippingState: '',
      shippingZipCode: '',
      shippingCountry: 'USA',
      orderItems: [{ productId: '', quantity: 1 }],
    });
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleShowDetails = async (order) => {
    try {
      const itemsRes = await orderApi.getItems(order.orderId);
      setCurrentOrder(order);
      setOrderItems(itemsRes.data);
      setShowDetailsModal(true);
    } catch (error) {
      toast.error('Failed to fetch order details');
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewOrder({ ...newOrder, [name]: value });
  };

  const handleItemChange = (index, field, value) => {
    const items = [...newOrder.orderItems];
    items[index][field] = value;
    setNewOrder({ ...newOrder, orderItems: items });
  };

  const addOrderItem = () => {
    setNewOrder({
      ...newOrder,
      orderItems: [...newOrder.orderItems, { productId: '', quantity: 1 }],
    });
  };

  const removeOrderItem = (index) => {
    const items = newOrder.orderItems.filter((_, i) => i !== index);
    setNewOrder({ ...newOrder, orderItems: items });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const orderData = {
        ...newOrder,
        customerId: parseInt(newOrder.customerId),
        orderItems: newOrder.orderItems.map((item) => ({
          productId: parseInt(item.productId),
          quantity: parseInt(item.quantity),
        })),
      };
      await orderApi.create(orderData);
      toast.success('Order created successfully');
      fetchData();
      handleCloseModal();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to create order');
    }
  };

  const handleStatusChange = async (orderId, status) => {
    try {
      await orderApi.updateStatus(orderId, status);
      toast.success('Order status updated');
      fetchData();
    } catch (error) {
      toast.error('Failed to update status');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this order?')) {
      try {
        await orderApi.delete(id);
        toast.success('Order deleted successfully');
        fetchData();
      } catch (error) {
        toast.error('Failed to delete order');
      }
    }
  };

  const getStatusBadge = (status) => {
    const variants = {
      PENDING: 'warning',
      PROCESSING: 'info',
      SHIPPED: 'primary',
      DELIVERED: 'success',
      CANCELLED: 'danger',
    };
    return <Badge bg={variants[status] || 'secondary'}>{status}</Badge>;
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
      <div className="page-header d-flex justify-content-between align-items-center">
        <div>
          <h1>Orders</h1>
          <p className="text-muted">Manage customer orders</p>
        </div>
        <Button variant="primary" onClick={handleShowModal}>
          <FaPlus className="me-2" /> New Order
        </Button>
      </div>

      <Card>
        <Card.Header>
          <span>All Orders ({orders.length})</span>
        </Card.Header>
        <Card.Body>
          {orders.length === 0 ? (
            <div className="empty-state">
              <p>No orders yet</p>
            </div>
          ) : (
            <Table responsive hover>
              <thead>
                <tr>
                  <th>Order ID</th>
                  <th>Customer</th>
                  <th>Date</th>
                  <th>Status</th>
                  <th>Total</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {orders.map((order) => (
                  <tr key={order.orderId}>
                    <td>#{order.orderId}</td>
                    <td>
                      {order.customerName}
                      <br />
                      <small className="text-muted">{order.customerEmail}</small>
                    </td>
                    <td>{new Date(order.orderDate).toLocaleDateString()}</td>
                    <td>{getStatusBadge(order.status)}</td>
                    <td><strong>${order.totalAmount?.toFixed(2)}</strong></td>
                    <td>
                      <Button
                        variant="outline-info"
                        size="sm"
                        className="btn-action"
                        onClick={() => handleShowDetails(order)}
                      >
                        <FaEye />
                      </Button>
                      {order.status === 'PENDING' && (
                        <Button
                          variant="outline-success"
                          size="sm"
                          className="btn-action"
                          onClick={() => handleStatusChange(order.orderId, 'PROCESSING')}
                          title="Mark as Processing"
                        >
                          <FaCheck />
                        </Button>
                      )}
                      <Button
                        variant="outline-danger"
                        size="sm"
                        className="btn-action"
                        onClick={() => handleDelete(order.orderId)}
                      >
                        <FaTrash />
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
        </Card.Body>
      </Card>

      {/* New Order Modal */}
      <Modal show={showModal} onHide={handleCloseModal} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Create New Order</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Customer *</Form.Label>
              <Form.Select
                name="customerId"
                value={newOrder.customerId}
                onChange={handleInputChange}
                required
              >
                <option value="">Select Customer</option>
                {customers.map((customer) => (
                  <option key={customer.customerId} value={customer.customerId}>
                    {customer.firstName} {customer.lastName} - {customer.email}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>

            <hr />
            <h6>Shipping Address</h6>
            <Form.Group className="mb-3">
              <Form.Label>Address</Form.Label>
              <Form.Control
                type="text"
                name="shippingAddress"
                value={newOrder.shippingAddress}
                onChange={handleInputChange}
              />
            </Form.Group>
            <div className="row">
              <div className="col-md-4">
                <Form.Group className="mb-3">
                  <Form.Label>City</Form.Label>
                  <Form.Control
                    type="text"
                    name="shippingCity"
                    value={newOrder.shippingCity}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </div>
              <div className="col-md-4">
                <Form.Group className="mb-3">
                  <Form.Label>State</Form.Label>
                  <Form.Control
                    type="text"
                    name="shippingState"
                    value={newOrder.shippingState}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </div>
              <div className="col-md-4">
                <Form.Group className="mb-3">
                  <Form.Label>Zip Code</Form.Label>
                  <Form.Control
                    type="text"
                    name="shippingZipCode"
                    value={newOrder.shippingZipCode}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </div>
            </div>

            <hr />
            <div className="d-flex justify-content-between align-items-center mb-3">
              <h6 className="mb-0">Order Items</h6>
              <Button variant="outline-primary" size="sm" onClick={addOrderItem}>
                <FaPlus /> Add Item
              </Button>
            </div>

            {newOrder.orderItems.map((item, index) => (
              <div className="row mb-2" key={index}>
                <div className="col-md-7">
                  <Form.Select
                    value={item.productId}
                    onChange={(e) => handleItemChange(index, 'productId', e.target.value)}
                    required
                  >
                    <option value="">Select Product</option>
                    {products.map((product) => (
                      <option key={product.productId} value={product.productId}>
                        {product.productName} - ${parseFloat(product.unitPrice).toFixed(2)}
                      </option>
                    ))}
                  </Form.Select>
                </div>
                <div className="col-md-3">
                  <Form.Control
                    type="number"
                    min="1"
                    placeholder="Qty"
                    value={item.quantity}
                    onChange={(e) => handleItemChange(index, 'quantity', e.target.value)}
                    required
                  />
                </div>
                <div className="col-md-2">
                  {newOrder.orderItems.length > 1 && (
                    <Button
                      variant="outline-danger"
                      onClick={() => removeOrderItem(index)}
                    >
                      <FaTrash />
                    </Button>
                  )}
                </div>
              </div>
            ))}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Cancel
            </Button>
            <Button variant="primary" type="submit">
              Create Order
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>

      {/* Order Details Modal */}
      <Modal show={showDetailsModal} onHide={() => setShowDetailsModal(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Order #{currentOrder?.orderId} Details</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {currentOrder && (
            <>
              <div className="row mb-4">
                <div className="col-md-6">
                  <h6>Customer Information</h6>
                  <p className="mb-1"><strong>{currentOrder.customerName}</strong></p>
                  <p className="mb-1">{currentOrder.customerEmail}</p>
                </div>
                <div className="col-md-6">
                  <h6>Shipping Address</h6>
                  <p className="mb-1">{currentOrder.shippingAddress}</p>
                  <p className="mb-1">
                    {currentOrder.shippingCity}, {currentOrder.shippingState} {currentOrder.shippingZipCode}
                  </p>
                </div>
              </div>

              <div className="row mb-4">
                <div className="col-md-6">
                  <h6>Order Date</h6>
                  <p>{new Date(currentOrder.orderDate).toLocaleString()}</p>
                </div>
                <div className="col-md-6">
                  <h6>Status</h6>
                  <Form.Select
                    value={currentOrder.status}
                    onChange={(e) => {
                      handleStatusChange(currentOrder.orderId, e.target.value);
                      setCurrentOrder({ ...currentOrder, status: e.target.value });
                    }}
                  >
                    <option value="PENDING">Pending</option>
                    <option value="PROCESSING">Processing</option>
                    <option value="SHIPPED">Shipped</option>
                    <option value="DELIVERED">Delivered</option>
                    <option value="CANCELLED">Cancelled</option>
                  </Form.Select>
                </div>
              </div>

              <h6>Order Items</h6>
              <Table striped bordered>
                <thead>
                  <tr>
                    <th>Product</th>
                    <th>Unit Price</th>
                    <th>Quantity</th>
                    <th>Subtotal</th>
                  </tr>
                </thead>
                <tbody>
                  {orderItems.map((item) => (
                    <tr key={item.orderItemId}>
                      <td>{item.productName}</td>
                      <td>${parseFloat(item.unitPrice).toFixed(2)}</td>
                      <td>{item.quantity}</td>
                      <td>${item.subtotal?.toFixed(2)}</td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr>
                    <td colSpan="3" className="text-end"><strong>Total:</strong></td>
                    <td><strong>${currentOrder.totalAmount?.toFixed(2)}</strong></td>
                  </tr>
                </tfoot>
              </Table>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDetailsModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default Orders;

import React, { useState, useEffect } from 'react';
import { Card, Table, Button, Modal, Form, Spinner, Badge } from 'react-bootstrap';
import { FaSync, FaExclamationTriangle } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { inventoryApi, productApi } from '../services/api';

function Inventory() {
  const [inventory, setInventory] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showRestockModal, setShowRestockModal] = useState(false);
  const [currentItem, setCurrentItem] = useState(null);
  const [restockQuantity, setRestockQuantity] = useState(0);
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [inventoryRes, productsRes] = await Promise.all([
        inventoryApi.getAll(),
        productApi.getAll(),
      ]);
      setInventory(inventoryRes.data);
      setProducts(productsRes.data);
      setLoading(false);
    } catch (error) {
      toast.error('Failed to fetch data');
      setLoading(false);
    }
  };

  const handleFilter = async (filterType) => {
    setFilter(filterType);
    try {
      let response;
      switch (filterType) {
        case 'low':
          response = await inventoryApi.getLowStock();
          break;
        case 'out':
          response = await inventoryApi.getOutOfStock();
          break;
        default:
          response = await inventoryApi.getAll();
      }
      setInventory(response.data);
    } catch (error) {
      toast.error('Failed to filter inventory');
    }
  };

  const handleShowRestock = (item) => {
    setCurrentItem(item);
    setRestockQuantity(0);
    setShowRestockModal(true);
  };

  const handleRestock = async () => {
    if (restockQuantity <= 0) {
      toast.error('Please enter a valid quantity');
      return;
    }
    try {
      await inventoryApi.restock(currentItem.productId, restockQuantity);
      toast.success('Product restocked successfully');
      fetchData();
      setShowRestockModal(false);
    } catch (error) {
      toast.error('Failed to restock product');
    }
  };

  const handleUpdateReorderLevel = async (item, newLevel) => {
    try {
      await inventoryApi.update(item.inventoryId, {
        quantity: item.quantityInStock,
        reorderLevel: parseInt(newLevel),
      });
      toast.success('Reorder level updated');
      fetchData();
    } catch (error) {
      toast.error('Failed to update reorder level');
    }
  };

  const getStockStatus = (item) => {
    if (item.quantityInStock === 0) {
      return <Badge bg="danger">Out of Stock</Badge>;
    } else if (item.quantityInStock <= item.reorderLevel) {
      return <Badge bg="warning" text="dark">Low Stock</Badge>;
    }
    return <Badge bg="success">In Stock</Badge>;
  };

  // Map inventory to products for display
  const inventoryWithProducts = inventory.map((inv) => {
    const product = products.find((p) => p.productId === inv.productId);
    return {
      ...inv,
      productName: product?.productName || inv.productName,
      category: product?.category,
      unitPrice: product?.unitPrice,
    };
  });

  if (loading) {
    return (
      <div className="loading-spinner">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  const lowStockCount = inventory.filter(
    (i) => i.quantityInStock > 0 && i.quantityInStock <= i.reorderLevel
  ).length;
  const outOfStockCount = inventory.filter((i) => i.quantityInStock === 0).length;

  return (
    <>
      <div className="page-header d-flex justify-content-between align-items-center">
        <div>
          <h1>Inventory</h1>
          <p className="text-muted">Monitor and manage stock levels</p>
        </div>
        <Button variant="outline-primary" onClick={fetchData}>
          <FaSync className="me-2" /> Refresh
        </Button>
      </div>

      {/* Summary Cards */}
      <div className="row mb-4">
        <div className="col-md-4">
          <Card
            className={`cursor-pointer ${filter === 'all' ? 'border-primary' : ''}`}
            onClick={() => handleFilter('all')}
            style={{ cursor: 'pointer' }}
          >
            <Card.Body className="text-center">
              <h3>{inventory.length}</h3>
              <p className="mb-0 text-muted">Total Items</p>
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-4">
          <Card
            className={`cursor-pointer ${filter === 'low' ? 'border-warning' : ''}`}
            onClick={() => handleFilter('low')}
            style={{ cursor: 'pointer' }}
          >
            <Card.Body className="text-center">
              <h3 className="text-warning">{lowStockCount}</h3>
              <p className="mb-0 text-muted">Low Stock</p>
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-4">
          <Card
            className={`cursor-pointer ${filter === 'out' ? 'border-danger' : ''}`}
            onClick={() => handleFilter('out')}
            style={{ cursor: 'pointer' }}
          >
            <Card.Body className="text-center">
              <h3 className="text-danger">{outOfStockCount}</h3>
              <p className="mb-0 text-muted">Out of Stock</p>
            </Card.Body>
          </Card>
        </div>
      </div>

      <Card>
        <Card.Header className="d-flex justify-content-between align-items-center">
          <span>Inventory Status</span>
          {(lowStockCount > 0 || outOfStockCount > 0) && (
            <span className="text-warning">
              <FaExclamationTriangle className="me-1" />
              {lowStockCount + outOfStockCount} items need attention
            </span>
          )}
        </Card.Header>
        <Card.Body>
          {inventoryWithProducts.length === 0 ? (
            <div className="empty-state">
              <p>No inventory items found</p>
            </div>
          ) : (
            <Table responsive hover>
              <thead>
                <tr>
                  <th>Product</th>
                  <th>Category</th>
                  <th>In Stock</th>
                  <th>Reorder Level</th>
                  <th>Status</th>
                  <th>Last Restock</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {inventoryWithProducts.map((item) => (
                  <tr key={item.inventoryId}>
                    <td>
                      <strong>{item.productName}</strong>
                    </td>
                    <td>{item.category || '-'}</td>
                    <td>
                      <strong
                        className={
                          item.quantityInStock === 0
                            ? 'text-danger'
                            : item.quantityInStock <= item.reorderLevel
                            ? 'text-warning'
                            : 'text-success'
                        }
                      >
                        {item.quantityInStock}
                      </strong>
                    </td>
                    <td>
                      <Form.Control
                        type="number"
                        size="sm"
                        style={{ width: '80px' }}
                        defaultValue={item.reorderLevel}
                        onBlur={(e) => {
                          if (parseInt(e.target.value) !== item.reorderLevel) {
                            handleUpdateReorderLevel(item, e.target.value);
                          }
                        }}
                      />
                    </td>
                    <td>{getStockStatus(item)}</td>
                    <td>
                      {item.lastRestockDate
                        ? new Date(item.lastRestockDate).toLocaleDateString()
                        : '-'}
                    </td>
                    <td>
                      <Button
                        variant="outline-success"
                        size="sm"
                        onClick={() => handleShowRestock(item)}
                      >
                        <FaSync className="me-1" /> Restock
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
        </Card.Body>
      </Card>

      {/* Restock Modal */}
      <Modal show={showRestockModal} onHide={() => setShowRestockModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Restock Product</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {currentItem && (
            <>
              <p>
                <strong>Product:</strong> {currentItem.productName}
              </p>
              <p>
                <strong>Current Stock:</strong> {currentItem.quantityInStock}
              </p>
              <Form.Group className="mb-3">
                <Form.Label>Quantity to Add</Form.Label>
                <Form.Control
                  type="number"
                  min="1"
                  value={restockQuantity}
                  onChange={(e) => setRestockQuantity(parseInt(e.target.value) || 0)}
                />
              </Form.Group>
              <p className="text-muted">
                New Stock Level: {currentItem.quantityInStock + restockQuantity}
              </p>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowRestockModal(false)}>
            Cancel
          </Button>
          <Button variant="success" onClick={handleRestock}>
            Confirm Restock
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default Inventory;

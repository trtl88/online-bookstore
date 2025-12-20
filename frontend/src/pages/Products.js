import React, { useState, useEffect } from 'react';
import { Card, Table, Button, Modal, Form, Spinner, InputGroup, FormControl } from 'react-bootstrap';
import { FaPlus, FaEdit, FaTrash, FaSearch } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { productApi, supplierApi } from '../services/api';

function Products() {
  const [products, setProducts] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [currentProduct, setCurrentProduct] = useState({
    productName: '',
    description: '',
    category: '',
    unitPrice: '',
    supplierId: '',
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [productsRes, suppliersRes] = await Promise.all([
        productApi.getAll(),
        supplierApi.getAll(),
      ]);
      setProducts(productsRes.data);
      setSuppliers(suppliersRes.data);
      setLoading(false);
    } catch (error) {
      toast.error('Failed to fetch data');
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      fetchData();
      return;
    }
    try {
      const response = await productApi.search(searchTerm);
      setProducts(response.data);
    } catch (error) {
      toast.error('Search failed');
    }
  };

  const handleShowModal = (product = null) => {
    if (product) {
      setCurrentProduct({
        ...product,
        unitPrice: product.unitPrice,
        supplierId: product.supplierId || '',
      });
      setEditMode(true);
    } else {
      setCurrentProduct({
        productName: '',
        description: '',
        category: '',
        unitPrice: '',
        supplierId: '',
      });
      setEditMode(false);
    }
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditMode(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCurrentProduct({ ...currentProduct, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const productData = {
        ...currentProduct,
        unitPrice: parseFloat(currentProduct.unitPrice),
        supplierId: currentProduct.supplierId ? parseInt(currentProduct.supplierId) : null,
      };

      if (editMode) {
        await productApi.update(currentProduct.productId, productData);
        toast.success('Product updated successfully');
      } else {
        await productApi.create(productData);
        toast.success('Product created successfully');
      }
      fetchData();
      handleCloseModal();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Operation failed');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await productApi.delete(id);
        toast.success('Product deleted successfully');
        fetchData();
      } catch (error) {
        toast.error('Failed to delete product');
      }
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
      <div className="page-header d-flex justify-content-between align-items-center">
        <div>
          <h1>Products</h1>
          <p className="text-muted">Manage your product catalog</p>
        </div>
        <Button variant="primary" onClick={() => handleShowModal()}>
          <FaPlus className="me-2" /> Add Product
        </Button>
      </div>

      <Card>
        <Card.Header className="d-flex justify-content-between align-items-center">
          <span>All Products ({products.length})</span>
          <InputGroup className="search-box">
            <FormControl
              placeholder="Search products..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            />
            <Button variant="outline-primary" onClick={handleSearch}>
              <FaSearch />
            </Button>
          </InputGroup>
        </Card.Header>
        <Card.Body>
          {products.length === 0 ? (
            <div className="empty-state">
              <FaSearch />
              <p>No products found</p>
            </div>
          ) : (
            <Table responsive hover>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Category</th>
                  <th>Price</th>
                  <th>Supplier</th>
                  <th>Stock</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {products.map((product) => (
                  <tr key={product.productId}>
                    <td>{product.productId}</td>
                    <td>
                      <strong>{product.productName}</strong>
                      <br />
                      <small className="text-muted">{product.description?.substring(0, 50)}</small>
                    </td>
                    <td>{product.category}</td>
                    <td>${parseFloat(product.unitPrice).toFixed(2)}</td>
                    <td>{product.supplierName || '-'}</td>
                    <td>
                      {product.inventory ? (
                        <span className={`stock-status stock-${product.inventory.stockStatus === 'IN_STOCK' ? 'in' : product.inventory.stockStatus === 'LOW_STOCK' ? 'low' : 'out'}`}>
                          {product.inventory.quantityInStock}
                        </span>
                      ) : (
                        <span className="text-muted">N/A</span>
                      )}
                    </td>
                    <td>
                      <Button
                        variant="outline-primary"
                        size="sm"
                        className="btn-action"
                        onClick={() => handleShowModal(product)}
                      >
                        <FaEdit />
                      </Button>
                      <Button
                        variant="outline-danger"
                        size="sm"
                        className="btn-action"
                        onClick={() => handleDelete(product.productId)}
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

      <Modal show={showModal} onHide={handleCloseModal} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>{editMode ? 'Edit Product' : 'Add New Product'}</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Product Name *</Form.Label>
              <Form.Control
                type="text"
                name="productName"
                value={currentProduct.productName}
                onChange={handleInputChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="description"
                value={currentProduct.description}
                onChange={handleInputChange}
              />
            </Form.Group>
            <div className="row">
              <div className="col-md-4">
                <Form.Group className="mb-3">
                  <Form.Label>Category</Form.Label>
                  <Form.Control
                    type="text"
                    name="category"
                    value={currentProduct.category}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </div>
              <div className="col-md-4">
                <Form.Group className="mb-3">
                  <Form.Label>Unit Price *</Form.Label>
                  <Form.Control
                    type="number"
                    step="0.01"
                    min="0"
                    name="unitPrice"
                    value={currentProduct.unitPrice}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              </div>
              <div className="col-md-4">
                <Form.Group className="mb-3">
                  <Form.Label>Supplier</Form.Label>
                  <Form.Select
                    name="supplierId"
                    value={currentProduct.supplierId}
                    onChange={handleInputChange}
                  >
                    <option value="">Select Supplier</option>
                    {suppliers.map((supplier) => (
                      <option key={supplier.supplierId} value={supplier.supplierId}>
                        {supplier.supplierName}
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </div>
            </div>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Cancel
            </Button>
            <Button variant="primary" type="submit">
              {editMode ? 'Update' : 'Create'}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </>
  );
}

export default Products;

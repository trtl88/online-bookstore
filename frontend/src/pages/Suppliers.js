import React, { useState, useEffect } from 'react';
import { Card, Table, Button, Modal, Form, Spinner, InputGroup, FormControl } from 'react-bootstrap';
import { FaPlus, FaEdit, FaTrash, FaSearch } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { supplierApi } from '../services/api';

function Suppliers() {
  const [suppliers, setSuppliers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [currentSupplier, setCurrentSupplier] = useState({
    supplierName: '',
    contactName: '',
    email: '',
    phone: '',
    address: '',
    city: '',
    state: '',
    zipCode: '',
    country: 'USA',
  });

  useEffect(() => {
    fetchSuppliers();
  }, []);

  const fetchSuppliers = async () => {
    try {
      const response = await supplierApi.getAll();
      setSuppliers(response.data);
      setLoading(false);
    } catch (error) {
      toast.error('Failed to fetch suppliers');
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      fetchSuppliers();
      return;
    }
    try {
      const response = await supplierApi.search(searchTerm);
      setSuppliers(response.data);
    } catch (error) {
      toast.error('Search failed');
    }
  };

  const handleShowModal = (supplier = null) => {
    if (supplier) {
      setCurrentSupplier(supplier);
      setEditMode(true);
    } else {
      setCurrentSupplier({
        supplierName: '',
        contactName: '',
        email: '',
        phone: '',
        address: '',
        city: '',
        state: '',
        zipCode: '',
        country: 'USA',
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
    setCurrentSupplier({ ...currentSupplier, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editMode) {
        await supplierApi.update(currentSupplier.supplierId, currentSupplier);
        toast.success('Supplier updated successfully');
      } else {
        await supplierApi.create(currentSupplier);
        toast.success('Supplier created successfully');
      }
      fetchSuppliers();
      handleCloseModal();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Operation failed');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this supplier?')) {
      try {
        await supplierApi.delete(id);
        toast.success('Supplier deleted successfully');
        fetchSuppliers();
      } catch (error) {
        toast.error('Failed to delete supplier. It may have associated products.');
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
          <h1>Suppliers</h1>
          <p className="text-muted">Manage your supplier relationships</p>
        </div>
        <Button variant="primary" onClick={() => handleShowModal()}>
          <FaPlus className="me-2" /> Add Supplier
        </Button>
      </div>

      <Card>
        <Card.Header className="d-flex justify-content-between align-items-center">
          <span>All Suppliers ({suppliers.length})</span>
          <InputGroup className="search-box">
            <FormControl
              placeholder="Search suppliers..."
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
          {suppliers.length === 0 ? (
            <div className="empty-state">
              <FaSearch />
              <p>No suppliers found</p>
            </div>
          ) : (
            <Table responsive hover>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Company Name</th>
                  <th>Contact</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>City</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {suppliers.map((supplier) => (
                  <tr key={supplier.supplierId}>
                    <td>{supplier.supplierId}</td>
                    <td>{supplier.supplierName}</td>
                    <td>{supplier.contactName}</td>
                    <td>{supplier.email}</td>
                    <td>{supplier.phone}</td>
                    <td>{supplier.city}</td>
                    <td>
                      <Button
                        variant="outline-primary"
                        size="sm"
                        className="btn-action"
                        onClick={() => handleShowModal(supplier)}
                      >
                        <FaEdit />
                      </Button>
                      <Button
                        variant="outline-danger"
                        size="sm"
                        className="btn-action"
                        onClick={() => handleDelete(supplier.supplierId)}
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
          <Modal.Title>{editMode ? 'Edit Supplier' : 'Add New Supplier'}</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <div className="row">
              <div className="col-md-6">
                <Form.Group className="mb-3">
                  <Form.Label>Company Name *</Form.Label>
                  <Form.Control
                    type="text"
                    name="supplierName"
                    value={currentSupplier.supplierName}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              </div>
              <div className="col-md-6">
                <Form.Group className="mb-3">
                  <Form.Label>Contact Name</Form.Label>
                  <Form.Control
                    type="text"
                    name="contactName"
                    value={currentSupplier.contactName}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6">
                <Form.Group className="mb-3">
                  <Form.Label>Email *</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={currentSupplier.email}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              </div>
              <div className="col-md-6">
                <Form.Group className="mb-3">
                  <Form.Label>Phone</Form.Label>
                  <Form.Control
                    type="text"
                    name="phone"
                    value={currentSupplier.phone}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </div>
            </div>
            <Form.Group className="mb-3">
              <Form.Label>Address</Form.Label>
              <Form.Control
                type="text"
                name="address"
                value={currentSupplier.address}
                onChange={handleInputChange}
              />
            </Form.Group>
            <div className="row">
              <div className="col-md-4">
                <Form.Group className="mb-3">
                  <Form.Label>City</Form.Label>
                  <Form.Control
                    type="text"
                    name="city"
                    value={currentSupplier.city}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </div>
              <div className="col-md-4">
                <Form.Group className="mb-3">
                  <Form.Label>State</Form.Label>
                  <Form.Control
                    type="text"
                    name="state"
                    value={currentSupplier.state}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </div>
              <div className="col-md-4">
                <Form.Group className="mb-3">
                  <Form.Label>Zip Code</Form.Label>
                  <Form.Control
                    type="text"
                    name="zipCode"
                    value={currentSupplier.zipCode}
                    onChange={handleInputChange}
                  />
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

export default Suppliers;

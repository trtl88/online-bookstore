import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { Container, Row, Col } from 'react-bootstrap';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import Customers from './pages/Customers';
import Suppliers from './pages/Suppliers';
import Products from './pages/Products';
import Orders from './pages/Orders';
import Inventory from './pages/Inventory';

function App() {
  return (
    <>
      <Container fluid>
        <Row>
          <Col md={2} className="sidebar p-0">
            <Sidebar />
          </Col>
          <Col md={10} className="main-content">
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/customers" element={<Customers />} />
              <Route path="/suppliers" element={<Suppliers />} />
              <Route path="/products" element={<Products />} />
              <Route path="/orders" element={<Orders />} />
              <Route path="/inventory" element={<Inventory />} />
            </Routes>
          </Col>
        </Row>
      </Container>
      <ToastContainer position="top-right" autoClose={3000} />
    </>
  );
}

export default App;

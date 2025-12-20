import React from 'react';
import { Nav } from 'react-bootstrap';
import { NavLink } from 'react-router-dom';
import { 
  FaHome, 
  FaUsers, 
  FaTruck, 
  FaBox, 
  FaShoppingCart, 
  FaWarehouse,
  FaCubes
} from 'react-icons/fa';

function Sidebar() {
  return (
    <div className="d-flex flex-column h-100">
      <div className="text-center py-4 text-white">
        <FaCubes className="logo-icon" size={30} />
        <span className="navbar-brand mb-0">OPS</span>
        <p className="small mb-0 mt-1" style={{ opacity: 0.7 }}>Order Processing System</p>
      </div>
      
      <hr className="mx-3" style={{ borderColor: '#495057' }} />
      
      <Nav className="flex-column">
        <Nav.Link as={NavLink} to="/" className="nav-link">
          <FaHome /> Dashboard
        </Nav.Link>
        <Nav.Link as={NavLink} to="/customers" className="nav-link">
          <FaUsers /> Customers
        </Nav.Link>
        <Nav.Link as={NavLink} to="/suppliers" className="nav-link">
          <FaTruck /> Suppliers
        </Nav.Link>
        <Nav.Link as={NavLink} to="/products" className="nav-link">
          <FaBox /> Products
        </Nav.Link>
        <Nav.Link as={NavLink} to="/orders" className="nav-link">
          <FaShoppingCart /> Orders
        </Nav.Link>
        <Nav.Link as={NavLink} to="/inventory" className="nav-link">
          <FaWarehouse /> Inventory
        </Nav.Link>
      </Nav>
    </div>
  );
}

export default Sidebar;

import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Customer API
export const customerApi = {
  getAll: () => api.get('/customers'),
  getById: (id) => api.get(`/customers/${id}`),
  search: (keyword) => api.get(`/customers/search?keyword=${keyword}`),
  create: (customer) => api.post('/customers', customer),
  update: (id, customer) => api.put(`/customers/${id}`, customer),
  delete: (id) => api.delete(`/customers/${id}`),
};

// Supplier API
export const supplierApi = {
  getAll: () => api.get('/suppliers'),
  getById: (id) => api.get(`/suppliers/${id}`),
  search: (keyword) => api.get(`/suppliers/search?keyword=${keyword}`),
  create: (supplier) => api.post('/suppliers', supplier),
  update: (id, supplier) => api.put(`/suppliers/${id}`, supplier),
  delete: (id) => api.delete(`/suppliers/${id}`),
};

// Product API
export const productApi = {
  getAll: () => api.get('/products'),
  getById: (id) => api.get(`/products/${id}`),
  search: (keyword) => api.get(`/products/search?keyword=${keyword}`),
  getByCategory: (category) => api.get(`/products/category/${category}`),
  getCategories: () => api.get('/products/categories'),
  create: (product) => api.post('/products', product),
  update: (id, product) => api.put(`/products/${id}`, product),
  delete: (id) => api.delete(`/products/${id}`),
};

// Inventory API
export const inventoryApi = {
  getAll: () => api.get('/inventory'),
  getById: (id) => api.get(`/inventory/${id}`),
  getByProductId: (productId) => api.get(`/inventory/product/${productId}`),
  getLowStock: () => api.get('/inventory/low-stock'),
  getOutOfStock: () => api.get('/inventory/out-of-stock'),
  create: (inventory) => api.post('/inventory', inventory),
  update: (id, inventory) => api.put(`/inventory/${id}`, inventory),
  restock: (productId, quantity) => api.post(`/inventory/restock/${productId}?quantity=${quantity}`),
};

// Order API
export const orderApi = {
  getAll: () => api.get('/orders'),
  getById: (id) => api.get(`/orders/${id}`),
  getByCustomerId: (customerId) => api.get(`/orders/customer/${customerId}`),
  getByStatus: (status) => api.get(`/orders/status/${status}`),
  getItems: (orderId) => api.get(`/orders/${orderId}/items`),
  create: (order) => api.post('/orders', order),
  updateStatus: (id, status) => api.put(`/orders/${id}/status?status=${status}`),
  delete: (id) => api.delete(`/orders/${id}`),
};

export default api;

import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Auth API
export const authApi = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
  logout: (userId) => api.post(`/auth/logout/${userId}`),
};

// User API
export const userApi = {
  getAll: () => api.get('/users'),
  getById: (id) => api.get(`/users/${id}`),
  getCustomers: () => api.get('/users/customers'),
  update: (id, user) => api.put(`/users/${id}`, user),
};

// Book API
export const bookApi = {
  getAll: () => api.get('/books'),
  getById: (id) => api.get(`/books/${id}`),
  getByIsbn: (isbn) => api.get(`/books/isbn/${isbn}`),
  getByCategory: (category) => api.get(`/books/category/${category}`),
  search: (title) => api.get(`/books/search?title=${title}`),
  getLowStock: () => api.get('/books/low-stock'),
  getOutOfStock: () => api.get('/books/out-of-stock'),
  create: (book) => api.post('/books', book),
  update: (id, book) => api.put(`/books/${id}`, book),
  updateStock: (id, quantity) => api.patch(`/books/${id}/stock`, { quantity }),
  delete: (id) => api.delete(`/books/${id}`),
};

// Author API
export const authorApi = {
  getAll: () => api.get('/authors'),
  getById: (id) => api.get(`/authors/${id}`),
  create: (author) => api.post('/authors', author),
  delete: (id) => api.delete(`/authors/${id}`),
};

// Publisher API
export const publisherApi = {
  getAll: () => api.get('/publishers'),
  getById: (id) => api.get(`/publishers/${id}`),
  create: (publisher) => api.post('/publishers', publisher),
  update: (id, publisher) => api.put(`/publishers/${id}`, publisher),
  delete: (id) => api.delete(`/publishers/${id}`),
};

// Book Order API (orders from publishers)
export const bookOrderApi = {
  getAll: () => api.get('/book-orders'),
  getById: (id) => api.get(`/book-orders/${id}`),
  getPending: () => api.get('/book-orders/pending'),
  getByBook: (bookId) => api.get(`/book-orders/book/${bookId}`),
  place: (bookId, quantity) => api.post('/book-orders', { bookId, quantity }),
  confirm: (id) => api.post(`/book-orders/${id}/confirm`),
  getOrderCount: (bookId) => api.get(`/book-orders/book/${bookId}/count`),
};

// Shopping Cart API
export const cartApi = {
  get: (userId) => api.get(`/cart/${userId}`),
  addItem: (userId, bookId, quantity) => api.post(`/cart/${userId}/add`, { bookId, quantity }),
  updateItem: (userId, bookId, quantity) => api.put(`/cart/${userId}/update`, { bookId, quantity }),
  removeItem: (userId, bookId) => api.delete(`/cart/${userId}/remove/${bookId}`),
  clear: (userId) => api.delete(`/cart/${userId}/clear`),
};

// Customer Order API (sales to customers)
export const orderApi = {
  getAll: () => api.get('/orders'),
  getById: (id) => api.get(`/orders/${id}`),
  getByUser: (userId) => api.get(`/orders/user/${userId}`),
  checkout: (userId, checkoutData) => api.post(`/orders/checkout/${userId}`, checkoutData),
  updateStatus: (id, status) => api.patch(`/orders/${id}/status`, { status }),
};

// Reports API
export const reportApi = {
  getSalesPreviousMonth: () => api.get('/reports/sales/previous-month'),
  getSalesForDate: (date) => api.get(`/reports/sales/date?date=${date}`),
  getTop5Customers: () => api.get('/reports/top-customers'),
  getTop10Books: () => api.get('/reports/top-books'),
  getBookOrderCount: (bookId) => api.get(`/reports/book-orders/${bookId}`),
};

export default api;

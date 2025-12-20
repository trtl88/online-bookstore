# 📚 Online Bookstore System

A full-stack web application for an online bookstore with customer shopping features and admin management capabilities.

## 🌟 Features

### Customer Features
- **Browse Books**: View all available books with category filtering (Science, Art, Religion, History, Geography)
- **Shopping Cart**: Add books to cart, update quantities, remove items
- **Checkout**: Secure checkout with credit card validation (16 digits)
- **Order History**: View past orders and their status

### Admin Features
- **Dashboard**: Overview with statistics, top books, and top customers
- **Manage Books**: Add, edit, delete books with ISBN, title, authors, publisher, price, stock
- **Publisher Orders**: Place orders to publishers when stock is low, confirm received orders
- **Customer Orders**: View and manage customer orders, update order status
- **Reports**:
  - Monthly sales report (previous month)
  - Daily sales report (by date)
  - Top 5 customers by purchases
  - Top 10 selling books
  - Book order count by ISBN

### Database Features
- **Automatic Stock Management**: Trigger prevents negative stock quantities
- **Auto-Reorder**: Trigger automatically creates publisher orders when stock falls below threshold

## 🛠️ Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA** with Hibernate
- **Lombok** for boilerplate reduction
- **MySQL** database

### Frontend
- **React 18**
- **React Router** for navigation
- **Axios** for API calls
- **CSS3** with responsive design

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **Node.js 16+** and npm
- **MySQL 8.0+** (via XAMPP or standalone)

## 🚀 Getting Started

### 1. Database Setup

1. Start MySQL (via XAMPP or MySQL service)
2. Open MySQL console or phpMyAdmin
3. Run the database schema:
```sql
-- Execute the contents of database/bookstore_schema.sql
SOURCE C:/path/to/order-processing-system/database/bookstore_schema.sql;
```

This creates:
- `online_bookstore` database
- All required tables with proper relationships
- Triggers for stock management
- Stored procedures for reports
- Sample data for testing

### 2. Backend Setup

```bash
cd backend

# Install dependencies and build
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend runs at: `http://localhost:8080`

### 3. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

The frontend runs at: `http://localhost:3000`

## 👥 Demo Accounts

After running the database schema, these accounts are available:

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Customer | john | pass123 |
| Customer | jane | pass456 |

## 📁 Project Structure

```
order-processing-system/
├── backend/
│   └── src/main/java/com/order/
│       ├── controller/     # REST API controllers
│       ├── dto/            # Data Transfer Objects
│       ├── model/          # JPA entities
│       ├── repository/     # Spring Data repositories
│       └── service/        # Business logic
├── frontend/
│   └── src/
│       ├── components/     # Reusable components (Header)
│       ├── pages/          # Page components
│       └── services/       # API service layer
└── database/
    └── bookstore_schema.sql  # Complete database schema
```

## 🔌 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/logout/{userId}` - User logout (clears cart)

### Books
- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get book by ID
- `GET /api/books/category/{category}` - Get books by category
- `POST /api/books` - Create book (Admin)
- `PUT /api/books/{id}` - Update book (Admin)
- `DELETE /api/books/{id}` - Delete book (Admin)

### Shopping Cart
- `GET /api/cart/{userId}` - Get user's cart
- `POST /api/cart/{userId}/items` - Add item to cart
- `PUT /api/cart/{userId}/items/{itemId}` - Update cart item
- `DELETE /api/cart/{userId}/items/{itemId}` - Remove cart item

### Orders
- `GET /api/orders` - Get all orders (Admin)
- `GET /api/orders/user/{userId}` - Get user's orders
- `POST /api/orders/checkout` - Checkout cart
- `PUT /api/orders/{id}/status` - Update order status (Admin)

### Publisher Orders
- `GET /api/book-orders` - Get all publisher orders
- `POST /api/book-orders` - Create publisher order
- `PUT /api/book-orders/{id}/confirm` - Confirm order received

### Reports
- `GET /api/reports/monthly-sales` - Monthly sales report
- `GET /api/reports/daily-sales?date=YYYY-MM-DD` - Daily sales
- `GET /api/reports/top-customers` - Top 5 customers
- `GET /api/reports/top-books` - Top 10 selling books
- `GET /api/reports/book-order-count/{isbn}` - Order count by ISBN

## 📝 Book Categories

- Science
- Art
- Religion
- History
- Geography

## � Docker Deployment

### Quick Start with Docker

Run the entire application with a single command:

```bash
# Using pre-built images from DockerHub
docker-compose -f docker-compose.hub.yml up

# Or build locally
docker-compose up --build
```

### Docker Images

Pre-built images available on DockerHub:
- `trtl88/bookstore-backend:latest`
- `trtl88/bookstore-frontend:latest`

### Docker Services

| Service | Port | Description |
|---------|------|-------------|
| Frontend | 3000 | React app served via Nginx |
| Backend | 8080 | Spring Boot REST API |
| MySQL | 3307 | Database (mapped to avoid conflicts) |

### Docker Commands

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove data volumes
docker-compose down -v

# Rebuild images
docker-compose up --build
```

### Docker Files

| File | Purpose |
|------|---------|
| `docker-compose.yml` | Build locally from source |
| `docker-compose.hub.yml` | Use pre-built DockerHub images |
| `backend/Dockerfile` | Multi-stage build for Spring Boot |
| `frontend/Dockerfile` | Multi-stage build for React + Nginx |

## �🔒 Security Notes

- Passwords should be hashed in production (currently plain text for demo)
- Credit card validation is client-side only (integrate payment gateway for production)
- Add JWT authentication for production use

## 📄 License

This project is for educational purposes.

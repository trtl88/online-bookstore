# Order Processing System

A full-stack web application for managing orders, customers, products, suppliers, and inventory.

## 🚀 Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Backend framework
- **Spring Data JPA** - Database ORM
- **MySQL 8.0+** - Database
- **Maven** - Build tool
- **Lombok** - Reduce boilerplate code

### Frontend
- **React 18** - Frontend framework
- **React Router 6** - Client-side routing
- **Bootstrap 5** - CSS framework
- **React-Bootstrap** - Bootstrap components for React
- **Axios** - HTTP client
- **React-Toastify** - Toast notifications
- **React-Icons** - Icon library

## 📁 Project Structure

```
order-processing-system/
├── database/           # SQL scripts
│   ├── schema.sql      # Database schema with sample data
│   └── README.md       # Database setup instructions
├── backend/            # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/orderprocessing/
│   │   │   │   ├── config/         # Configuration classes
│   │   │   │   ├── controller/     # REST controllers
│   │   │   │   ├── dto/            # Data transfer objects
│   │   │   │   ├── exception/      # Exception handlers
│   │   │   │   ├── model/          # JPA entities
│   │   │   │   ├── repository/     # Spring Data repositories
│   │   │   │   ├── service/        # Business logic
│   │   │   │   └── OrderProcessingApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
├── frontend/           # React frontend
│   ├── public/
│   ├── src/
│   │   ├── components/     # Reusable components
│   │   ├── pages/          # Page components
│   │   ├── services/       # API services
│   │   ├── App.js
│   │   ├── index.js
│   │   └── index.css
│   └── package.json
└── README.md
```

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

1. **Java JDK 17+**
   - Download: https://adoptium.net/
   - Verify: `java -version`

2. **Maven 3.8+**
   - Download: https://maven.apache.org/download.cgi
   - Verify: `mvn -version`

3. **Node.js 18+ & npm**
   - Download: https://nodejs.org/
   - Verify: `node -v` and `npm -v`

4. **MySQL Server 8.0+**
   - Download: https://dev.mysql.com/downloads/mysql/
   - Verify: `mysql --version`

## 🔧 Installation & Setup

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd order-processing-system
```

### Step 2: Set Up the Database

1. Open MySQL command line or MySQL Workbench
2. Run the schema file:

```bash
# Using MySQL command line
mysql -u root -p < database/schema.sql
```

Or in MySQL Workbench:
- Go to File > Run SQL Script
- Select `database/schema.sql`
- Click Run

### Step 3: Configure Backend

1. Open `backend/src/main/resources/application.properties`
2. Update the database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/order_processing_system
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

### Step 4: Start the Backend

```bash
cd backend
mvn spring-boot:run
```

The backend will start on: **http://localhost:8080/api**

### Step 5: Install Frontend Dependencies

```bash
cd frontend
npm install
```

### Step 6: Start the Frontend

```bash
npm start
```

The frontend will start on: **http://localhost:3000**

## 📚 API Endpoints

### Customers
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers` | Get all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| GET | `/api/customers/search?keyword=` | Search customers |
| POST | `/api/customers` | Create customer |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |

### Suppliers
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/suppliers` | Get all suppliers |
| GET | `/api/suppliers/{id}` | Get supplier by ID |
| GET | `/api/suppliers/search?keyword=` | Search suppliers |
| POST | `/api/suppliers` | Create supplier |
| PUT | `/api/suppliers/{id}` | Update supplier |
| DELETE | `/api/suppliers/{id}` | Delete supplier |

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/search?keyword=` | Search products |
| GET | `/api/products/category/{category}` | Get by category |
| GET | `/api/products/categories` | Get all categories |
| POST | `/api/products` | Create product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |

### Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders/customer/{customerId}` | Get orders by customer |
| GET | `/api/orders/status/{status}` | Get orders by status |
| GET | `/api/orders/{id}/items` | Get order items |
| POST | `/api/orders` | Create order |
| PUT | `/api/orders/{id}/status?status=` | Update order status |
| DELETE | `/api/orders/{id}` | Delete order |

### Inventory
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/inventory` | Get all inventory |
| GET | `/api/inventory/{id}` | Get inventory by ID |
| GET | `/api/inventory/product/{productId}` | Get by product |
| GET | `/api/inventory/low-stock` | Get low stock items |
| GET | `/api/inventory/out-of-stock` | Get out of stock |
| POST | `/api/inventory/restock/{productId}?quantity=` | Restock product |
| PUT | `/api/inventory/{id}` | Update inventory |

## 🎯 Features

### Dashboard
- Overview of system statistics
- Total customers, products, orders, suppliers
- Low stock alerts
- Pending orders count
- Recent orders list

### Customer Management
- Add, edit, delete customers
- Search functionality
- View customer details

### Supplier Management
- Add, edit, delete suppliers
- Search functionality
- View supplier details

### Product Management
- Add, edit, delete products
- Assign suppliers to products
- View stock levels
- Search and filter products

### Order Management
- Create new orders with multiple items
- View order details and items
- Update order status (Pending → Processing → Shipped → Delivered)
- Cancel orders (restores inventory)
- Automatic inventory deduction

### Inventory Management
- View stock levels for all products
- Filter by stock status (All, Low Stock, Out of Stock)
- Restock products
- Update reorder levels
- Stock status indicators

## 📦 Dependencies

### Backend (Maven Dependencies)

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- MySQL Connector -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

### Frontend (npm Dependencies)

```json
{
  "dependencies": {
    "axios": "^1.6.2",
    "bootstrap": "^5.3.2",
    "react": "^18.2.0",
    "react-bootstrap": "^2.9.1",
    "react-dom": "^18.2.0",
    "react-icons": "^4.12.0",
    "react-router-dom": "^6.21.0",
    "react-toastify": "^9.1.3"
  }
}
```

## 🛠️ Troubleshooting

### Backend Issues

**Port 8080 already in use:**
```bash
# Kill process on port 8080 (Windows)
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Database connection error:**
- Ensure MySQL is running
- Check credentials in application.properties
- Verify database exists

**Maven build fails:**
```bash
mvn clean install -DskipTests
```

### Frontend Issues

**npm install fails:**
```bash
# Clear npm cache
npm cache clean --force
rm -rf node_modules
npm install
```

**API connection error:**
- Ensure backend is running on port 8080
- Check CORS configuration
- Verify API URL in services/api.js

## 🔒 Environment Variables

For production, use environment variables:

**Backend:**
```bash
export DB_URL=jdbc:mysql://localhost:3306/order_processing_system
export DB_USERNAME=root
export DB_PASSWORD=yourpassword
```

**Frontend:**
```bash
export REACT_APP_API_URL=http://localhost:8080/api
```

## 📝 License

This project is created for educational purposes as part of a Database course project.

## 👥 Contributors

- Mohamed

## 📞 Support

For issues and questions, please create an issue in the repository.
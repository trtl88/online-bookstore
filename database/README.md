# Database Setup Instructions

## Prerequisites
- MySQL Server 8.0 or higher
- MySQL Workbench (optional, for GUI management)

## Setup Steps

### 1. Install MySQL Server
- Download from: https://dev.mysql.com/downloads/mysql/
- Follow installation wizard
- Set root password during installation

### 2. Create Database
Option A - Using MySQL Command Line:
```bash
mysql -u root -p
```
Then run:
```sql
source schema.sql
```

Option B - Using MySQL Workbench:
1. Open MySQL Workbench
2. Connect to your MySQL server
3. Go to File > Run SQL Script
4. Select `schema.sql`
5. Click Run

### 3. Verify Database Creation
```sql
USE order_processing_system;
SHOW TABLES;
```

You should see:
- customers
- suppliers
- products
- inventory
- orders
- order_items

### 4. Configure Backend Connection
Update the database credentials in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/order_processing_system
spring.datasource.username=root
spring.datasource.password=your_password_here
```

## Database Structure

### Tables:
1. **customers** - Customer information
2. **suppliers** - Supplier details
3. **products** - Product catalog
4. **inventory** - Stock levels
5. **orders** - Order headers
6. **order_items** - Order line items

### Views:
1. **customer_order_summary** - Customer spending analytics
2. **product_inventory_status** - Inventory status overview
3. **order_details_view** - Complete order information

### Stored Procedures:
1. **place_order** - Create new order
2. **add_order_item** - Add items to order
3. **update_order_status** - Change order status
4. **restock_product** - Update inventory quantities

## Sample Data
The schema includes sample data for testing:
- 3 suppliers
- 10 products
- 5 customers
- 5 orders with items

## Troubleshooting

### Connection Issues
- Ensure MySQL server is running
- Check firewall settings
- Verify port 3306 is open

### Authentication Error
- Update user password in application.properties
- Grant privileges: `GRANT ALL PRIVILEGES ON order_processing_system.* TO 'root'@'localhost';`

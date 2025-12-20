-- Order Processing System Database Schema
-- Drop existing database if exists
DROP DATABASE IF EXISTS order_processing_system;
CREATE DATABASE order_processing_system;
USE order_processing_system;

-- Table: Customers
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(50),
    state VARCHAR(50),
    zip_code VARCHAR(10),
    country VARCHAR(50) DEFAULT 'USA',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: Suppliers
CREATE TABLE suppliers (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_name VARCHAR(100) NOT NULL,
    contact_name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(50),
    state VARCHAR(50),
    zip_code VARCHAR(10),
    country VARCHAR(50) DEFAULT 'USA',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: Products
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    unit_price DECIMAL(10, 2) NOT NULL,
    supplier_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE SET NULL
);

-- Table: Inventory
CREATE TABLE inventory (
    inventory_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    quantity_in_stock INT NOT NULL DEFAULT 0,
    reorder_level INT DEFAULT 10,
    last_restock_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_product (product_id)
);

-- Table: Orders
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    shipping_address VARCHAR(255),
    shipping_city VARCHAR(50),
    shipping_state VARCHAR(50),
    shipping_zip_code VARCHAR(10),
    shipping_country VARCHAR(50) DEFAULT 'USA',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- Table: Order Items
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) AS (quantity * unit_price) STORED,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT
);

-- Create indexes for better query performance
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_supplier ON products(supplier_id);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_date ON orders(order_date);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);

-- Insert sample data for testing

-- Sample Suppliers
INSERT INTO suppliers (supplier_name, contact_name, email, phone, address, city, state, zip_code, country) VALUES
('Tech Supplies Inc', 'John Smith', 'john@techsupplies.com', '555-0101', '123 Tech St', 'San Francisco', 'CA', '94102', 'USA'),
('Global Electronics', 'Jane Doe', 'jane@globalelectronics.com', '555-0102', '456 Electronics Ave', 'New York', 'NY', '10001', 'USA'),
('Quality Parts Co', 'Bob Johnson', 'bob@qualityparts.com', '555-0103', '789 Parts Blvd', 'Chicago', 'IL', '60601', 'USA');

-- Sample Products
INSERT INTO products (product_name, description, category, unit_price, supplier_id) VALUES
('Laptop Pro', 'High-performance laptop with 16GB RAM', 'Electronics', 1299.99, 1),
('Wireless Mouse', 'Ergonomic wireless mouse with USB receiver', 'Accessories', 29.99, 2),
('USB-C Cable', 'High-speed USB-C charging cable 6ft', 'Accessories', 15.99, 2),
('Monitor 27"', '4K Ultra HD 27-inch display', 'Electronics', 399.99, 1),
('Keyboard Mechanical', 'RGB backlit mechanical keyboard', 'Accessories', 89.99, 3),
('Webcam HD', '1080p HD webcam with built-in microphone', 'Electronics', 79.99, 2),
('External SSD', '1TB portable external SSD', 'Storage', 149.99, 1),
('Desk Lamp LED', 'Adjustable LED desk lamp', 'Office', 45.99, 3),
('Office Chair', 'Ergonomic office chair with lumbar support', 'Furniture', 299.99, 3),
('Standing Desk', 'Electric height-adjustable standing desk', 'Furniture', 599.99, 3);

-- Sample Inventory
INSERT INTO inventory (product_id, quantity_in_stock, reorder_level, last_restock_date) VALUES
(1, 50, 10, '2024-12-01'),
(2, 150, 20, '2024-12-05'),
(3, 200, 30, '2024-12-10'),
(4, 30, 5, '2024-12-01'),
(5, 75, 15, '2024-12-08'),
(6, 60, 10, '2024-12-12'),
(7, 40, 10, '2024-12-15'),
(8, 100, 15, '2024-12-01'),
(9, 25, 5, '2024-11-28'),
(10, 15, 3, '2024-11-25');

-- Sample Customers
INSERT INTO customers (first_name, last_name, email, phone, address, city, state, zip_code, country) VALUES
('Alice', 'Johnson', 'alice.johnson@email.com', '555-1001', '123 Main St', 'Los Angeles', 'CA', '90001', 'USA'),
('Bob', 'Smith', 'bob.smith@email.com', '555-1002', '456 Oak Ave', 'Houston', 'TX', '77001', 'USA'),
('Carol', 'Davis', 'carol.davis@email.com', '555-1003', '789 Pine Rd', 'Miami', 'FL', '33101', 'USA'),
('David', 'Wilson', 'david.wilson@email.com', '555-1004', '321 Elm St', 'Seattle', 'WA', '98101', 'USA'),
('Emma', 'Brown', 'emma.brown@email.com', '555-1005', '654 Maple Dr', 'Boston', 'MA', '02101', 'USA');

-- Sample Orders
INSERT INTO orders (customer_id, order_date, status, total_amount, shipping_address, shipping_city, shipping_state, shipping_zip_code) VALUES
(1, '2024-12-15 10:30:00', 'DELIVERED', 1329.98, '123 Main St', 'Los Angeles', 'CA', '90001'),
(2, '2024-12-16 14:20:00', 'SHIPPED', 489.98, '456 Oak Ave', 'Houston', 'TX', '77001'),
(3, '2024-12-17 09:15:00', 'PROCESSING', 75.98, '789 Pine Rd', 'Miami', 'FL', '33101'),
(4, '2024-12-18 16:45:00', 'PENDING', 899.98, '321 Elm St', 'Seattle', 'WA', '98101'),
(5, '2024-12-19 11:00:00', 'PENDING', 179.97, '654 Maple Dr', 'Boston', 'MA', '02101');

-- Sample Order Items
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
-- Order 1
(1, 1, 1, 1299.99),
(1, 2, 1, 29.99),
-- Order 2
(2, 4, 1, 399.99),
(2, 5, 1, 89.99),
-- Order 3
(3, 3, 2, 15.99),
(3, 8, 1, 45.99),
-- Order 4
(4, 9, 1, 299.99),
(4, 10, 1, 599.99),
-- Order 5
(5, 6, 1, 79.99),
(5, 7, 1, 149.99);

-- Useful Views

-- View: Customer Order Summary
CREATE VIEW customer_order_summary AS
SELECT 
    c.customer_id,
    c.first_name,
    c.last_name,
    c.email,
    COUNT(o.order_id) AS total_orders,
    COALESCE(SUM(o.total_amount), 0) AS total_spent
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.first_name, c.last_name, c.email;

-- View: Product Inventory Status
CREATE VIEW product_inventory_status AS
SELECT 
    p.product_id,
    p.product_name,
    p.category,
    p.unit_price,
    i.quantity_in_stock,
    i.reorder_level,
    CASE 
        WHEN i.quantity_in_stock <= i.reorder_level THEN 'LOW_STOCK'
        WHEN i.quantity_in_stock = 0 THEN 'OUT_OF_STOCK'
        ELSE 'IN_STOCK'
    END AS stock_status
FROM products p
LEFT JOIN inventory i ON p.product_id = i.product_id;

-- View: Order Details
CREATE VIEW order_details_view AS
SELECT 
    o.order_id,
    o.order_date,
    o.status,
    c.customer_id,
    CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
    c.email AS customer_email,
    oi.product_id,
    p.product_name,
    oi.quantity,
    oi.unit_price,
    oi.subtotal,
    o.total_amount AS order_total
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN products p ON oi.product_id = p.product_id;

-- Stored Procedures

-- Procedure: Place a new order
DELIMITER //
CREATE PROCEDURE place_order(
    IN p_customer_id INT,
    IN p_shipping_address VARCHAR(255),
    IN p_shipping_city VARCHAR(50),
    IN p_shipping_state VARCHAR(50),
    IN p_shipping_zip_code VARCHAR(10)
)
BEGIN
    INSERT INTO orders (customer_id, shipping_address, shipping_city, shipping_state, shipping_zip_code, total_amount)
    VALUES (p_customer_id, p_shipping_address, p_shipping_city, p_shipping_state, p_shipping_zip_code, 0.00);
    
    SELECT LAST_INSERT_ID() AS order_id;
END //
DELIMITER ;

-- Procedure: Add item to order
DELIMITER //
CREATE PROCEDURE add_order_item(
    IN p_order_id INT,
    IN p_product_id INT,
    IN p_quantity INT
)
BEGIN
    DECLARE v_unit_price DECIMAL(10, 2);
    DECLARE v_stock INT;
    
    -- Get product price and check stock
    SELECT unit_price INTO v_unit_price FROM products WHERE product_id = p_product_id;
    SELECT quantity_in_stock INTO v_stock FROM inventory WHERE product_id = p_product_id;
    
    IF v_stock >= p_quantity THEN
        -- Add order item
        INSERT INTO order_items (order_id, product_id, quantity, unit_price)
        VALUES (p_order_id, p_product_id, p_quantity, v_unit_price);
        
        -- Update inventory
        UPDATE inventory SET quantity_in_stock = quantity_in_stock - p_quantity
        WHERE product_id = p_product_id;
        
        -- Update order total
        UPDATE orders SET total_amount = (
            SELECT SUM(subtotal) FROM order_items WHERE order_id = p_order_id
        ) WHERE order_id = p_order_id;
        
        SELECT 'SUCCESS' AS status;
    ELSE
        SELECT 'INSUFFICIENT_STOCK' AS status;
    END IF;
END //
DELIMITER ;

-- Procedure: Update order status
DELIMITER //
CREATE PROCEDURE update_order_status(
    IN p_order_id INT,
    IN p_status VARCHAR(20)
)
BEGIN
    UPDATE orders SET status = p_status WHERE order_id = p_order_id;
    SELECT 'Order status updated' AS message;
END //
DELIMITER ;

-- Procedure: Restock product
DELIMITER //
CREATE PROCEDURE restock_product(
    IN p_product_id INT,
    IN p_quantity INT
)
BEGIN
    UPDATE inventory 
    SET quantity_in_stock = quantity_in_stock + p_quantity,
        last_restock_date = CURDATE()
    WHERE product_id = p_product_id;
    
    SELECT 'Product restocked successfully' AS message;
END //
DELIMITER ;

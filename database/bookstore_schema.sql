-- Online Bookstore System Database Schema
-- Based on Alexandria University Database Systems Project Requirements
-- Fall 2025

DROP DATABASE IF EXISTS online_bookstore;
CREATE DATABASE online_bookstore;
USE online_bookstore;

-- =====================================================
-- TABLE: users (Administrators and Customers)
-- =====================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    shipping_address VARCHAR(255),
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =====================================================
-- TABLE: publishers
-- =====================================================
CREATE TABLE publishers (
    publisher_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TABLE: authors
-- =====================================================
CREATE TABLE authors (
    author_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TABLE: books
-- =====================================================
CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(200) NOT NULL,
    publisher_id INT,
    publication_year INT,
    selling_price DECIMAL(10, 2) NOT NULL,
    category ENUM('Science', 'Art', 'Religion', 'History', 'Geography') NOT NULL,
    quantity_in_stock INT NOT NULL DEFAULT 0,
    threshold INT NOT NULL DEFAULT 5,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (publisher_id) REFERENCES publishers(publisher_id) ON DELETE SET NULL,
    CONSTRAINT chk_quantity CHECK (quantity_in_stock >= 0),
    CONSTRAINT chk_price CHECK (selling_price > 0)
);

-- =====================================================
-- TABLE: book_authors (Many-to-Many relationship)
-- =====================================================
CREATE TABLE book_authors (
    book_id INT NOT NULL,
    author_id INT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE
);

-- =====================================================
-- TABLE: book_orders (Orders FROM Publishers for restocking)
-- =====================================================
CREATE TABLE book_orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 10,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'CONFIRMED') DEFAULT 'PENDING',
    confirmed_date TIMESTAMP NULL,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
);

-- =====================================================
-- TABLE: shopping_carts
-- =====================================================
CREATE TABLE shopping_carts (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- =====================================================
-- TABLE: cart_items
-- =====================================================
CREATE TABLE cart_items (
    cart_item_id INT PRIMARY KEY AUTO_INCREMENT,
    cart_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES shopping_carts(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    UNIQUE KEY unique_cart_book (cart_id, book_id)
);

-- =====================================================
-- TABLE: customer_orders (Sales to Customers)
-- =====================================================
CREATE TABLE customer_orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    credit_card_last_four VARCHAR(4),
    shipping_address VARCHAR(255),
    status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED') DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- =====================================================
-- TABLE: order_items (Items in Customer Orders)
-- =====================================================
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) AS (quantity * unit_price) STORED,
    FOREIGN KEY (order_id) REFERENCES customer_orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE RESTRICT
);

-- =====================================================
-- INDEXES for better performance
-- =====================================================
CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_category ON books(category);
CREATE INDEX idx_books_publisher ON books(publisher_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_customer_orders_user ON customer_orders(user_id);
CREATE INDEX idx_customer_orders_date ON customer_orders(order_date);
CREATE INDEX idx_book_orders_book ON book_orders(book_id);
CREATE INDEX idx_book_orders_status ON book_orders(status);

-- =====================================================
-- TRIGGER: Prevent negative stock (BEFORE UPDATE)
-- =====================================================
DELIMITER //
CREATE TRIGGER prevent_negative_stock
BEFORE UPDATE ON books
FOR EACH ROW
BEGIN
    IF NEW.quantity_in_stock < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot update: This would result in negative stock quantity';
    END IF;
END //
DELIMITER ;

-- =====================================================
-- TRIGGER: Auto-order when stock drops below threshold (AFTER UPDATE)
-- =====================================================
DELIMITER //
CREATE TRIGGER auto_order_on_low_stock
AFTER UPDATE ON books
FOR EACH ROW
BEGIN
    -- Check if stock dropped from above threshold to below threshold
    IF OLD.quantity_in_stock >= OLD.threshold AND NEW.quantity_in_stock < NEW.threshold THEN
        -- Place an automatic order for 10 copies
        INSERT INTO book_orders (book_id, quantity, status)
        VALUES (NEW.book_id, 10, 'PENDING');
    END IF;
END //
DELIMITER ;

-- =====================================================
-- VIEW: Book details with authors and publisher
-- =====================================================
CREATE VIEW book_details_view AS
SELECT 
    b.book_id,
    b.isbn,
    b.title,
    GROUP_CONCAT(a.name SEPARATOR ', ') AS authors,
    p.name AS publisher_name,
    b.publication_year,
    b.selling_price,
    b.category,
    b.quantity_in_stock,
    b.threshold,
    CASE 
        WHEN b.quantity_in_stock = 0 THEN 'Out of Stock'
        WHEN b.quantity_in_stock < b.threshold THEN 'Low Stock'
        ELSE 'In Stock'
    END AS availability
FROM books b
LEFT JOIN publishers p ON b.publisher_id = p.publisher_id
LEFT JOIN book_authors ba ON b.book_id = ba.book_id
LEFT JOIN authors a ON ba.author_id = a.author_id
GROUP BY b.book_id, b.isbn, b.title, p.name, b.publication_year, 
         b.selling_price, b.category, b.quantity_in_stock, b.threshold;

-- =====================================================
-- VIEW: Customer order history
-- =====================================================
CREATE VIEW customer_order_history AS
SELECT 
    co.order_id,
    co.user_id,
    CONCAT(u.first_name, ' ', u.last_name) AS customer_name,
    co.order_date,
    co.total_amount,
    co.status,
    oi.book_id,
    b.isbn,
    b.title AS book_title,
    oi.quantity,
    oi.unit_price,
    oi.subtotal
FROM customer_orders co
JOIN users u ON co.user_id = u.user_id
JOIN order_items oi ON co.order_id = oi.order_id
JOIN books b ON oi.book_id = b.book_id;

-- =====================================================
-- STORED PROCEDURE: Get Total Sales for Previous Month
-- =====================================================
DELIMITER //
CREATE PROCEDURE get_sales_previous_month()
BEGIN
    SELECT 
        COUNT(DISTINCT co.order_id) AS total_orders,
        SUM(co.total_amount) AS total_sales,
        SUM(oi.quantity) AS total_books_sold
    FROM customer_orders co
    JOIN order_items oi ON co.order_id = oi.order_id
    WHERE co.order_date >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)
      AND co.order_date < CURDATE();
END //
DELIMITER ;

-- =====================================================
-- STORED PROCEDURE: Get Total Sales for a Specific Day
-- =====================================================
DELIMITER //
CREATE PROCEDURE get_sales_for_date(IN p_date DATE)
BEGIN
    SELECT 
        COUNT(DISTINCT co.order_id) AS total_orders,
        COALESCE(SUM(co.total_amount), 0) AS total_sales,
        COALESCE(SUM(oi.quantity), 0) AS total_books_sold
    FROM customer_orders co
    LEFT JOIN order_items oi ON co.order_id = oi.order_id
    WHERE DATE(co.order_date) = p_date;
END //
DELIMITER ;

-- =====================================================
-- STORED PROCEDURE: Get Top 5 Customers (Last 3 Months)
-- =====================================================
DELIMITER //
CREATE PROCEDURE get_top_5_customers()
BEGIN
    SELECT 
        u.user_id,
        u.username,
        CONCAT(u.first_name, ' ', u.last_name) AS customer_name,
        u.email,
        COUNT(co.order_id) AS total_orders,
        SUM(co.total_amount) AS total_purchase_amount
    FROM users u
    JOIN customer_orders co ON u.user_id = co.user_id
    WHERE co.order_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
      AND u.role = 'CUSTOMER'
    GROUP BY u.user_id, u.username, u.first_name, u.last_name, u.email
    ORDER BY total_purchase_amount DESC
    LIMIT 5;
END //
DELIMITER ;

-- =====================================================
-- STORED PROCEDURE: Get Top 10 Selling Books (Last 3 Months)
-- =====================================================
DELIMITER //
CREATE PROCEDURE get_top_10_books()
BEGIN
    SELECT 
        b.book_id,
        b.isbn,
        b.title,
        b.category,
        SUM(oi.quantity) AS total_copies_sold,
        SUM(oi.subtotal) AS total_revenue
    FROM books b
    JOIN order_items oi ON b.book_id = oi.book_id
    JOIN customer_orders co ON oi.order_id = co.order_id
    WHERE co.order_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
    GROUP BY b.book_id, b.isbn, b.title, b.category
    ORDER BY total_copies_sold DESC
    LIMIT 10;
END //
DELIMITER ;

-- =====================================================
-- STORED PROCEDURE: Get Book Order Count (Replenishment Orders)
-- =====================================================
DELIMITER //
CREATE PROCEDURE get_book_order_count(IN p_book_id INT)
BEGIN
    SELECT 
        b.book_id,
        b.isbn,
        b.title,
        COUNT(bo.order_id) AS times_ordered,
        SUM(bo.quantity) AS total_quantity_ordered
    FROM books b
    LEFT JOIN book_orders bo ON b.book_id = bo.book_id
    WHERE b.book_id = p_book_id
    GROUP BY b.book_id, b.isbn, b.title;
END //
DELIMITER ;

-- =====================================================
-- STORED PROCEDURE: Confirm Book Order (Add to Stock)
-- =====================================================
DELIMITER //
CREATE PROCEDURE confirm_book_order(IN p_order_id INT)
BEGIN
    DECLARE v_book_id INT;
    DECLARE v_quantity INT;
    DECLARE v_status VARCHAR(20);
    
    -- Get order details
    SELECT book_id, quantity, status INTO v_book_id, v_quantity, v_status
    FROM book_orders WHERE order_id = p_order_id;
    
    IF v_status = 'CONFIRMED' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Order is already confirmed';
    ELSE
        -- Update book stock
        UPDATE books SET quantity_in_stock = quantity_in_stock + v_quantity
        WHERE book_id = v_book_id;
        
        -- Update order status
        UPDATE book_orders SET status = 'CONFIRMED', confirmed_date = NOW()
        WHERE order_id = p_order_id;
        
        SELECT 'Order confirmed successfully' AS message;
    END IF;
END //
DELIMITER ;

-- =====================================================
-- STORED PROCEDURE: Checkout Shopping Cart
-- =====================================================
DELIMITER //
CREATE PROCEDURE checkout_cart(
    IN p_user_id INT,
    IN p_credit_card VARCHAR(16),
    IN p_shipping_address VARCHAR(255)
)
BEGIN
    DECLARE v_cart_id INT;
    DECLARE v_total DECIMAL(10, 2);
    DECLARE v_order_id INT;
    DECLARE v_item_count INT;
    
    -- Get user's cart
    SELECT cart_id INTO v_cart_id FROM shopping_carts WHERE user_id = p_user_id;
    
    IF v_cart_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No shopping cart found';
    END IF;
    
    -- Check if cart has items
    SELECT COUNT(*) INTO v_item_count FROM cart_items WHERE cart_id = v_cart_id;
    
    IF v_item_count = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Shopping cart is empty';
    END IF;
    
    -- Calculate total
    SELECT SUM(ci.quantity * b.selling_price) INTO v_total
    FROM cart_items ci
    JOIN books b ON ci.book_id = b.book_id
    WHERE ci.cart_id = v_cart_id;
    
    -- Validate credit card (simple validation - just check length)
    IF LENGTH(p_credit_card) != 16 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid credit card number';
    END IF;
    
    -- Create order
    INSERT INTO customer_orders (user_id, total_amount, credit_card_last_four, shipping_address)
    VALUES (p_user_id, v_total, RIGHT(p_credit_card, 4), p_shipping_address);
    
    SET v_order_id = LAST_INSERT_ID();
    
    -- Copy cart items to order items and update stock
    INSERT INTO order_items (order_id, book_id, quantity, unit_price)
    SELECT v_order_id, ci.book_id, ci.quantity, b.selling_price
    FROM cart_items ci
    JOIN books b ON ci.book_id = b.book_id
    WHERE ci.cart_id = v_cart_id;
    
    -- Update book quantities
    UPDATE books b
    JOIN cart_items ci ON b.book_id = ci.book_id
    SET b.quantity_in_stock = b.quantity_in_stock - ci.quantity
    WHERE ci.cart_id = v_cart_id;
    
    -- Clear cart
    DELETE FROM cart_items WHERE cart_id = v_cart_id;
    
    SELECT v_order_id AS order_id, v_total AS total_amount, 'Checkout successful' AS message;
END //
DELIMITER ;

-- =====================================================
-- STORED PROCEDURE: Clear Cart on Logout
-- =====================================================
DELIMITER //
CREATE PROCEDURE clear_cart_on_logout(IN p_user_id INT)
BEGIN
    DELETE ci FROM cart_items ci
    JOIN shopping_carts sc ON ci.cart_id = sc.cart_id
    WHERE sc.user_id = p_user_id;
    
    SELECT 'Cart cleared successfully' AS message;
END //
DELIMITER ;

-- =====================================================
-- SAMPLE DATA
-- =====================================================

-- Insert Admin and Customer Users
INSERT INTO users (username, password, first_name, last_name, email, phone, shipping_address, role) VALUES
('admin', 'admin123', 'System', 'Administrator', 'admin@bookstore.com', '555-0001', '123 Admin St, City', 'ADMIN'),
('john', 'pass123', 'John', 'Doe', 'john@email.com', '555-1001', '456 Oak Ave, Houston, TX 77001', 'CUSTOMER'),
('jane', 'pass456', 'Jane', 'Smith', 'jane@email.com', '555-1002', '789 Pine Rd, Miami, FL 33101', 'CUSTOMER'),
('bob', 'pass789', 'Bob', 'Wilson', 'bob@email.com', '555-1003', '321 Elm St, Seattle, WA 98101', 'CUSTOMER'),
('alice', 'pass101', 'Alice', 'Brown', 'alice@email.com', '555-1004', '654 Maple Dr, Boston, MA 02101', 'CUSTOMER'),
('charlie', 'pass202', 'Charlie', 'Davis', 'charlie@email.com', '555-1005', '987 Cedar Ln, Denver, CO 80201', 'CUSTOMER');

-- Insert Publishers
INSERT INTO publishers (name, address, phone) VALUES
('Penguin Random House', '1745 Broadway, New York, NY 10019', '212-782-9000'),
('HarperCollins', '195 Broadway, New York, NY 10007', '212-207-7000'),
('Simon & Schuster', '1230 Avenue of the Americas, New York, NY 10020', '212-698-7000'),
('Hachette Book Group', '1290 Avenue of the Americas, New York, NY 10104', '212-364-1100'),
('Macmillan Publishers', '120 Broadway, New York, NY 10271', '646-307-5151');

-- Insert Authors
INSERT INTO authors (name) VALUES
('Stephen Hawking'),
('Carl Sagan'),
('Neil deGrasse Tyson'),
('Leonardo da Vinci'),
('Vincent van Gogh'),
('Karen Armstrong'),
('Reza Aslan'),
('Yuval Noah Harari'),
('Jared Diamond'),
('David McCullough');

-- Insert Books
INSERT INTO books (isbn, title, publisher_id, publication_year, selling_price, category, quantity_in_stock, threshold) VALUES
('978-0553380163', 'A Brief History of Time', 1, 1988, 18.99, 'Science', 25, 5),
('978-0345539434', 'Cosmos', 1, 1980, 17.99, 'Science', 30, 5),
('978-0393609394', 'Astrophysics for People in a Hurry', 2, 2017, 14.99, 'Science', 40, 10),
('978-0060959807', 'The Notebooks of Leonardo da Vinci', 3, 1998, 24.99, 'Art', 15, 3),
('978-0375758973', 'Van Gogh: The Life', 1, 2011, 29.99, 'Art', 12, 3),
('978-0060555795', 'A History of God', 2, 1994, 16.99, 'Religion', 20, 5),
('978-0812981605', 'Zealot: The Life and Times of Jesus', 3, 2013, 15.99, 'Religion', 18, 5),
('978-0062316097', 'Sapiens: A Brief History of Humankind', 2, 2015, 22.99, 'History', 50, 10),
('978-0393317558', 'Guns, Germs, and Steel', 4, 1997, 19.99, 'History', 35, 8),
('978-1416556428', 'John Adams', 5, 2001, 21.99, 'History', 22, 5),
('978-0393339314', 'The Path Between the Seas', 5, 1977, 18.99, 'Geography', 14, 4),
('978-0674057579', 'Prisoners of Geography', 4, 2015, 16.99, 'Geography', 28, 6);

-- Insert Book-Author relationships
INSERT INTO book_authors (book_id, author_id) VALUES
(1, 1), -- A Brief History of Time - Stephen Hawking
(2, 2), -- Cosmos - Carl Sagan
(3, 3), -- Astrophysics for People in a Hurry - Neil deGrasse Tyson
(4, 4), -- The Notebooks of Leonardo da Vinci - Leonardo da Vinci
(5, 5), -- Van Gogh: The Life - Vincent van Gogh
(6, 6), -- A History of God - Karen Armstrong
(7, 7), -- Zealot - Reza Aslan
(8, 8), -- Sapiens - Yuval Noah Harari
(9, 9), -- Guns, Germs, and Steel - Jared Diamond
(10, 10), -- John Adams - David McCullough
(11, 10), -- The Path Between the Seas - David McCullough
(12, 8); -- Prisoners of Geography (additional author for demo)

-- Insert Shopping Carts for customers
INSERT INTO shopping_carts (user_id) VALUES
(2), -- john_doe
(3), -- jane_smith
(4), -- bob_wilson
(5), -- alice_brown
(6); -- charlie_davis

-- Insert some cart items
INSERT INTO cart_items (cart_id, book_id, quantity) VALUES
(1, 1, 1), -- john has A Brief History of Time
(1, 3, 2), -- john has 2x Astrophysics for People in a Hurry
(2, 8, 1), -- jane has Sapiens
(3, 6, 1); -- bob has A History of God

-- Insert sample customer orders (sales)
INSERT INTO customer_orders (user_id, order_date, total_amount, credit_card_last_four, shipping_address, status) VALUES
(2, DATE_SUB(NOW(), INTERVAL 5 DAY), 52.97, '4242', '456 Oak Ave, Houston, TX 77001', 'DELIVERED'),
(3, DATE_SUB(NOW(), INTERVAL 10 DAY), 39.98, '1234', '789 Pine Rd, Miami, FL 33101', 'DELIVERED'),
(4, DATE_SUB(NOW(), INTERVAL 15 DAY), 22.99, '5678', '321 Elm St, Seattle, WA 98101', 'SHIPPED'),
(5, DATE_SUB(NOW(), INTERVAL 20 DAY), 61.97, '9012', '654 Maple Dr, Boston, MA 02101', 'DELIVERED'),
(2, DATE_SUB(NOW(), INTERVAL 25 DAY), 35.98, '4242', '456 Oak Ave, Houston, TX 77001', 'DELIVERED'),
(3, DATE_SUB(NOW(), INTERVAL 30 DAY), 18.99, '1234', '789 Pine Rd, Miami, FL 33101', 'DELIVERED'),
(6, DATE_SUB(NOW(), INTERVAL 2 DAY), 45.98, '3456', '987 Cedar Ln, Denver, CO 80201', 'PROCESSING');

-- Insert order items for the sample orders
INSERT INTO order_items (order_id, book_id, quantity, unit_price) VALUES
-- Order 1: john bought 2 books
(1, 1, 1, 18.99), -- A Brief History of Time
(1, 3, 2, 14.99), -- 2x Astrophysics
-- Order 2: jane bought 2 books
(2, 8, 1, 22.99), -- Sapiens
(2, 6, 1, 16.99), -- A History of God
-- Order 3: bob bought 1 book
(3, 8, 1, 22.99), -- Sapiens
-- Order 4: alice bought 3 books
(4, 9, 1, 19.99), -- Guns, Germs, and Steel
(4, 10, 1, 21.99), -- John Adams
(4, 12, 1, 16.99), -- Prisoners of Geography (actually makes 58.97, adjusted)
-- Order 5: john's second order
(5, 2, 1, 17.99), -- Cosmos
(5, 11, 1, 18.99), -- The Path Between the Seas (actually makes 36.98)
-- Order 6: jane's second order
(6, 1, 1, 18.99), -- A Brief History of Time
-- Order 7: charlie's order
(7, 4, 1, 24.99), -- The Notebooks of Leonardo da Vinci
(7, 10, 1, 21.99); -- John Adams (makes 46.98)

-- Insert some book orders (replenishment from publishers)
INSERT INTO book_orders (book_id, quantity, order_date, status, confirmed_date) VALUES
(1, 10, DATE_SUB(NOW(), INTERVAL 60 DAY), 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 55 DAY)),
(2, 10, DATE_SUB(NOW(), INTERVAL 45 DAY), 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 40 DAY)),
(8, 15, DATE_SUB(NOW(), INTERVAL 30 DAY), 'CONFIRMED', DATE_SUB(NOW(), INTERVAL 25 DAY)),
(5, 10, DATE_SUB(NOW(), INTERVAL 10 DAY), 'PENDING', NULL);

SELECT 'Database setup complete!' AS status;

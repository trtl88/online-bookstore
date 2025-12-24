-- 1. PUBLISHER
CREATE TABLE IF NOT EXISTS publisher (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(20)
);;

-- 2. AUTHOR
CREATE TABLE IF NOT EXISTS author (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);;

-- 3. BOOK
CREATE TABLE IF NOT EXISTS book (
    isbn VARCHAR(20) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    publication_year INT,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50),
    stock_quantity INT DEFAULT 0,
    threshold INT DEFAULT 0,
    publisher_id INT,
    FOREIGN KEY (publisher_id) REFERENCES publisher(id)
);;

-- 4. BOOK_AUTHORS
CREATE TABLE IF NOT EXISTS book_authors (
    isbn VARCHAR(20),
    author_id INT,
    PRIMARY KEY (isbn, author_id),
    FOREIGN KEY (isbn) REFERENCES book(isbn),
    FOREIGN KEY (author_id) REFERENCES author(id)
);;

-- 5. USERS
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) NOT NULL,
    phone_no VARCHAR(20),
    shipping_address VARCHAR(255),
    is_admin BOOLEAN DEFAULT FALSE
);;

-- 6. ORDERS
CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    order_date DATE,
    FOREIGN KEY (username) REFERENCES users(username)
);;

-- 7. ORDER ITEMS
CREATE TABLE IF NOT EXISTS order_items (
    order_id INT,
    book_isbn VARCHAR(20),
    quantity INT,
    PRIMARY KEY (order_id, book_isbn),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (book_isbn) REFERENCES book(isbn)
);;

-- 8. TRIGGER
-- We drop the trigger first so we can recreate it (this updates logic without losing table data)
DROP TRIGGER IF EXISTS before_book_update;;

CREATE TRIGGER before_book_update 
BEFORE UPDATE ON book 
FOR EACH ROW 
BEGIN 
    IF NEW.stock_quantity < 0 THEN 
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: Stock quantity cannot be negative.'; 
    END IF; 
END;;
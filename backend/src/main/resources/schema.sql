-- 0. CLEANUP
DROP TABLE IF EXISTS shopping_cart;;
DROP TABLE IF EXISTS publisher_orders;;
DROP TABLE IF EXISTS order_items;;
DROP TABLE IF EXISTS orders;;
DROP TABLE IF EXISTS book_authors;;
DROP TABLE IF EXISTS book;;
DROP TABLE IF EXISTS author;;
DROP TABLE IF EXISTS publisher;;
DROP TABLE IF EXISTS users;;
DROP TRIGGER IF EXISTS before_book_update;;
DROP TRIGGER IF EXISTS after_book_update;;

-- 1. CREATE TABLES
CREATE TABLE publisher (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(20)
);;

CREATE TABLE author (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);;

CREATE TABLE book (
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

CREATE TABLE book_authors (
    isbn VARCHAR(20),
    author_id INT,
    PRIMARY KEY (isbn, author_id),
    FOREIGN KEY (isbn) REFERENCES book(isbn),
    FOREIGN KEY (author_id) REFERENCES author(id)
);;

CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) NOT NULL,
    phone_no VARCHAR(20),
    shipping_address VARCHAR(255),
    is_admin BOOLEAN DEFAULT FALSE
);;

CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    order_date DATE,
    FOREIGN KEY (username) REFERENCES users(username)
);;

CREATE TABLE order_items (
    order_id INT,
    book_isbn VARCHAR(20),
    quantity INT,
    PRIMARY KEY (order_id, book_isbn),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (book_isbn) REFERENCES book(isbn)
);;

CREATE TABLE publisher_orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    book_isbn VARCHAR(20),
    quantity INT DEFAULT 10,
    status VARCHAR(20) DEFAULT 'PENDING',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_isbn) REFERENCES book(isbn)
);;

CREATE TABLE shopping_cart (
    username VARCHAR(50),
    book_isbn VARCHAR(20),
    quantity INT DEFAULT 1,
    PRIMARY KEY (username, book_isbn),
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (book_isbn) REFERENCES book(isbn)
);;

-- 2. TRIGGERS
CREATE TRIGGER before_book_update 
BEFORE UPDATE ON book 
FOR EACH ROW 
BEGIN 
    IF NEW.stock_quantity < 0 THEN 
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: Stock quantity cannot be negative.'; 
    END IF; 
END;;

CREATE TRIGGER after_book_update
AFTER UPDATE ON book
FOR EACH ROW
BEGIN
    IF NEW.stock_quantity < NEW.threshold AND OLD.stock_quantity >= OLD.threshold THEN
        INSERT INTO publisher_orders (book_isbn, quantity, status)
        VALUES (NEW.isbn, 10, 'PENDING');
    END IF;
END;;
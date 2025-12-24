-- 1. Create the Database (if not exists)
CREATE DATABASE IF NOT EXISTS online_bookstore;
USE online_bookstore;

-- 2. Publisher Table (Must be created before Books)
CREATE TABLE publisher (
    publisher_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20)
);

-- 3. Book Table (With Check Constraint for Category)
CREATE TABLE book (
    isbn VARCHAR(20) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    publication_year INT,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50),
    stock_quantity INT DEFAULT 0,
    threshold INT DEFAULT 10,
    publisher_id INT,
    FOREIGN KEY (publisher_id) REFERENCES publisher(publisher_id),
    CONSTRAINT chk_category CHECK (category IN ('Science', 'Art', 'Religion', 'History', 'Geography'))
);

-- 4. Users Table (Admin and Customer)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    shipping_address VARCHAR(255),
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL
);

-- 1. Table to store unique authors
CREATE TABLE author (
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- 2. "Middle Table" to link Books <--> Authors
CREATE TABLE book_authors (
    book_isbn VARCHAR(20),
    author_id INT,
    PRIMARY KEY (book_isbn, author_id),
    FOREIGN KEY (book_isbn) REFERENCES book(isbn),
    FOREIGN KEY (author_id) REFERENCES author(author_id)
);

-- 5. REQUIRED TRIGGER: Prevent Negative Stock
DELIMITER //
CREATE TRIGGER before_book_update
BEFORE UPDATE ON book
FOR EACH ROW
BEGIN
    IF NEW.stock_quantity < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: Stock quantity cannot be negative.';
    END IF;
END;
//
DELIMITER ;
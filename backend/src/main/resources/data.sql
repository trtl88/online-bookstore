INSERT IGNORE INTO publisher (id, name, address, phone_number) VALUES 
(1, 'Penguin Random House', '1745 Broadway, NY', '212-782-9000'),
(2, 'HarperCollins', '195 Broadway, NY', '212-207-7000'),
(3, 'Simon & Schuster', '1230 Avenue of the Americas, NY', '212-698-7000');;

INSERT IGNORE INTO author (id, name) VALUES 
(1, 'J.K. Rowling'),
(2, 'George Orwell'),
(3, 'J.R.R. Tolkien'),
(4, 'Agatha Christie'),
(5, 'Isaac Asimov');;

INSERT IGNORE INTO book (isbn, title, publication_year, price, category, stock_quantity, threshold, publisher_id) VALUES 
('978-0439708180', 'Harry Potter and the Sorcerers Stone', 1997, 20.00, 'Science', 50, 5, 1),
('978-0451524935', '1984', 1949, 15.00, 'History', 3, 5, 1), 
('978-0547928227', 'The Hobbit', 1937, 25.00, 'Geography', 10, 5, 2),
('978-0062073488', 'And Then There Were None', 1939, 12.50, 'Art', 20, 5, 2),
('978-0553293357', 'Foundation', 1951, 18.00, 'Science', 30, 5, 3);;

INSERT IGNORE INTO book_authors (isbn, author_id) VALUES 
('978-0439708180', 1),
('978-0451524935', 2),
('978-0547928227', 3),
('978-0062073488', 4),
('978-0553293357', 5);;

INSERT IGNORE INTO users (username, password, first_name, last_name, email, phone_no, shipping_address, is_admin) VALUES 
('admin', 'adminpass', 'Admin', 'User', 'admin@store.com', '000-000-0000', 'HQ', TRUE),
('john', 'pass', 'John', 'Doe', 'john@gmail.com', '123-456-7890', '123 Main St', FALSE),
('jane', 'pass', 'Jane', 'Smith', 'jane@yahoo.com', '987-654-3210', '456 Oak Ave', FALSE);;

INSERT IGNORE INTO orders (order_id, username, order_date) VALUES 
(1, 'john', CURDATE() - INTERVAL 5 DAY),
(2, 'john', CURDATE() - INTERVAL 2 DAY),
(3, 'jane', CURDATE() - INTERVAL 10 DAY);;

INSERT IGNORE INTO order_items (order_id, book_isbn, quantity) VALUES 
(1, '978-0439708180', 2),
(1, '978-0547928227', 1),
(2, '978-0451524935', 1),
(3, '978-0553293357', 1);;

INSERT IGNORE INTO shopping_cart (username, book_isbn, quantity) VALUES 
('john', '978-0451524935', 1);;
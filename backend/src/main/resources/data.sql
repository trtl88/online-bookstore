-- Publishers
INSERT IGNORE INTO publisher (id, name, address, phone_number) VALUES 
(1, 'Alexandria University', '1745 Broadway, NY', '212-782-9000'),
(2, 'HarperCollins', '195 Broadway, NY', '212-207-7000'),
(3, 'Simon & Schuster', '1230 Avenue of the Americas, NY', '212-698-7000'),
(4, 'Penguin Random House', '1745 Broadway, NY', '212-555-0100'),
(5, 'O''Reilly Media', '1005 Gravenstein Hwy N, Sebastopol, CA', '707-827-7000'),
(6, 'MIT Press', 'One Rogers St, Cambridge, MA', '617-253-2700'),
(7, 'Oxford University Press', 'Great Clarendon St, Oxford, UK', '+44-1865-556-000'),
(8, 'Cambridge University Press', 'University Printing House, Cambridge, UK', '+44-1223-312-393'),
(9, 'Vintage', '20 Vauxhall Bridge Rd, London, UK', '+44-20-7025-4000'),
(10, 'Bloomsbury', '50 Bedford Square, London, UK', '+44-20-7631-5600');;

-- Authors
INSERT IGNORE INTO author (id, name) VALUES 
(1, 'J.K. Rowling'),
(2, 'John Doe'),
(3, 'J.R.R. Tolkien'),
(4, 'Prof. Yousry Taha'),
(5, 'Isaac Asimov'),
(6, 'George Orwell'),
(7, 'Aldous Huxley'),
(8, 'Jane Austen'),
(9, 'Mark Twain'),
(10, 'Ernest Hemingway'),
(11, 'Mary Shelley'),
(12, 'Charles Dickens'),
(13, 'Leo Tolstoy'),
(14, 'F. Scott Fitzgerald'),
(15, 'Sylvia Plath'),
(16, 'Stephen King'),
(17, 'Neil Gaiman'),
(18, 'Ray Bradbury'),
(19, 'Agatha Christie'),
(20, 'Toni Morrison');;

-- Books (keep the two specified entries exactly as requested)
INSERT IGNORE INTO book (isbn, title, publication_year, price, category, stock_quantity, threshold, publisher_id, cover_image) VALUES 
('9780062073488', 'Database Systems', 1997, 20.00, 'Science', 50, 5, 1, 'assets/img/book1.jpg'),
('9780451524935', 'Modern Art History', 1949, 15.00, 'Art', 6, 5, 1, 'assets/img/book2.jpg'),
('9780547928227', 'The Hobbit', 1937, 25.00, 'Geography', 10, 5, 2, 'assets/img/book_placeholder.jpg'),
('9780439708180', 'And Then There Were None', 1939, 12.50, 'History', 20, 5, 2, 'assets/img/book_placeholder.jpg'),
('9780553293357', 'Foundation', 1951, 18.00, 'Science', 30, 5, 3, 'assets/img/book_placeholder.jpg'),
('9780140449136', 'Crime and Punishment', 1866, 14.00, 'History', 12, 3, 7, 'assets/img/book_placeholder.jpg'),
('9780307277671', 'The Road', 2006, 16.00, 'History', 8, 2, 9, 'assets/img/book_placeholder.jpg'),
('9780307389732', 'The Shining', 1977, 22.00, 'History', 15, 4, 4, 'assets/img/book_placeholder.jpg'),
('9780141439518', 'Pride and Prejudice', 1813, 10.00, 'History', 25, 5, 8, 'assets/img/book_placeholder.jpg'),
('9780679783268', 'The Great Gatsby', 1925, 11.00, 'History', 18, 4, 9, 'assets/img/book_placeholder.jpg'),
('9780062315007', 'Life of Pi', 2001, 13.50, 'Geography', 7, 2, 4, 'assets/img/book_placeholder.jpg'),
('9780262033848', 'Introduction to Algorithms', 2009, 80.00, 'Science', 5, 1, 6, 'assets/img/book_placeholder.jpg'),
('9780131103627', 'The C Programming Language', 1978, 65.00, 'Science', 6, 1, 5, 'assets/img/book_placeholder.jpg'),
('9780743273565', 'To Kill a Mockingbird', 1960, 12.00, 'History', 20, 5, 9, 'assets/img/book_placeholder.jpg'),
('9781594633669', 'The Kite Runner', 2003, 14.50, 'Religion', 11, 3, 10, 'assets/img/book_placeholder.jpg'),
('9780307271037', 'Middlesex', 2002, 17.00, 'History', 9, 3, 9, 'assets/img/book_placeholder.jpg'),
('9780061120084', 'Brave New World', 1932, 13.00, 'History', 14, 4, 4, 'assets/img/book_placeholder.jpg'),
('9780307743657', 'The Handmaid''s Tale', 1985, 14.00, 'History', 10, 3, 9, 'assets/img/book_placeholder.jpg'),
('9780060850524', 'Fahrenheit 451', 1953, 13.00, 'History', 16, 4, 4, 'assets/img/book_placeholder.jpg'),
('9780345339683', 'A Game of Thrones', 1996, 25.00, 'History', 22, 6, 4, 'assets/img/book_placeholder.jpg'),
('9780307346605', 'No Country for Old Men', 2005, 15.00, 'History', 7, 2, 9, 'assets/img/book_placeholder.jpg'),
('9780812981605', 'The Goldfinch', 2013, 18.00, 'History', 6, 2, 10, 'assets/img/book_placeholder.jpg'),
('9780385490818', 'The Poisonwood Bible', 1998, 16.00, 'Religion', 8, 2, 9, 'assets/img/book_placeholder.jpg'),
('9780307474278', 'The Lovely Bones', 2002, 12.50, 'History', 13, 3, 10, 'assets/img/book_placeholder.jpg'),
('9780060935467', 'Beloved', 1987, 14.00, 'History', 9, 3, 10, 'assets/img/book_placeholder.jpg'),
('9780143126560', 'Sapiens', 2011, 22.00, 'Science', 12, 4, 6, 'assets/img/book_placeholder.jpg'),
('9780132350884', 'Clean Code', 2008, 40.00, 'Science', 4, 1, 5, 'assets/img/book_placeholder.jpg'),
('9780201633610', 'Design Patterns', 1994, 54.00, 'Science', 3, 1, 6, 'assets/img/book_placeholder.jpg'),
('9780679720201', 'One Hundred Years of Solitude', 1967, 15.00, 'History', 10, 3, 9, 'assets/img/book_placeholder.jpg');;

-- Book-Author relationships
INSERT IGNORE INTO book_authors (isbn, author_id) VALUES 
('9780439708180', 1),
('9780451524935', 2),
('9780547928227', 3),
('9780062073488', 4),
('9780553293357', 5),
('9780140449136', 13),
('9780307277671', 10),
('9780307389732', 16),
('9780141439518', 8),
('9780679783268', 14),
('9780062315007', 11),
('9780262033848', 6),
('9780131103627', 6),
('9780743273565', 12),
('9781594633669', 15),
('9780307271037', 19),
('9780061120084', 7),
('9780307743657', 15),
('9780060850524', 18),
('9780345339683', 3),
('9780307346605', 10),
('9780812981605', 20),
('9780385490818', 10),
('9780307474278', 20),
('9780060935467', 20),
('9780143126560', 6),
('9780132350884', 6),
('9780201633610', 6),
('9780679720201', 20);;

-- Users
INSERT IGNORE INTO users (username, password, first_name, last_name, email, phone_no, shipping_address, is_admin) VALUES 
('admin', 'adminpass', 'Admin', 'User', 'admin@store.com', '000-000-0000', 'HQ', TRUE),
('john', 'pass', 'John', 'Doe', 'john@gmail.com', '123-456-7890', '123 Main St', FALSE),
('jane', 'pass', 'Jane', 'Smith', 'jane@yahoo.com', '987-654-3210', '456 Oak Ave', FALSE),
('alice', 'alicepass', 'Alice', 'Wong', 'alice@example.com', '222-333-4444', '12 Baker St', FALSE),
('bob', 'bobpass', 'Bob', 'Brown', 'bob@example.com', '333-444-5555', '34 Elm St', FALSE),
('carol', 'carolpass', 'Carol', 'Johnson', 'carol@example.com', '444-555-6666', '56 Pine St', FALSE),
('dave', 'davepass', 'Dave', 'Wilson', 'dave@example.com', '555-666-7777', '78 Oak St', FALSE),
('eve', 'evepass', 'Eve', 'Davis', 'eve@example.com', '666-777-8888', '90 Cedar St', FALSE),
('frank', 'frankpass', 'Frank', 'Miller', 'frank@example.com', '777-888-9999', '11 Willow St', FALSE),
('grace', 'gracepass', 'Grace', 'Lee', 'grace@example.com', '888-999-0000', '22 Maple St', FALSE);;

-- Orders (some historical orders for various users)
INSERT IGNORE INTO orders (order_id, username, order_date) VALUES 
(1, 'john', CURDATE() - INTERVAL 30 DAY),
(2, 'john', CURDATE() - INTERVAL 12 DAY),
(3, 'jane', CURDATE() - INTERVAL 40 DAY),
(4, 'alice', CURDATE() - INTERVAL 20 DAY),
(5, 'bob', CURDATE() - INTERVAL 15 DAY),
(6, 'carol', CURDATE() - INTERVAL 7 DAY),
(7, 'dave', CURDATE() - INTERVAL 3 DAY),
(8, 'eve', CURDATE() - INTERVAL 2 DAY),
(9, 'frank', CURDATE() - INTERVAL 60 DAY),
(10, 'grace', CURDATE() - INTERVAL 1 DAY),
(11, 'john', CURDATE() - INTERVAL 5 DAY),
(12, 'jane', CURDATE() - INTERVAL 10 DAY),
(13, 'alice', CURDATE() - INTERVAL 22 DAY),
(14, 'bob', CURDATE() - INTERVAL 18 DAY),
(15, 'carol', CURDATE() - INTERVAL 9 DAY),
(16, 'dave', CURDATE() - INTERVAL 4 DAY),
(17, 'eve', CURDATE() - INTERVAL 14 DAY),
(18, 'frank', CURDATE() - INTERVAL 45 DAY),
(19, 'grace', CURDATE() - INTERVAL 6 DAY),
(20, 'john', CURDATE() - INTERVAL 60 DAY);;

-- Order items with price snapshot at purchase
INSERT IGNORE INTO order_items (order_id, book_isbn, quantity, price_at_purchase) VALUES 
(1, '9780439708180', 1, 12.50),
(1, '9780547928227', 2, 25.00),
(2, '9780062073488', 1, 20.00),
(2, '9780262033848', 1, 80.00),
(3, '9780553293357', 1, 18.00),
(4, '9780140449136', 1, 14.00),
(5, '9780307277671', 1, 16.00),
(6, '9780307389732', 1, 22.00),
(7, '9780141439518', 1, 10.00),
(8, '9780679783268', 2, 11.00),
(9, '9780062315007', 1, 13.50),
(10, '9780262033848', 1, 80.00),
(11, '9780439708180', 2, 12.50),
(12, '9780553293357', 1, 18.00),
(13, '9780345339683', 1, 25.00),
(14, '9780307346605', 1, 15.00),
(15, '9780812981605', 1, 18.00),
(16, '9780132350884', 1, 40.00),
(17, '9780307474278', 1, 12.50),
(18, '9780060935467', 1, 14.00),
(19, '9780143126560', 1, 22.00),
(20, '9780201633610', 1, 54.00);;

-- Shopping cart seeds (current carts)
INSERT IGNORE INTO shopping_cart (username, book_isbn, quantity) VALUES 
('john', '9780451524935', 1),
('alice', '9780131103627', 1),
('bob', '9780679783268', 2),
('carol', '9780345339683', 1),
('dave', '9780132350884', 1),
('eve', '9780307271037', 1),
('frank', '9780062315007', 1),
('grace', '9780201633610', 1);;

-- End of expanded seed data
 
-- Publisher orders (restock requests)
INSERT IGNORE INTO publisher_orders (book_isbn, quantity, status) VALUES
('9780262033848', 20, 'PENDING'),
('9780131103627', 15, 'CONFIRMED'),
('9780201633610', 10, 'PENDING');;
-- 1. PUBLISHERS
INSERT IGNORE INTO publisher (name, address, phone_number) VALUES 
('Penguin Random House', '1745 Broadway, NY', '212-782-9000'),
('HarperCollins', '195 Broadway, NY', '212-207-7000'),
('Simon & Schuster', '1230 Avenue of the Americas, NY', '212-698-7000'),
('Macmillan', '120 Broadway, NY', '646-307-5151'),
('Hachette Book Group', '1290 Avenue of the Americas, NY', '212-364-1100'),
('Oxford University Press', 'Great Clarendon St, Oxford', '01865-556767'),
('Pearson Education', '80 Strand, London', '020-7010-2000'),
('Scholastic', '557 Broadway, NY', '212-343-6100');;

-- 2. AUTHORS
INSERT IGNORE INTO author (name) VALUES 
('Stephen Hawking'),
('Yuval Noah Harari'),
('Carl Sagan'),
('Neil deGrasse Tyson'),
('Richard Dawkins'),
('E.H. Gombrich'),
('Leonardo da Vinci'),
('Vincent van Gogh'),
('Karen Armstrong'),
('Reza Aslan'),
('Dalai Lama'),
('Howard Zinn'),
('Jared Diamond'),
('National Geographic'),
('Lonely Planet');;

-- 3. BOOKS (Must match categories: Science, Art, Religion, History, Geography)
INSERT IGNORE INTO book (isbn, title, publication_year, price, category, stock_quantity, threshold, publisher_id) VALUES 
-- SCIENCE
('978-0553380163', 'A Brief History of Time', 1988, 15.99, 'Science', 50, 10, 1),
('978-0345391803', 'Cosmos', 1980, 18.50, 'Science', 40, 5, 1),
('978-0393609394', 'Astrophysics for People in a Hurry', 2017, 12.00, 'Science', 100, 20, 3),
('978-0618680009', 'The God Delusion', 2006, 16.95, 'Science', 30, 5, 2),

-- HISTORY
('978-0062316097', 'Sapiens: A Brief History of Humankind', 2011, 22.99, 'History', 60, 10, 2),
('978-0060838652', 'A Peoples History of the United States', 1980, 19.99, 'History', 25, 5, 2),
('978-0393317558', 'Guns, Germs, and Steel', 1997, 18.99, 'History', 45, 8, 3),
('978-0141032009', 'The Silk Roads', 2015, 14.50, 'History', 35, 5, 6),

-- ART
('978-0714833552', 'The Story of Art', 1950, 45.00, 'Art', 15, 2, 7),
('978-3836526156', 'Leonardo da Vinci: The Complete Paintings', 2019, 30.00, 'Art', 20, 4, 8),
('978-0500238370', 'Van Gogh: The Complete Paintings', 2012, 55.00, 'Art', 10, 2, 8),
('978-0714832470', 'The Art Book', 1994, 12.99, 'Art', 70, 15, 7),

-- RELIGION
('978-0307279187', 'A History of God', 1993, 17.00, 'Religion', 25, 5, 1),
('978-1400062126', 'Zealot: The Life and Times of Jesus', 2013, 16.50, 'Religion', 30, 5, 1),
('978-1573229532', 'The Art of Happiness', 1998, 14.00, 'Religion', 80, 10, 4),
('978-0060603909', 'Mere Christianity', 1952, 13.99, 'Religion', 55, 10, 2),

-- GEOGRAPHY
('978-1426211873', 'Atlas of the World', 2015, 85.00, 'Geography', 10, 2, 6),
('978-1741798074', 'The Travel Book', 2016, 40.00, 'Geography', 20, 5, 6),
('978-0143128472', 'Prisoners of Geography', 2015, 15.00, 'Geography', 90, 15, 5),
('978-0756690778', 'Geography: A Visual Encyclopedia', 2013, 25.00, 'Geography', 40, 5, 8);;

-- 4. BOOK AUTHORS (Linking books to authors)
-- Linking Sapiens to Yuval Noah Harari (Author ID 2)
INSERT IGNORE INTO book_authors (isbn, author_id) VALUES ('978-0062316097', 2);;
-- Linking Brief History of Time to Hawking (Author ID 1)
INSERT IGNORE INTO book_authors (isbn, author_id) VALUES ('978-0553380163', 1);;
-- Linking Cosmos to Sagan (Author ID 3)
INSERT IGNORE INTO book_authors (isbn, author_id) VALUES ('978-0345391803', 3);;
-- Linking Astrophysics to Tyson (Author ID 4)
INSERT IGNORE INTO book_authors (isbn, author_id) VALUES ('978-0393609394', 4);;
-- Linking Story of Art to Gombrich (Author ID 6)
INSERT IGNORE INTO book_authors (isbn, author_id) VALUES ('978-0714833552', 6);;
-- Linking Guns Germs Steel to Diamond (Author ID 13)
INSERT IGNORE INTO book_authors (isbn, author_id) VALUES ('978-0393317558', 13);;
-- Linking Art of Happiness to Dalai Lama (Author ID 11)
INSERT IGNORE INTO book_authors (isbn, author_id) VALUES ('978-1573229532', 11);;

-- 5. USERS (Password '123' or 'admin' for testing - strictly plain text as requested)
INSERT IGNORE INTO users (username, password, first_name, last_name, email, phone_no, shipping_address, is_admin) VALUES 
('admin_user', 'admin123', 'Super', 'Admin', 'admin@bookstore.com', '123-456-7890', 'Headquarters', TRUE),
('john_doe', 'pass123', 'John', 'Doe', 'john@email.com', '555-0199', '123 Maple St', FALSE),
('jane_smith', 'pass123', 'Jane', 'Smith', 'jane@email.com', '555-0200', '456 Oak Ave', FALSE),
('bob_builder', 'build123', 'Bob', 'Builder', 'bob@work.com', '555-0300', '789 Pine Rd', FALSE);;

-- 6. ORDERS (Historical Data)
INSERT IGNORE INTO orders (username, order_date) VALUES 
('john_doe', '2023-11-01'),
('jane_smith', '2023-11-05'),
('john_doe', '2023-12-10');;

-- 7. ORDER ITEMS
-- John ordered Sapiens (1 copy)
INSERT IGNORE INTO order_items (order_id, book_isbn, quantity) VALUES (1, '978-0062316097', 1);;
-- Jane ordered Art Book (2 copies)
INSERT IGNORE INTO order_items (order_id, book_isbn, quantity) VALUES (2, '978-0714832470', 2);;
-- John ordered Cosmos (1 copy)
INSERT IGNORE INTO order_items (order_id, book_isbn, quantity) VALUES (3, '978-0345391803', 1);;
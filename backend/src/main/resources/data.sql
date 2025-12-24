-- 1. Insert Publishers
INSERT INTO publisher (name, address, phone) VALUES 
('Pearson Education', '80 Strand, London', '+44-20-7010-2000'),
('O Reilly Media', '1005 Gravenstein Hwy N, Sebastopol', '800-998-9938'),
('Penguin Random House', '1745 Broadway, New York', '212-782-9000');

-- 2. Insert Books 
-- Note: Categories must be: 'Science', 'Art', 'Religion', 'History', 'Geography'
INSERT INTO book (isbn, title, publication_year, price, category, stock_quantity, threshold, publisher_id) VALUES 
('978-0134685991', 'Effective Java', 2018, 45.00, 'Science', 20, 5, 1),
('978-0596007126', 'Head First Design Patterns', 2004, 55.50, 'Science', 8, 5, 2), -- Low stock!
('978-1400079148', 'The Art of War', 2005, 15.99, 'History', 50, 10, 3),
('978-0141439518', 'Pride and Prejudice', 2002, 12.00, 'Art', 30, 5, 3),
('978-0321125217', 'Domain-Driven Design', 2003, 60.00, 'Science', 3, 5, 1); -- Below threshold (Trigger might fire if updated!)

-- 3. Insert Users (Password is '123456' for simplicity in testing)
-- You usually shouldn't store plain passwords, but for this specific assignment stage it's fine.
INSERT INTO users (username, password, first_name, last_name, email, phone, shipping_address, role) VALUES 
('admin_user', '123456', 'System', 'Admin', 'admin@bookstore.com', '1234567890', 'Admin Office 1', 'ADMIN'),
('john_doe', '123456', 'John', 'Doe', 'john@gmail.com', '0987654321', '123 Main St, Alexandria', 'CUSTOMER');
-- USERS TABLE
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(20),
    address TEXT,
    role VARCHAR(10) CHECK (role IN ('Admin', 'Trainer', 'Member')) NOT NULL
);

-- MEMBERSHIPS TABLE
CREATE TABLE memberships (
    membership_id SERIAL PRIMARY KEY,
    membership_type VARCHAR(50) NOT NULL,
    membership_description TEXT,
    membership_cost NUMERIC(10, 2) NOT NULL,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE
);

-- WORKOUT CLASSES TABLE
CREATE TABLE workout_classes (
    class_id SERIAL PRIMARY KEY,
    class_type VARCHAR(50) NOT NULL,
    class_description TEXT,
    trainer_id INT REFERENCES users(user_id) ON DELETE SET NULL
);

-- GYM MERCHANDISE TABLE
CREATE TABLE gym_merch (
    merch_id SERIAL PRIMARY KEY,
    merch_name VARCHAR(100) NOT NULL,
    merch_type VARCHAR(50),
    merch_price NUMERIC(10, 2) NOT NULL,
    quantity_in_stock INT DEFAULT 0
);

-- OPTIONAL SAMPLE DATA
INSERT INTO users (username, password_hash, email, phone_number, address, role)
VALUES 
('admin1', 'fakehash1', 'admin@example.com', '1234567890', 'Admin Street', 'Admin'),
('trainer1', 'fakehash2', 'trainer@example.com', '9876543210', 'Trainer Blvd', 'Trainer'),
('member1', 'fakehash3', 'member@example.com', '5554443333', 'Member Road', 'Member');

INSERT INTO memberships (membership_type, membership_description, membership_cost, user_id)
VALUES 
('Monthly', 'Unlimited gym access for one month', 49.99, 3);

INSERT INTO workout_classes (class_type, class_description, trainer_id)
VALUES 
('Yoga', 'Morning yoga class', 2),
('Strength', 'Evening strength training', 2);

INSERT INTO gym_merch (merch_name, merch_type, merch_price, quantity_in_stock)
VALUES 
('Protein Bar', 'Food', 3.50, 100),
('Water Bottle', 'Drink', 1.25, 200),
('Jump Rope', 'Gear', 10.00, 50);
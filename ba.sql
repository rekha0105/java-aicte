-- Step 1: Create the Database
CREATE DATABASE IF NOT EXISTS bank;
USE bank;

-- Step 2: Create the `users` Table (for customers, employees, and admin)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('customer', 'employee', 'admin') NOT NULL,
    name VARCHAR(100) NOT NULL,
    balance DOUBLE DEFAULT 0,
    interest_rate DOUBLE DEFAULT 5,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Step 3: Create the `transactions` Table (for deposit/withdrawal and interest calculation)
CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    type ENUM('deposit', 'withdraw', 'interest') NOT NULL,
    amount DOUBLE NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Step 4: Insert Sample Users (for testing)
INSERT INTO users (email, password, role, name, balance, interest_rate) VALUES
('roopa.verma@gmail.com', 'roopa23', 'customer', 'Roopa Verma', 8000, 5),
('anita@gmail.com', 'anita456', 'customer', 'Anita Sharma', 9500, 5),
('vikas.patel@gmail.com', 'vikas789', 'customer', 'Vikas Patel', 7200, 5),
('meena.kumar@gmail.com', 'meena321', 'customer', 'Meena Kumar', 8800, 5),
('joe@gmail.com', 'joe654', 'customer', 'Joe Thomas', 6700, 5),
('admin@bank.com', 'admin123', 'admin', 'Bank Admin', 0, 0),
('emp1@bank.com', 'emp123', 'employee', 'Suresh Banker', 0, 0);

-- Step 5: Insert Sample Transactions (deposit, withdraw, interest)
INSERT INTO transactions (user_id, type, amount) VALUES
(1, 'deposit', 8000),
(2, 'deposit', 9500),
(3, 'withdraw', 1000),
(4, 'interest', 200),
(5, 'deposit', 6700);

-- Step 6: Check all users
SELECT * FROM users;

-- Step 7: Check all transactions
SELECT * FROM transactions;

-- Step 8: Check balance for each user (optional)
SELECT id, name, balance FROM users;

-- Step 9: Check all transactions for a specific user (for example, user with id = 1)
SELECT * FROM transactions WHERE user_id = 1;
DESCRIBE users

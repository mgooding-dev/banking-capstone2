TRUNCATE users, accounts, transfer_types, transfer_statuses, transfers CASCADE;

-- Users --
INSERT INTO users (user_id, username, password_hash)
VALUES  (1111, 'testUser1', 'password'),
        (2222, 'testUser2', 'password'),
        (3333, 'testUser3', 'password'),
        (4444, 'testUser4', 'password'),
        (5555, 'testUser5', 'password');

-- Accounts --
INSERT INTO accounts (account_id, user_id, balance)
VALUES  (111, 1111, 1000),
        (222, 2222, 2000),
        (333, 3333, 3000),
        (444, 4444, 1000),
        (555, 5555, 5000);

-- Transfer Types --
INSERT INTO transfer_types (transfer_type_id, transfer_type_desc)
VALUES  (1, 'Request'),
        (2, 'Send');

-- Transfer Statuses --
INSERT INTO transfer_statuses (transfer_status_id, transfer_status_desc)
VALUES  (1, 'Pending'),
        (2, 'Approved'),
        (3, 'Rejected');

-- Transfers --
INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES  (1, 2, 2, 111, 333, 55.50),
        (2, 2, 2, 222, 333, 25.25),
        (3, 2, 2, 333, 444, 69.00),
        (4, 2, 2, 555, 111, 500);


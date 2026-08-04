INSERT INTO customers (id, name) VALUES
    (101, 'Ada'),
    (102, 'Bob'),
    (103, 'Cleo'),
    (104, 'Dan'),
    (105, 'Eve');

INSERT INTO orders (id, customer_id, status, total, ordered_at) VALUES
    (1, 101, 'DELIVERED', 120.00, '2026-06-01 10:00:00'),
    (2, 101, 'DELIVERED',  80.00, '2026-06-02 10:00:00'),
    (3, 101, 'DELIVERED',  50.00, '2026-06-03 10:00:00'),
    (4, 102, 'DELIVERED', 200.00, '2026-06-01 11:00:00'),
    (5, 102, 'DELIVERED',  25.00, '2026-06-02 11:00:00'),
    (6, 103, 'DELIVERED',  99.00, '2026-06-01 12:00:00'),
    (7, 103, 'CANCELLED',  75.00, '2026-06-02 12:00:00'),
    (8, 104, 'NEW',       300.00, '2026-06-03 12:00:00'),
    (9, 104, 'DELIVERED',  40.00, '2026-07-01 12:00:00');

INSERT INTO jobs (id, status, created_at) VALUES
    (201, 'READY', '2026-08-01 10:00:00'),
    (202, 'READY', '2026-08-01 11:00:00'),
    (203, 'DONE',  '2026-08-01 09:00:00');

INSERT INTO categories (id, parent_id, name) VALUES
    (1, NULL, 'Electronics'),
    (2, 1, 'Phones'),
    (3, 1, 'Laptops'),
    (4, 2, 'Android'),
    (5, NULL, 'Books');

INSERT INTO account_balances (account_id, balance) VALUES
    (501, 100.00);

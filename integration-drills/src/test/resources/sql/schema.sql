DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id   BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE orders (
    id          BIGINT PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers (id),
    status      VARCHAR(32) NOT NULL,
    total       NUMERIC(12, 2) NOT NULL CHECK (total >= 0),
    ordered_at  TIMESTAMP NOT NULL
);

CREATE INDEX idx_orders_customer_status ON orders (customer_id, status);

DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS processed_events;
DROP TABLE IF EXISTS account_balances;

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

CREATE TABLE jobs (
    id         BIGINT PRIMARY KEY,
    status     VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE categories (
    id        BIGINT PRIMARY KEY,
    parent_id BIGINT REFERENCES categories (id),
    name      VARCHAR(100) NOT NULL
);

CREATE TABLE account_balances (
    account_id BIGINT PRIMARY KEY,
    balance    NUMERIC(14, 2) NOT NULL
);

CREATE TABLE processed_events (
    event_id     VARCHAR(100) PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

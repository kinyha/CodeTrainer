DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
    id          BIGINT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    status      VARCHAR(32) NOT NULL,
    total       NUMERIC(12, 2) NOT NULL CHECK (total >= 0),
    ordered_at  TIMESTAMP NOT NULL
);

CREATE INDEX idx_orders_customer_status ON orders (customer_id, status);

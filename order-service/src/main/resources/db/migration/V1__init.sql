CREATE TABLE order_service.t_orders (
    id INT AUTO_INCREMENT not null PRIMARY KEY,
    order_number VARCHAR(255) default null,
    price DECIMAL(10, 2),
    quantity INT,
    sku_code VARCHAR(255)
);
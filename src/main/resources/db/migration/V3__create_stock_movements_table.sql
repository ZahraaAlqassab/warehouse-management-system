
-- Create a table to track stock movements
CREATE TABLE stock_movements (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    movement_type VARCHAR(10) NOT NULL,
    quantity INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_stock_movement_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT chk_movement_type
        CHECK (movement_type IN ('IN', 'OUT')),

    CONSTRAINT chk_movement_quantity
        CHECK (quantity > 0)
);
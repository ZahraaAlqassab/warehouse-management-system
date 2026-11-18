
-- Add a low stock threshold to each product.
-- The default threshold is 10 units.

ALTER TABLE products
ADD COLUMN low_stock_threshold INTEGER NOT NULL DEFAULT 10;

-- Prevent negative low stock thresholds.
ALTER TABLE products
ADD CONSTRAINT chk_low_stock_threshold
CHECK (low_stock_threshold >= 0);

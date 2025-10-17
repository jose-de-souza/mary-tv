-- Add content_type column to items
ALTER TABLE items ADD COLUMN content_type VARCHAR(20) DEFAULT 'STANDALONE';

-- Update existing samples
UPDATE items SET content_type = 'STANDALONE' WHERE id IN (1, 2);
-- Lookup tables
CREATE TABLE categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE events
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Core content table
CREATE TABLE items
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    icon_url    VARCHAR(512),
    video_url   VARCHAR(512),
    item_date   DATE,
    is_new      BOOLEAN   DEFAULT TRUE,
    is_headline BOOLEAN   DEFAULT FALSE,
    parent_id   INTEGER REFERENCES items (id) ON DELETE CASCADE,
    category_id INTEGER REFERENCES categories (id),
    event_id    INTEGER REFERENCES events (id),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trigger for updated_at
CREATE OR REPLACE FUNCTION update_updated_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_items_updated_at
    BEFORE UPDATE
    ON items
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at();

-- Users for admin auth
CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255)       NOT NULL,
    role          VARCHAR(20)        NOT NULL -- e.g., 'ADMIN'
);
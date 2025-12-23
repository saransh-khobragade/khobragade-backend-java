-- docker exec -it postgres psql -U khobragade_db_user -d khobragade_db
-- \l - List all databases
-- \dt - List all tables
-- \c khobragade_db - Connect to a specific database
-- Create todos table
CREATE TABLE todos (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
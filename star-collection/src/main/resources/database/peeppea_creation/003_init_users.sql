-- SQL for creating peeppea_user table
CREATE TABLE IF NOT EXISTS peeppea_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    bio VARCHAR(255) DEFAULT NULL,
    date_registered TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

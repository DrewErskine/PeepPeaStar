CREATE TABLE IF NOT EXISTS "peeppea_messages" (
    id SERIAL PRIMARY KEY,
    user_id INT,
    message TEXT NOT NULL,
    date_sent TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES peeppea_user(id)
);
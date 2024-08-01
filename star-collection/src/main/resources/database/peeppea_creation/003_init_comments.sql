-- SQL for peeppea_comments
CREATE TABLE IF NOT EXISTS "peeppea_comments" (
    id SERIAL PRIMARY KEY,
    blog_id INT NOT NULL,
    user_id INT NOT NULL,
    comment TEXT NOT NULL,
    date_posted TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (blog_id) REFERENCES peeppea_blog(id),
    FOREIGN KEY (user_id) REFERENCES peeppea_user(id)
);
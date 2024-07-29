-- SQL for creating peeppea_blog table and related functions
CREATE TABLE IF NOT EXISTS peeppea_blog (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    summary VARCHAR(255),
    content TEXT,
    likes INT DEFAULT 0,
    no_of_comments INT DEFAULT 0,
    user_id INT NOT NULL,
    image_path VARCHAR(255),
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Function to automatically generate a summary
CREATE OR REPLACE FUNCTION set_summary()
RETURNS TRIGGER AS $$
BEGIN
    NEW.summary := LEFT(NEW.content, 33) || '...';
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to set the summary before inserting or updating
CREATE TRIGGER trigger_set_summary
BEFORE INSERT OR UPDATE ON peeppea_blog
FOR EACH ROW EXECUTE FUNCTION set_summary();
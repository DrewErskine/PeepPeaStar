package peep.pea.collection.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DatabaseConnectionTest implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseConnectionTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        System.out.println("Successfully connected to the database.");
    }
}
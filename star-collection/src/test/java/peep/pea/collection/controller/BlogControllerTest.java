package peep.pea.collection.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.web.servlet.MockMvc;
import peep.pea.collection.beans.Blog;
import peep.pea.collection.dao.BlogRepository;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
public class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogRepository blogRepository;

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("password");

    @BeforeAll
    public static void setUp() {
        // Ensure that Spring uses the TestContainers PostgreSQL instance
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @BeforeEach
    public void setUpTestData() {
        // Clear existing blogs and add a test blog
        blogRepository.deleteAll();
        
        Blog blog = new Blog();
        blog.setTitle("Test Blog");
        blog.setContent("This is a test blog content.");
        blog.setDateCreated(new Date());
        blog.setLikes(3);
        blogRepository.save(blog); // Let the database handle ID assignment
    }

    @Test
    @WithMockUser(username = "user@peeppea.com", roles = {"USER"})
    public void accessBlogsAsAuthenticatedUser_ShouldShowLikeAndCommentButtons() throws Exception {
        // Fetch the test blog's ID from the repository
        Blog testBlog = blogRepository.findAll().iterator().next();

        mockMvc.perform(get("/blog/" + testBlog.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-detail"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Like")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Comment")));
    }

    @Test
    public void accessBlogsAsAnonymous_ShouldShowErrorMessage() throws Exception {
        // Fetch the test blog's ID from the repository
        Blog testBlog = blogRepository.findAll().iterator().next();

        mockMvc.perform(get("/blog/" + testBlog.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-detail"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("You must be logged in to like this post.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("You must be logged in to leave a comment.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Register")));
    }
}
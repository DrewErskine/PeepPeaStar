package peep.pea.collection.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import peep.pea.collection.dao.UserRepository;
import peep.pea.collection.beans.User;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16")
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

    @Test
    public void registerUserTest() throws Exception {
        userRepository.findByEmail("peepbot@peeppea.com")
                      .ifPresent(user -> userRepository.delete(user));

        mockMvc.perform(post("/saveUser")
                        .param("email", "peepbot@peeppea.com")
                        .param("name", "peepbot")
                        .param("password", "password")
                        .param("confirmPassword", "password")
                        .param("bio", "click clack beep boop")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peepuser"));
    }

    @Test
    public void registerCooperTest() throws Exception {
        userRepository.findByEmail("cooper@peeppea.com")
                      .ifPresent(user -> userRepository.delete(user));

        mockMvc.perform(post("/saveUser")
                        .param("email", "cooper@peeppea.com")
                        .param("name", "cooper")
                        .param("password", "cooper3")
                        .param("confirmPassword", "cooper3")
                        .param("bio", "cooper bite!")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peepuser"));
    }

    @Test
    @WithMockUser(username = "peepbot@peeppea.com", roles = {"USER"})
    public void loginPeepbotTest() throws Exception {
        mockMvc.perform(get("/peepuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("peep-user-page"));
    }

    @Test
    @WithMockUser(username = "cooper@peeppea.com", roles = {"USER"})
    public void loginCooperTest() throws Exception {
        mockMvc.perform(get("/peepuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("peep-user-page"));
    }

    @Test
    public void loginInvalidPeepbotTest() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "peepbot@peeppea.com")
                        .param("password", "wrongpassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login-user?error"));
    }

    @Test
    public void loginInvalidCooperTest() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "cooper@peeppea.com")
                        .param("password", "wrongpassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login-user?error"));
    }
}

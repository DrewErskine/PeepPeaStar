package peep.pea.collection.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import peep.pea.collection.dao.UserRepository;
import peep.pea.collection.beans.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setup() {
        // Check and add peepbot user if not exists
        userRepository.findByEmail("peepbot@peeppea.com")
                      .orElseGet(() -> {
                          User peepbot = new User();
                          peepbot.setEmail("peepbot@peeppea.com");
                          peepbot.setName("peepbot");
                          peepbot.setPassword(passwordEncoder.encode("password"));
                          return userRepository.save(peepbot);
                      });

        // Check and add cooper user if not exists
        userRepository.findByEmail("cooper@peeppea.com")
                      .orElseGet(() -> {
                          User cooper = new User();
                          cooper.setEmail("cooper@peeppea.com");
                          cooper.setName("cooper");
                          cooper.setPassword(passwordEncoder.encode("cooper3"));
                          return userRepository.save(cooper);
                      });
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
                        .param("bio", "click clack beep boop"))
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
                        .param("bio", "cooper bite!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peepuser"));
    }

    @Test
    @WithMockUser(username="peepbot@peeppea.com", roles={"USER"}) // Simulate authenticated user
    public void loginPeepbotTest() throws Exception {
        mockMvc.perform(get("/peepuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("peep-user-page"));
    }    

    @Test
    @WithMockUser(username="cooper@peeppea.com", roles={"USER"}) // Simulate authenticated user
    public void loginCooperTest() throws Exception {
        mockMvc.perform(get("/peepuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("peep-user-page"));
    }

    @Test
    public void loginInvalidPeepbotTest() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "peepbot@peeppea.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login-user?error"));
    }

    @Test
    public void loginInvalidCooperTest() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "cooper@peeppea.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login-user?error"));
    }
}
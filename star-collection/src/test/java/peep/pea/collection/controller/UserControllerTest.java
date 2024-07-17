package peep.pea.collection.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import peep.pea.collection.dao.UserRepository;
import peep.pea.collection.beans.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setup() {
        userRepository.findByEmail("peepbot@peeppea.com")
                      .ifPresent(user -> userRepository.delete(user));
        userRepository.findByEmail("cooper@peeppea.com")
                      .ifPresent(user -> userRepository.delete(user));

        // Creating test users for login tests
        User peepbot = new User();
        peepbot.setEmail("peepbot@peeppea.com");
        peepbot.setName("peepbot");
        peepbot.setPassword(passwordEncoder.encode("password"));
        userRepository.save(peepbot);

        User cooper = new User();
        cooper.setEmail("cooper@peeppea.com");
        cooper.setName("cooper");
        cooper.setPassword(passwordEncoder.encode("cooper3"));
        userRepository.save(cooper);
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
    public void loginPeepbotTest() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "peepbot@peeppea.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peepuser"));
    }

    @Test
    public void loginCooperTest() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "cooper@peeppea.com")
                        .param("password", "cooper3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peepuser"));
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
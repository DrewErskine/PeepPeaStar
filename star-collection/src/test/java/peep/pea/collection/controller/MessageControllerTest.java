package peep.pea.collection.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import peep.pea.collection.beans.User;
import peep.pea.collection.dao.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setup() {
        // Ensure users are present for the test
        userRepository.findByEmail("peepbot@peeppea.com")
                      .ifPresent(user -> userRepository.delete(user));
        userRepository.findByEmail("cooper@peeppea.com")
                      .ifPresent(user -> userRepository.delete(user));

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
    @WithMockUser(username="peepbot@peeppea.com", roles={"USER"})
    public void testSendMessageAsPeepbot() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/sendMessage")
                        .param("message", "Hello, Peep Pea community!"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/peepuser"));
    }
    
    @Test
    @WithMockUser(username="cooper@peeppea.com", roles={"USER"})
    public void testSendMessageAsCooper() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/sendMessage")
                        .param("message", "Bark bark!"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/peepuser"));
    }

    @Test
    public void testSendMessageWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/sendMessage")
                        .param("message", "Anonymous message!"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login-user"));
    }
}
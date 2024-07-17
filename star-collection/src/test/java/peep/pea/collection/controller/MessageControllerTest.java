package peep.pea.collection.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import peep.pea.collection.beans.User;
import peep.pea.collection.dao.UserRepository;
import static org.junit.jupiter.api.Assertions.fail;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.findByEmail("peepbot@peeppea.com").ifPresentOrElse(user -> {
            // User exists, no action needed
        }, () -> {
            // User does not exist, create the user
            try {
                mockMvc.perform(post("/saveUser")
                        .param("email", "peepbot@peeppea.com")
                        .param("name", "peepbot")
                        .param("password", "password")
                        .param("confirmPassword", "password")
                        .param("bio", "click clack beep boop")
                        .with(csrf()))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/peepuser"));
            } catch (Exception e) {
                fail("Failed to create peepbot user: " + e.getMessage());
            }
        });
    }

    @Test
    @WithMockUser(username = "peepbot@peeppea.com", roles = "USER")
    public void sendMessageAsPeepbotTest() throws Exception {
        User peepbot = userRepository.findByEmail("peepbot@peeppea.com")
                                     .orElseThrow(() -> new AssertionError("User not found"));
        mockMvc.perform(post("/sendMessage")
                .param("id", String.valueOf(peepbot.getId()))
                .param("message", "Hello from peepbot!")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peepuser"));
    }
}
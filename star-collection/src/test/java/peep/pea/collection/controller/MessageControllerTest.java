package peep.pea.collection.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import peep.pea.collection.beans.Message;
import peep.pea.collection.beans.User;
import peep.pea.collection.dao.UserRepository;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() throws Exception {
        // Ensure the user exists
        User user = userRepository.findByEmail("peepbot@peeppea.com").orElse(null);
        if (user == null) {
            try {
                mockMvc.perform(post("/saveUser")
                        .param("email", "peepbot@peeppea.com")
                        .param("name", "peepbot")
                        .param("password", "password")
                        .param("confirmPassword", "password")
                        .param("bio", "click clack beep boop"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/peepuser"));
                System.out.println("User created: peepbot@peeppea.com");
            } catch (Exception e) {
                fail("Failed to create peepbot user: " + e.getMessage());
            }
        }
    }

    @Test
    @WithMockUser(username = "peepbot@peeppea.com", roles = "USER")
    public void sendMessageAsPeepbotTest() throws Exception {
        User peepbot = userRepository.findByEmail("peepbot@peeppea.com")
                .orElseThrow(() -> new AssertionError("User not found"));

        System.out.println("User found for test: " + peepbot.getEmail());
        mockMvc.perform(post("/sendMessage")
                .param("message", "Hello from peepbot!")
                .flashAttr("messageForm", new Message(peepbot, "Hello from peepbot!")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peepuser"));
    }
}
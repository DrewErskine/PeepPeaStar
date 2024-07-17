package peep.pea.collection.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import peep.pea.collection.dao.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @BeforeEach
        public void setup() {
                userRepository.findByEmail("peepbot@peeppea.com")
                                .ifPresent(user -> userRepository.delete(user));
                userRepository.findByEmail("nala@peeppea.com")
                                .ifPresent(user -> userRepository.delete(user));
                userRepository.findByEmail("cooper@peeppea.com")
                                .ifPresent(user -> userRepository.delete(user));
        }

        @Test
        public void registerUserTest() throws Exception {
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
        public void registerNalaTest() throws Exception {
                mockMvc.perform(post("/saveUser")
                                .param("email", "nala@peeppea.com")
                                .param("name", "nala")
                                .param("password", "nalanap3")
                                .param("confirmPassword", "nalanap3")
                                .param("bio", "nala nap!"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/peepuser"));
        }

        @Test
        public void registerCooperTest() throws Exception {
                mockMvc.perform(post("/saveUser")
                                .param("email", "cooper@peeppea.com")
                                .param("name", "cooper")
                                .param("password", "cooper3")
                                .param("confirmPassword", "cooper3")
                                .param("bio", "cooper bite!"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/peepuser"));
        }
}
package peep.pea.collection.controller;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import peep.pea.collection.beans.Message;
import peep.pea.collection.beans.User;
import peep.pea.collection.dao.MessageRepository;
import peep.pea.collection.dao.UserRepository;

@Controller
public class MessageController {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public MessageController(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/newMessage")
    public String showMessageForm(Model model) {
        model.addAttribute("messageForm", new Message());
        return "peep-user-page";
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@ModelAttribute("messageForm") @Valid Message message, BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Please correct the errors in the form!");
            return "peep-user-page";
        }

        User user = userRepository.findByName(authentication.getName());
        if (user != null) {
            message.setUser(user);
            message.setDateSent(new Date());
            messageRepository.save(message);

            model.addAttribute("statusMessage", "Message sent successfully!");
            model.addAttribute("user", user);
            return "redirect:/peepuser";
        } else {
            return "redirect:/login-user";
        }
    }
}

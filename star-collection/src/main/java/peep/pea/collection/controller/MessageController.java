package peep.pea.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import peep.pea.collection.beans.Message;
import peep.pea.collection.beans.User;
import peep.pea.collection.dao.MessageRepository;
import peep.pea.collection.dao.UserRepository;
import peep.pea.collection.dto.UserMessageDto;

import java.util.Date;
import java.util.Optional;

@Controller
public class MessageController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/newMessage")
    public String showMessageForm(Model model, Authentication authentication) {
        addUserToModel(authentication, model);
        model.addAttribute("peepMessage", new UserMessageDto());
        return "peep-user-page";
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@ModelAttribute("peepMessage") @Valid UserMessageDto messageDto, BindingResult result, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        addUserToModel(authentication, model);
        if (result.hasErrors()) {
            model.addAttribute("error", "Please correct the errors in the form!");
            return "peep-user-page";
        }

        if (authentication == null || authentication.getName() == null) {
            model.addAttribute("error", "User authentication required.");
            return "redirect:/login-user";
        }

        String username = authentication.getName();
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            try {
                User user = optionalUser.get();
                Message message = new Message();
                message.setUser(user);
                message.setMessage(messageDto.getMessage());
                message.setDateSent(new Date());
                messageRepository.save(message);

                redirectAttributes.addFlashAttribute("messageSent", true);
                return "redirect:/getAllBlogs";
            } catch (Exception e) {
                // Log the exception with stack trace
                e.printStackTrace();
                model.addAttribute("error", "An error occurred while sending the message.");
                return "peep-user-page";
            }
        } else {
            model.addAttribute("error", "User not found.");
            return "redirect:/login-user";
        }
    }

    private void addUserToModel(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            Optional<User> optionalUser = userRepository.findByEmail(username);
            optionalUser.ifPresent(user -> model.addAttribute("user", user));
        }
    }
}
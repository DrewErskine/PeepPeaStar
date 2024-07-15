package peep.pea.collection.controller;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import peep.pea.collection.dto.UserRegistrationDto;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageRepository messageRepository;

    public UserController(UserRepository userRepository, MessageRepository messageRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/newUser")
    public String displayRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register-user";
    }

    @GetMapping("/login-user")
    public String displayLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid UserRegistrationDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register-user";
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setDateRegistered(new Date());

        userRepository.save(user);

        model.addAttribute("userSaved", true);
        model.addAttribute("user", user);
        return "peep-user-page";
    }

    @GetMapping("/peepuser")
    public String redirectToPeepUserPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            User user = userRepository.findByName(username);
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("messageForm", new Message());
                return "peep-user-page";
            } else {
                System.out.println("User not found: " + username);
                return "redirect:/login-user";
            }
        } else {
            System.out.println("User is not authenticated");
            return "redirect:/login-user";
        }
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@ModelAttribute("message") @Valid Message message, BindingResult result, Model model,
                              Authentication authentication) {
        if (result.hasErrors()) {
            return "peep-user-page"; // Assuming this is the page from where the message is sent
        }

        User user = userRepository.findByName(authentication.getName());
        if (user != null) {
            message.setUser(user);
            messageRepository.save(message);

            model.addAttribute("messageSent", true);
            model.addAttribute("message", message); // Show the message in the UI
            return "redirect:/peepuser"; 
        } else {
            model.addAttribute("error", "User not authenticated properly.");
            return "redirect:/login-user";
        }
    }
}
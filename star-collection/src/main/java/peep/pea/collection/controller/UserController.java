package peep.pea.collection.controller;

import java.util.Date;
import java.util.List;

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
import peep.pea.collection.dto.UserLoginDto;
import peep.pea.collection.dto.UserMessageDto;
import peep.pea.collection.dto.UserRegistrationDto;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, MessageRepository messageRepository) {
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
        model.addAttribute("loginDto", new UserLoginDto());
        return "login-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid UserRegistrationDto userDto, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "register-user";
        }

        if (!userDto.isPasswordMatching()) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
            return "register-user";
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setDateRegistered(new Date());

        userRepository.save(user);

        model.addAttribute("userSaved", true);
        return "redirect:/peepuser";
    }

    @GetMapping("/peepuser")
    public String redirectToPeepUserPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("peepMessage", new UserMessageDto());
                // Fetch and add messages to the model
                List<Message> messages = messageRepository.findByUserId(user.getId());
                model.addAttribute("messages", messages);
                return "peep-user-page";
            }
        }
        return "redirect:/login-user";
    }

    

    @GetMapping("/account")
    public String showUserAccount(Model model) {
        return "peep-user-page";
    }
}
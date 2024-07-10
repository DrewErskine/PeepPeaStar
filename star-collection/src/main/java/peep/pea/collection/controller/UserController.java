package peep.pea.collection.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import peep.pea.collection.beans.User;
import peep.pea.collection.dao.UserRepository;
import peep.pea.collection.dto.UserRegistrationDto;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/newUser")
    public String displayRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid UserRegistrationDto userDto, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "register-user";
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);

        model.addAttribute("userSaved", true);
        return "redirect:/login";
    }

    @GetMapping("/peepuser-account")
    public String redirectToPeepUserPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            // Redirect to a specific user page if authenticated
            return "redirect:/peep-user-page";
        } else {
            // Redirect to login page if not authenticated
            return "redirect:/register-user";
        }
    }
}
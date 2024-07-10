package peep.pea.collection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "login-user";
    }

    public class UserForm {
        private String username;
        private String password;
    
        // Getters and setters
    }
    
}

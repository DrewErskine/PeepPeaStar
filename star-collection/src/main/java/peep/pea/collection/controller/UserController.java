package peep.pea.collection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import peep.pea.collection.beans.User;
import peep.pea.collection.dao.UserRepository;

@Controller
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    @GetMapping("/newUser")
    public String displayRegisrationForm(Model model){ 
        model.addAttribute("user", new User());
        return "register-user"; 
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult result, Model model){

        if(result.hasErrors()){
            return "register-user";
        }

        User savedUser = userRepository.save(user);
        if(savedUser != null){
            model.addAttribute("name", savedUser.getName());
            model.addAttribute("userSaved", true);
        }
        return "register-user";
    }
}
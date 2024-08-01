package peep.pea.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import peep.pea.collection.beans.User;
import peep.pea.collection.dao.UserRepository;

@Controller
public class CommonController {

    private final UserRepository userRepository;

    public CommonController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void addUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("loggedInUser", user);
            }
        }
    }

    protected UserRepository getUserRepository() {
        return userRepository;
    }
}
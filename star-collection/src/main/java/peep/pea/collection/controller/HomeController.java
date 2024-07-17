package peep.pea.collection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import peep.pea.collection.dao.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends CommonController {

    public HomeController(UserRepository userRepository) {
        super(userRepository);
    }

    @GetMapping("/home")
    public String displayHome(Model model) {
        return "index";
    }

    @GetMapping("/getCharacter/{charname}")
    public String getCharacter(@PathVariable("charname") String charName) {
        return "/peeppea-crew/" + charName;
    }

    @GetMapping("/aboutPeepPea")
    public String aboutPeepPea(Model model) {
        return "about-peeppea";
    }

    @ModelAttribute("currentURI")
    public String getCurrentURI(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
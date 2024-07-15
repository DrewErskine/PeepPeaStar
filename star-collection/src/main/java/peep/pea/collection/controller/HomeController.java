package peep.pea.collection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;



@Controller
public class HomeController {

    @GetMapping("/home")
    public String displayHome(Model model) {
        return "index";
    }

    @GetMapping("/getCharacter/{charname}")
    public String getCharacter(@PathVariable("charname") String charName){
        return "/peeppea-crew/"+charName;
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

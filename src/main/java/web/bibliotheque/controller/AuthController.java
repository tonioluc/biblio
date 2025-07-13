package web.bibliotheque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/se-connecter")
    public String showForm(){
        return "login";
    }

    @GetMapping("/")
    public String showFormByRoot(){
        return "redirect:/se-connecter";
    }
}

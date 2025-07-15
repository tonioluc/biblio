package web.bibliotheque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LivreController {

    @GetMapping("/api/livre")
    public String afficherFormAPI() {
        return "api-livre";
    }
}
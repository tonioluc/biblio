package web.bibliotheque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class GlobalController {
    @GetMapping("/accueil")
    public String showHome(HttpSession session) {
        String roleUserConnecter = (String) session.getAttribute("role");
        if (roleUserConnecter.equalsIgnoreCase("ADHERENT")) {
            return "accueil-adh";
        } else {
            return "accueil-biblio";
        }
    }
}

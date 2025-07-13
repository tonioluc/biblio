package web.bibliotheque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import web.bibliotheque.model.Utilisateur;
import web.bibliotheque.service.UtilisateurService;

@Controller
public class AuthController {
    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/se-connecter")
    public String showForm() {
        return "login";
    }

    @GetMapping("/")
    public String showFormByRoot() {
        return "redirect:/se-connecter";
    }

    @PostMapping("/se-connecter")
    public String traitement(@RequestParam("username") String username, @RequestParam("password") String mdp,
            HttpSession session, Model model) {
        try {
            Utilisateur utilisateur = utilisateurService.checkLogin(username, mdp);
            session.setAttribute("idUser", utilisateur.getIdUtilisateur());
            session.setAttribute("role", utilisateur.getRole());
            return "redirect:/accueil";
        } catch (Exception e) {
            model.addAttribute("erreur", e.getMessage());
            return "login";
        }
    }
}

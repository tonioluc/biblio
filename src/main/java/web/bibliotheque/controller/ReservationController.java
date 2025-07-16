package web.bibliotheque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import web.bibliotheque.service.LivreService;

@Controller
public class ReservationController {
    @Autowired
    private LivreService livreService;

    public void loadForm(Model model) {
        model.addAttribute("livres", livreService.getAll());
    }

    @GetMapping("/demande-reservation")
    public String showForm(Model model) {
        loadForm(model);
        return "demande-reservation"; // Nom du template Thymeleaf à afficher
    }
}

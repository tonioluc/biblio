package web.bibliotheque.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import web.bibliotheque.dto.PretExemplaireDTO;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Exemplaire;
import web.bibliotheque.service.AdherentService;
import web.bibliotheque.service.ExemplaireService;

@Controller
public class ExemplaireController {
    @Autowired
    private AdherentService adherentService;

    @Autowired
    private ExemplaireService exemplaireService;

    @GetMapping("/preter-livre")
    public String afficherFormulaire(Model model){
        List<Adherent> adherents = adherentService.getAllAdherents();
        model.addAttribute("adherents", adherents);
        
        List<Exemplaire> exemplaires = exemplaireService.getAll();
        model.addAttribute("exemplaires", exemplaires);

        model.addAttribute("pretDTO", new PretExemplaireDTO());

        return "preter-livre";
    }
}

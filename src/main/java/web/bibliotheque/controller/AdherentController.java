package web.bibliotheque.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Pret;
import web.bibliotheque.service.AdherentService;
import web.bibliotheque.service.PretService;

@Controller
public class AdherentController {
    @Autowired
    private PretService pretService;
    
    @Autowired
    private AdherentService adherentService;
    @GetMapping("/liste-pret-en-cours")
    public String showListPretEnCours(HttpSession session,Model model){
        Long idAdherent = (Long) session.getAttribute("idUser");
        Adherent adherent = adherentService.getAdherentById(idAdherent).orElse(null);
        List<Pret> prets = pretService.getPretsEnCours(adherent);
        for (Pret pret : prets) {
            System.out.println(pret.getIdPret());
        }
        model.addAttribute("prets", prets);
        return "liste-pret-en-cours";
    }
}

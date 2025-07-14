package web.bibliotheque.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import web.bibliotheque.dto.PretProlongerDto;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Pret;
import web.bibliotheque.model.Utilisateur;
import web.bibliotheque.service.AdherentService;
import web.bibliotheque.service.PretService;
import web.bibliotheque.service.ProlongementService;
import web.bibliotheque.service.UtilisateurService;

@Controller
public class AdherentController {
    @Autowired
    private PretService pretService;

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private ProlongementService prolongementService;

    @Autowired
    private UtilisateurService utilisateurService;

    private void chargerModelListePretEnCours(HttpSession session, Model model) {
        Long idUtilisateur = (Long) session.getAttribute("idUser");
        Utilisateur utilisateur = utilisateurService.getById(idUtilisateur);
        Adherent adherent = utilisateur.getAdherent();
        System.out.println("eto1");
        List<Pret> pretsSansProlongement = pretService.getPretsEnCours(adherent);
        System.out.println("eto2 "+pretsSansProlongement.size());
        List<PretProlongerDto> prets = prolongementService.convertPret(pretsSansProlongement);
        System.out.println("eto3 "+prets.size());
        model.addAttribute("pretsDTO", prets);
    }

    @GetMapping("/liste-pret-en-cours")
    public String showListPretEnCours(HttpSession session, Model model) {
        chargerModelListePretEnCours(session, model);
        return "liste-pret-en-cours";
    }

    @GetMapping("/demande-prolongement/{id}")
    public String prolongerPret(@PathVariable Long id, HttpSession session, Model model) {
        Pret pret = pretService.getPretById(id);
        if (pret != null) {
            boolean isProlonged = prolongementService.pretProlonger(pret);
            if (!isProlonged) {
                try {
                    prolongementService.autoriserAProlonger(pret, (Long) session.getAttribute("idUser"));
                    chargerModelListePretEnCours(session, model);
                    model.addAttribute("message", "Prolongement fait avec success");
                    return "liste-pret-en-cours";
                } catch (Exception e) {
                    chargerModelListePretEnCours(session, model);
                    model.addAttribute("erreur", e.getMessage());
                    return "liste-pret-en-cours";
                }
            }
            chargerModelListePretEnCours(session, model);
            model.addAttribute("erreur", "Pret déjà prolongé");
            return "liste-pret-en-cours";
        }
        chargerModelListePretEnCours(session, model);
        model.addAttribute("erreur", "Pret non trouvé");
        return "liste-pret-en-cours";
    }
}

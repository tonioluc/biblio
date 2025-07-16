package web.bibliotheque.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import web.bibliotheque.model.Pret;
import web.bibliotheque.model.Prolongement;
import web.bibliotheque.service.PretService;
import web.bibliotheque.service.ProlongementService;

@Controller
public class PretController {
    @Autowired
    private ProlongementService prolongementService;

    @Autowired
    private PretService pretService;

    public void loadPretProlonger(Model model) {
        List<Prolongement> prolongements = prolongementService.getAllProlongementAVerifier();
        model.addAttribute("prolongements", prolongements);
    }

    @GetMapping("/liste-pret-prolonger")
    public String listePretsProlonger(Model model) {
        loadPretProlonger(model);
        return "liste-prets-prolonges";
    }

    @GetMapping("/accepter-prolongement/{id}")
    public String accepterProlongement(@PathVariable("id") Long id, Model model) {
        Prolongement prolongement = prolongementService.getById(id);
        prolongementService.accepterProlongement(prolongement);
        loadPretProlonger(model);
        return "liste-prets-prolonges";
    }

    @GetMapping("/refuser-prolongement/{id}")
    public String refuserProlongement(@PathVariable("id") Long id, Model model) {
        Prolongement prolongement = prolongementService.getById(id);
        prolongementService.refuserProlongement(prolongement);
        loadPretProlonger(model);
        model.addAttribute("message", "Prolongement refusé.");
        return "liste-prets-prolonges";
    }

    public void loadPretsEnCours(Model model) {
        List<Pret> prets = pretService.tousLesPretsEnCours();
        model.addAttribute("prets", prets);
    }

    @GetMapping("/liste-prets-en-cours")
    public String afficherListePrets(Model model) {
        loadPretsEnCours(model);
        return "liste-pret-en-cours-biblio";
    }

    @GetMapping("/rendre-livre/{id}")
    public String afficherFormulaire(@PathVariable("id") Long idPret, Model model) {
        model.addAttribute("idPret", idPret);
        model.addAttribute("pret", pretService.getPretById(idPret));
        return "rendre-livre";
    }

    @PostMapping("/rendre-livre/{id}")
    public String rendreLivre(@PathVariable("id") Long idPret,
            @RequestParam("dateDeRetour") LocalDate dateRetourEffective, Model model) {
        boolean estPenalise = pretService.rendreUnLivre(idPret, dateRetourEffective);
        loadPretsEnCours(model);
        String message = "Rendement d'un livre effectué avec succes";
        if (estPenalise) {
            message += " , et pénalisé";
        }
        model.addAttribute("message", message);
        return "liste-pret-en-cours-biblio";
    }

    @GetMapping("/api/adherent")
    public String afficherFormAPI() {
        return "api-adherent";
    }

}

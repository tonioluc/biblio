package web.bibliotheque.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import web.bibliotheque.dto.PretExemplaireDTO;
import web.bibliotheque.model.Livre;
import web.bibliotheque.model.Pret;
import web.bibliotheque.model.TypeDePret;
import web.bibliotheque.model.Utilisateur;
import web.bibliotheque.service.ExemplaireService;
import web.bibliotheque.service.LivreService;
import web.bibliotheque.service.PretService;
import web.bibliotheque.service.TypeDePretService;
import web.bibliotheque.service.UtilisateurService;

@Controller
public class ExemplaireController {
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private TypeDePretService typeDePretService;

    @Autowired
    private PretService pretService;

    @Autowired
    private LivreService livreService;

    private void loadModelForm(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.getByRole("ADHERENT");
        model.addAttribute("utilisateurs", utilisateurs);

        List<Livre> livres = livreService.getAll();
        model.addAttribute("livres", livres);

        List<TypeDePret> typePrets = typeDePretService.getAll();
        model.addAttribute("typePrets", typePrets);

        model.addAttribute("pretDTO", new PretExemplaireDTO());

    }

    @GetMapping("/preter-livre")
    public String afficherFormulaire(Model model) {
        loadModelForm(model);
        return "preter-livre";
    }

    @PostMapping("/preter-livre")
    public String preterExemplaire(@ModelAttribute PretExemplaireDTO pretExemplaireDTO, Model model) {
        try {
            Pret pret = exemplaireService.autoriserAPreter(pretExemplaireDTO);
            pretService.sauvegarder(pret);
            loadModelForm(model);
            model.addAttribute("message", "Pret ajouter avec success");
            return "preter-livre";
        } catch (Exception e) {
            loadModelForm(model);
            model.addAttribute("erreur", e.getMessage());
            return "preter-livre";
        }
    }
}

package web.bibliotheque.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import web.bibliotheque.dto.PretExemplaireDTO;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Exemplaire;
import web.bibliotheque.model.Pret;
import web.bibliotheque.model.Profil;
import web.bibliotheque.model.Utilisateur;
import web.bibliotheque.service.AbonnementService;
import web.bibliotheque.service.AdherentService;
import web.bibliotheque.service.ExemplaireService;
import web.bibliotheque.service.UtilisateurService;

@Controller
public class ExemplaireController {
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private AbonnementService abonnementService;

    @Autowired
    private AdherentService adherentService;

    private void loadModelForm(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.getByRole("ADHERENT");
        model.addAttribute("utilisateurs", utilisateurs);

        List<Exemplaire> exemplaires = exemplaireService.getAll();
        model.addAttribute("exemplaires", exemplaires);

        model.addAttribute("pretDTO", new PretExemplaireDTO());

    }

    @GetMapping("/preter-livre")
    public String afficherFormulaire(Model model) {
        loadModelForm(model);
        return "preter-livre";
    }

    private Pret autoriserAPreter(PretExemplaireDTO pretExemplaireDTO) throws Exception {
        Optional<Exemplaire> exemplaireOpt = exemplaireService.getByRef(pretExemplaireDTO.getRef());
        if (exemplaireOpt.isPresent()) {
            Exemplaire exemplaire = exemplaireOpt.get();
            if (exemplaireService.estDisponible(pretExemplaireDTO.getDateDePret(), exemplaire.getIdExemplaire())) {
                Optional<Utilisateur> utilisateurOpt = utilisateurService
                        .getByUserName(pretExemplaireDTO.getAdherent());
                if (utilisateurOpt.isPresent()) {
                    Utilisateur utilisateur = utilisateurOpt.get();
                    Adherent adherent = utilisateur.getAdherent();
                    if (abonnementService.estAbonnee(adherent, pretExemplaireDTO.getDateDePret())) {
                        Profil profil = adherent.getProfil();
                        int quotaDePret = profil.getQuotaPret();
                        int nombreDePretEnCours = adherentService.nombreDePretEnCours(adherent);
                        if (quotaDePret > nombreDePretEnCours) {
                            
                        } else {
                            throw new Exception("Nombre de quota de pret insuffisant.");
                        }
                    } else {
                        throw new Exception("Adhérent non abonné");
                    }
                } else {
                    throw new Exception("Adhérent n'existe pas");
                }
            } else {
                throw new Exception("Exemplaire non dispo");
            }
        } else {
            throw new Exception("Exemplaire n'existe pas");
        }
    }

    @PostMapping("/preter-livre")
    public String preterExemplaire(@ModelAttribute PretExemplaireDTO pretExemplaireDTO, Model model) {

        return "redirect:/preter-livre";
    }
}

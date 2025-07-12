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
import web.bibliotheque.model.Utilisateur;
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
    private AdherentService adherentService;

    @GetMapping("/preter-livre")
    public String afficherFormulaire(Model model){
        List<Utilisateur> utilisateurs = utilisateurService.getByRole("ADHERENT");
        model.addAttribute("utilisateurs", utilisateurs);
        
        List<Exemplaire> exemplaires = exemplaireService.getAll();
        model.addAttribute("exemplaires", exemplaires);

        model.addAttribute("pretDTO", new PretExemplaireDTO());

        return "preter-livre";
    }

    @PostMapping("/preter-livre")
    public String preterExemplaire(@ModelAttribute PretExemplaireDTO pretExemplaireDTO ,Model model){
        Optional<Exemplaire> exemplaireOpt = exemplaireService.getByRef(pretExemplaireDTO.getRef());
        if (exemplaireOpt.isPresent()) {
            Exemplaire exemplaire = exemplaireOpt.get();
            if (exemplaireService.estDisponible(pretExemplaireDTO.getDateDePret(), exemplaire.getIdExemplaire())) {
                System.out.println("exemplaire dispo");

                Optional<Utilisateur> utilisateurOpt = utilisateurService.getByUserName(pretExemplaireDTO.getAdherent());
                if (utilisateurOpt.isPresent()) {
                    System.out.println("adhérent existe");
                    Utilisateur utilisateur = utilisateurOpt.get();
                    Adherent adherent = utilisateur.getAdherent();
                    
                }else{
                    System.out.println("Adhérent n'existe pas");
                }
            }else{
                System.out.println("Exemplaire non dispo");
            }
        }else{
            System.out.println("Exemplaire n'existe pas");
        }
        return "redirect:/preter-livre";
    }
}

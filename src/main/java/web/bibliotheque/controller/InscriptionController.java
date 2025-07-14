package web.bibliotheque.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import web.bibliotheque.dto.InscriptionAdherentDTO;
import web.bibliotheque.model.Abonnement;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Profil;
import web.bibliotheque.model.Utilisateur;
import web.bibliotheque.service.*;

@Controller
public class InscriptionController {

    @Autowired
    private ProfilService profilService;
    @Autowired
    private AdherentService adherentService;
    @Autowired
    private AbonnementService abonnementService;
    @Autowired 
    private UtilisateurService utilisateurService;

    @GetMapping("/inscription")
    public String afficherPageInscription(Model model) {
        List<Profil> profils = profilService.getAll();
        model.addAttribute("profils", profils);
        InscriptionAdherentDTO inscriptionAdherentDTO = new InscriptionAdherentDTO();
        inscriptionAdherentDTO.setNom("RANDRIA");
        inscriptionAdherentDTO.setPrenom("Antonio");
        inscriptionAdherentDTO.setUsername("tonioluc");
        model.addAttribute("inscriptionDTO",inscriptionAdherentDTO);
        return "inscription";
    }

    @Transactional
    @PostMapping("/inscrire-adherent")
    public String inscrireAdherent(@ModelAttribute InscriptionAdherentDTO dto, Model model) {
        try {
            Optional<Profil> profilOpt = profilService.getById(dto.getTypeCompte());
            if (profilOpt.isEmpty()) {
                model.addAttribute("errorMessage", "Profil non trouvé pour l'ID : " + dto.getTypeCompte());
                return "inscription";
            }

            Profil profil = profilOpt.get();
            
            Adherent adherent = new Adherent();
            adherent.setDateNaissance(dto.getDateNaissance());
            adherent.setNom(dto.getNom());
            adherent.setPrenom(dto.getPrenom());
            adherent.setProfil(profil);
            
            adherentService.save(adherent);

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setAdherent(adherent);
            utilisateur.setMotDePasse(dto.getPassword());
            utilisateur.setNomUtilisateur(dto.getUsername());
            utilisateur.setRole("ADHERENT");
            utilisateurService.save(utilisateur);

            Abonnement abonnement = new Abonnement();
            abonnement.setDateDebut(dto.getDateDebutAbonnement());
            abonnement.setDateExpiration(dto.getDateExpirationAbonnement());
            abonnement.setAdherent(adherent);
            abonnementService.save(abonnement);

            return "redirect:/inscription?success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'inscription : " + e.getMessage());
            return "redirect:/inscription?error";
        }
    }

}

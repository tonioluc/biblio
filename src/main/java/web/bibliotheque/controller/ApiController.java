package web.bibliotheque.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.bibliotheque.dto.IdDto;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Profil;
import web.bibliotheque.service.AbonnementService;
import web.bibliotheque.service.AdherentService;
import web.bibliotheque.service.PenaliteService;

@RestController

public class ApiController {
    @Autowired
    AdherentService adherentService;

    @Autowired
    AbonnementService abonnementService;

    @Autowired
    PenaliteService penaliteService;

    @PostMapping("/api/adherent")
    public Map<String, Object> endPointAdherent(@RequestParam("id") Long id) {
        // Long id = idDto.getId();
        Map<String, Object> response = new HashMap<>();
        Optional<Adherent> adherentOpt = adherentService.getAdherentById(id);
        if (!adherentOpt.isPresent()) {
            response.put("error", "Adherent non trouvé");
            return response;
        }
        Adherent adherent = adherentOpt.get();
        Profil profil = adherent.getProfil();
        response.put("idAdherent", id);
        response.put("nom", adherent.getNom());
        response.put("prenom", adherent.getPrenom());
        response.put("est abonnée", abonnementService.estAbonnee(adherent, LocalDate.now()));
        response.put("nombre de pret en cours", adherentService.nombreDePretEnCours(adherent));
        response.put("reste quota de prêt", profil.getQuotaPret() - adherentService.nombreDePretEnCours(adherent));
        boolean estPenalise = adherentService.estPenalise(adherent, LocalDate.now());
        if (estPenalise) {
            response.put("est pénalisé", Boolean.toString(estPenalise) + ". Dernière pénalité : "
            + penaliteService.getDernierByAdherent(adherent));
        } else {
            response.put("est pénalisé", estPenalise);
        }
        return response;
    }
}

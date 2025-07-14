package web.bibliotheque.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.dto.PretProlongerDto;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Pret;
import web.bibliotheque.model.Profil;
import web.bibliotheque.model.Prolongement;
import web.bibliotheque.model.Utilisateur;
import web.bibliotheque.repository.ProlongementRepostirory;

@Service
public class ProlongementService {
    @Autowired
    private ProlongementRepostirory prolongementRepostirory;

    @Autowired
    private AbonnementService abonnementService;

    @Autowired
    private UtilisateurService utilisateurService;

    public boolean pretProlonger(Pret pret) {
        return prolongementRepostirory.findByPret(pret).isPresent();
    }

    public List<PretProlongerDto> convertPret(List<Pret> prets) {
        List<PretProlongerDto> result = new ArrayList<>();
        for (Pret pret : prets) {
            PretProlongerDto pretProlongerDto = new PretProlongerDto();
            pretProlongerDto.setPret(pret);
            pretProlongerDto.setProlonger(pretProlonger(pret));
            result.add(pretProlongerDto);
        }
        return result;
    }

    public int nombreQuotaUtilise(Long idAdherent) {
        return prolongementRepostirory.nombreQuotaUtilise(idAdherent);
    }

    public void sauvegarder(Pret pret, Long idUtilisateur) throws Exception {
        Utilisateur utilisateur = utilisateurService.getById(idUtilisateur);
        Adherent adherent = utilisateur.getAdherent();
        Profil profil = adherent.getProfil();
        if (!abonnementService.estAbonnee(adherent, LocalDate.now())) {
            throw new Exception("Adhérent non abonnée"); // Vérifié
        }
        int quotaProlongement = profil.getQuotaProlongement();
        int quotaUtilise = this.nombreQuotaUtilise(adherent.getIdAdherent());
        if (quotaProlongement <= quotaUtilise) {
            throw new Exception("Quota de prolongement atteint pour l'adhérent , utilisé : " + quotaUtilise); // Vérifié
        }
        Prolongement prolongement = new Prolongement();
        prolongement.setPret(pret);
        prolongement.setDateRetourApresProlongement(pret.getDateRetourPrevue().plusDays(profil.getDurreeDePret()));
        prolongementRepostirory.save(prolongement);
    }

    public List<Prolongement> getAll() {
        return prolongementRepostirory.findAll();
    }
}

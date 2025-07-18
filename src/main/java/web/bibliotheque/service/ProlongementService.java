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

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private JourFerieService jourFerieService;

    @Autowired
    private PretService pretService;

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

    public void sauvergarder(Prolongement prolongement){
        prolongementRepostirory.save(prolongement);
    }


    public void autoriserAProlonger(Pret pret, Long idUtilisateur) throws Exception {
        Utilisateur utilisateur = utilisateurService.getById(idUtilisateur);
        Adherent adherent = utilisateur.getAdherent();
        Profil profil = adherent.getProfil();
        LocalDate dateApresProlongement = pret.getDateRetourPrevue().plusDays(profil.getDurreeDePret());
        if (!abonnementService.estAbonnee(adherent, dateApresProlongement)) {
            throw new Exception("Adhérent non abonnée"); // Vérifié
        }
        int quotaProlongement = profil.getQuotaProlongement();
        int quotaUtilise = this.nombreQuotaUtilise(adherent.getIdAdherent());
        if (quotaProlongement <= quotaUtilise) {
            throw new Exception("Quota de prolongement atteint pour l'adhérent , utilisé : " + quotaUtilise); // Vérifié
        }
        if(adherentService.estPenalise(adherent , dateApresProlongement)){
            throw new Exception("Vous êtes pénalisé , vous ne pouver pas faire un prolongement");
        }
        Prolongement prolongement = new Prolongement();
        prolongement.setPret(pret);
        prolongement.setDateRetourApresProlongement(dateApresProlongement);
        prolongement.setAccepted(false);
        prolongement.setChecked(false);
        prolongementRepostirory.save(prolongement);
    }

    public List<Prolongement> getAll() {
        return prolongementRepostirory.findAll();
    }

    public List<Prolongement> getAllProlongementAVerifier(){
        List<Prolongement> result = new ArrayList<>();
        for (Prolongement prolongement : this.getAll()) {
            if (!prolongement.isChecked()) {
                result.add(prolongement);
            }
        }
        return result;
    }

    public Prolongement getById(Long idProlongement){
        return prolongementRepostirory.findById(idProlongement).orElse(null);
    }

    public void accepterProlongement(Prolongement prolongement){
        Pret pret = prolongement.getPret();
        LocalDate dateRetourPrevue = pret.getDateRetourPrevue();
        pret.setDateRetourPrevue(jourFerieService.getDateActif(dateRetourPrevue));
        pretService.updatePret(pret);
        prolongement.setAccepted(true);
        prolongement.setChecked(true);
        this.sauvergarder(prolongement);
    }

    public void refuserProlongement(Prolongement prolongement){
        prolongement.setChecked(true);
        this.sauvergarder(prolongement);
    }
}

package web.bibliotheque.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.dto.PretExemplaireDTO;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Exemplaire;
import web.bibliotheque.model.Pret;
import web.bibliotheque.model.Profil;
import web.bibliotheque.model.Utilisateur;
import web.bibliotheque.repository.ExemplaireRepository;

@Service
public class ExemplaireService {
    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private AbonnementService abonnementService;

    @Autowired
    private AdherentService adherentService;

    public List<Exemplaire> getAll() {
        return exemplaireRepository.findAll();
    }

    public boolean estDisponible(LocalDate date_de_pret, Long id_exemplaire) {
        return (exemplaireRepository.nombreDePret(date_de_pret, id_exemplaire) == 0);
    }

    public Optional<Exemplaire> getByRef(String ref) {
        return exemplaireRepository.findByRef(ref);
    }

    private Pret autoriserAPreter(PretExemplaireDTO pretExemplaireDTO) throws Exception {
        Optional<Exemplaire> exemplaireOpt = this.getByRef(pretExemplaireDTO.getRef());
        if (exemplaireOpt.isPresent()) {
            Exemplaire exemplaire = exemplaireOpt.get();
            if (this.estDisponible(pretExemplaireDTO.getDateDePret(), exemplaire.getIdExemplaire())) {
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
                            if (!adherentService.estPenalise(adherent)) {
                                int ageAdherent = LocalDate.now().getYear() - adherent.getDateNaissance().getYear();
                                int ageRequis = exemplaire.getRestriction_age();
                                if (ageAdherent >= ageRequis) {
                                    
                                } else {
                                    throw new Exception("L'adhérent ne peut pas prêter ce livre , age requis : "
                                            + ageRequis + " age d'adhérent : " + ageAdherent);
                                }
                            } else {
                                throw new Exception("Adhérent pénalisé.");
                            }
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
}

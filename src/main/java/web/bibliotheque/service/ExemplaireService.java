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
import web.bibliotheque.model.TypeDePret;
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

    @Autowired
    private TypeDePretService typeDePretService;

    public List<Exemplaire> getAll() {
        return exemplaireRepository.findAll();
    }

    public boolean estDisponible(LocalDate date_de_pret, Long id_exemplaire) {
        return (exemplaireRepository.nombreDePret(date_de_pret, id_exemplaire) == 0);
    }

    public Optional<Exemplaire> getByRef(String ref) {
        return exemplaireRepository.findByRef(ref);
    }

    public boolean estReserver(LocalDate date, Exemplaire exemplaire) {
        return exemplaireRepository.estReserver(exemplaire.getIdExemplaire(), date) > 0;
    }

    public Pret autoriserAPreter(PretExemplaireDTO pretExemplaireDTO) throws Exception {
        Optional<Exemplaire> exemplaireOpt = this.getByRef(pretExemplaireDTO.getRef());
        if (exemplaireOpt.isPresent()) {
            Exemplaire exemplaire = exemplaireOpt.get();
            if (this.estDisponible(pretExemplaireDTO.getDateDePret(), exemplaire.getIdExemplaire())
                    && !this.estReserver(pretExemplaireDTO.getDateDePret(), exemplaire)) {
                Optional<Utilisateur> utilisateurOpt = utilisateurService
                        .getByUserName(pretExemplaireDTO.getAdherent());
                if (utilisateurOpt.isPresent()) {
                    Utilisateur utilisateur = utilisateurOpt.get();
                    Adherent adherent = utilisateur.getAdherent();
                    LocalDate datePret = pretExemplaireDTO.getDateDePret();
                    if (abonnementService.estAbonnee(adherent, datePret)) {
                        Profil profil = adherent.getProfil();
                        int quotaDePret = profil.getQuotaPret();
                        int nombreDePretEnCours = adherentService.nombreDePretEnCours(adherent);
                        if (quotaDePret > nombreDePretEnCours) {
                            if (!adherentService.estPenalise(adherent , datePret)) {
                                int ageAdherent = LocalDate.now().getYear() - adherent.getDateNaissance().getYear();
                                int ageRequis = exemplaire.getRestriction_age();
                                if (ageAdherent >= ageRequis) {
                                    Pret pret = new Pret();
                                    pret.setAdherent(adherent);
                                    pret.setDateDePret(datePret);
                                    TypeDePret typeDePret = typeDePretService
                                            .findById((long) pretExemplaireDTO.getTypePret()).get();
                                    pret.setTypeDePret(typeDePret);
                                    int durreeDePret = profil.getDurreeDePret();
                                    if (pretExemplaireDTO.getTypePret() == 1) {
                                        durreeDePret = 0;
                                        System.out.println("tafiditra");
                                    }
                                    pret.setDateRetourPrevue(datePret.plusDays(durreeDePret));

                                    pret.setExemplaire(exemplaire);
                                    pret.setDateRetourEffective(null);
                                    return pret;
                                } else {
                                    throw new Exception("L'adhérent ne peut pas prêter ce livre . Age requis : "
                                            + ageRequis + " age d'adhérent : " + ageAdherent); // verifier
                                }
                            } else {
                                throw new Exception("Adhérent pénalisé.");
                            }
                        } else {
                            throw new Exception("Nombre de quota de pret insuffisant."); // vérifier
                        }
                    } else {
                        throw new Exception("Adhérent non abonné"); // verifier
                    }
                } else {
                    throw new Exception("Adhérent n'existe pas"); // verifier
                }
            } else {
                throw new Exception("Exemplaire non dispo"); // verifier
            }
        } else {
            throw new Exception("Exemplaire n'existe pas"); // verifier
        }
    }
}

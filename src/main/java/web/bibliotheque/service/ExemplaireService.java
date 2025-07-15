package web.bibliotheque.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.dto.PretExemplaireDTO;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Exemplaire;
import web.bibliotheque.model.HistoriqueStatutExemplaire;
import web.bibliotheque.model.Livre;
import web.bibliotheque.model.Pret;
import web.bibliotheque.model.Profil;
import web.bibliotheque.model.RestrictionAge;
import web.bibliotheque.model.StatutExemplaire;
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
    private HistoriqueStatutExemplaireService historiqueStatutExemplaireService;

    @Autowired
    private TypeDePretService typeDePretService;

    @Autowired
    private LivreService livreService;

    @Autowired
    private RestrictionAgeService restrictionAgeService;

    @Autowired
    private StatutExemplaireService statutExemplaireService;

    public List<Exemplaire> getAll() {
        return exemplaireRepository.findAll();
    }

    public boolean estDisponible(LocalDate date_de_pret, Long id_exemplaire) {
        return (exemplaireRepository.nombreDePret(date_de_pret, id_exemplaire) == 0);
    }

    public boolean estReserver(LocalDate date, Exemplaire exemplaire) {
        return exemplaireRepository.estReserver(exemplaire.getIdExemplaire(), date) > 0;
    }

    public List<Exemplaire> getExemplairesDisponibles(Livre livre, LocalDate date) {
        List<Exemplaire> exemplaires = exemplaireRepository.findByLivre(livre);
        List<Exemplaire> exemplairesDisponibles = new ArrayList<>();

        for (Exemplaire ex : exemplaires) {
            List<HistoriqueStatutExemplaire> historiques = historiqueStatutExemplaireService
                    .getDernierStatutAvantDate(ex.getIdExemplaire(), date);

            if (historiques.isEmpty()) {
                exemplairesDisponibles.add(ex); // Pas de statut = disponible
            } else {
                HistoriqueStatutExemplaire dernier = historiques.get(0); // Le plus récent avant la date
                if (dernier.getStatutExemplaire().getLibelle().equalsIgnoreCase("Disponible")) {
                    exemplairesDisponibles.add(ex);
                }
            }
        }

        return exemplairesDisponibles;
    }

    public Pret autoriserAPreter(PretExemplaireDTO pretExemplaireDTO) throws Exception {
        Optional<Livre> livreOpt = livreService.getById(pretExemplaireDTO.getIdLivre());
        if (livreOpt.isPresent()) {
            Livre livre = livreOpt.get();
            List<Exemplaire> exemplairesDispo = this.getExemplairesDisponibles(livre,
                    pretExemplaireDTO.getDateDePret());
            if (exemplairesDispo.size() > 0) {
                Exemplaire exemplaire = exemplairesDispo.get(0);
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
                            if (!adherentService.estPenalise(adherent, datePret)) {
                                int ageAdherent = LocalDate.now().getYear() -
                                        adherent.getDateNaissance().getYear();
                                Optional<RestrictionAge> restrictionOpt = restrictionAgeService.getByLivre(livre);
                                int ageRequis = 0;
                                if (restrictionOpt.isPresent()) {
                                    ageRequis = restrictionOpt.get().getAge();
                                }

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
                                    }
                                    LocalDate dateRetourPrevue = pretExemplaireDTO.getDateDeRetour();
                                    pret.setDateRetourPrevue(dateRetourPrevue);
                                    if (dateRetourPrevue == null || pretExemplaireDTO.getTypePret() == 1) {
                                        pret.setDateRetourPrevue(datePret.plusDays(durreeDePret));
                                    } else if (dateRetourPrevue.isBefore(datePret)) {
                                        throw new Exception(
                                                "La date de retour prévu ne dois pas être avant la date de prêt");
                                    }

                                    pret.setExemplaire(exemplaire);
                                    pret.setDateRetourEffective(null);
                                    HistoriqueStatutExemplaire historiqueStatutExemplaire = new HistoriqueStatutExemplaire();
                                    historiqueStatutExemplaire.setDateChangement(datePret);
                                    historiqueStatutExemplaire.setExemplaire(exemplaire);
                                    StatutExemplaire statutExemplaire = statutExemplaireService.getById((long) 2);
                                    historiqueStatutExemplaire.setStatutExemplaire(statutExemplaire);
                                    historiqueStatutExemplaireService.create(historiqueStatutExemplaire);
                                    return pret;
                                } else {
                                    throw new Exception("L'adhérent ne peut pas prêter ce livre . Age requis : "
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
                throw new Exception("Livre non disponible");
            }
        } else {
            throw new Exception("Livre n'existe pas");
        }
    }
}

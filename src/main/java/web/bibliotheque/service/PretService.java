package web.bibliotheque.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Penalite;
import web.bibliotheque.model.Pret;
import web.bibliotheque.repository.PretRepository;

@Service
public class PretService {
    @Autowired
    PretRepository pretRepository;

    @Autowired
    PenaliteService penaliteService;

    public void sauvegarder(Pret pret) {
        pretRepository.save(pret);
    }

    public List<Pret> getByAdherent(Adherent adherent) {
        Long idAdherent = adherent.getIdAdherent();
        System.out.println("idAdherent : " + idAdherent);
        return pretRepository.findByIdAdherent(idAdherent);
    }

    public List<Pret> getPretsEnCours(Adherent adherent) {
        List<Pret> result = new ArrayList<>();

        for (Pret pret : this.getByAdherent(adherent)) {
            if (pret.getDateRetourEffective() == null) {
                result.add(pret);
            }
        }
        return result;
    }

    public Pret getPretById(Long id) {
        return pretRepository.findById(id).orElse(null);
    }

    public void updatePret(Pret pret) {
        pretRepository.save(pret);
    }

    public List<Pret> tousLesPretsEnCours() {
        return pretRepository.tousLesPretsEnCours();
    }

    public boolean rendreUnLivre(Long idPret, LocalDate dateDeRetourEffective) {
        boolean result = false;
        Pret pret = this.getPretById(idPret);
        LocalDate dateRetourPrevue = pret.getDateRetourPrevue();
        pret.setDateRetourEffective(dateDeRetourEffective);
        if (dateRetourPrevue.isBefore(dateDeRetourEffective)) {
            Adherent adherent = pret.getAdherent();
            LocalDate dateDeDebutPenalite = penaliteService.getDateDebutPenalite(dateDeRetourEffective, adherent.getIdAdherent());
            Penalite penalite = new Penalite();
            penalite.setAdherent(adherent);
            penalite.setDateDebut(dateDeDebutPenalite);
            penalite.setDateFin(dateDeDebutPenalite.plusDays(adherent.getProfil().getDurreePenalite()));
            penalite.setResolu(false);
            penaliteService.create(penalite);
            result = true;
        }
        this.updatePret(pret);
        return result;
    }

}

package web.bibliotheque.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Penalite;
import web.bibliotheque.repository.PenaliteRepository;

@Service
public class PenaliteService {
    @Autowired
    private PenaliteRepository penaliteRepository;

    public LocalDate getDateDebutPenalite(LocalDate dateRetourEffective ,Long idAdherent){
        Optional<Penalite> penaliteOptional = penaliteRepository.findMaxDatePenaliteByIdAdherent(idAdherent);
        if (!penaliteOptional.isPresent()) {
            return dateRetourEffective;       
        }
        Penalite penalite = penaliteOptional.get();
        LocalDate dateFinPenalite = penalite.getDateFin();
        if (dateRetourEffective.isBefore(dateFinPenalite)) {
            return dateFinPenalite;
        }
        return dateRetourEffective;
    }

    public void create(Penalite penalite){
        penaliteRepository.save(penalite);
    }

    public Penalite getDernierByAdherent(Adherent adherent){
        List<Penalite> penalites = penaliteRepository.findByAdherentOrderByDateFinDesc(adherent);
        if (penalites.isEmpty()) {
            return null;
        }
        return penalites.get(0);
    }
}

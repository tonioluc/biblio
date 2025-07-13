package web.bibliotheque.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.Abonnement;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.repository.AbonnementRepository;

@Service
public class AbonnementService {
    @Autowired
    AbonnementRepository abonnementRepository;

    public void save(Abonnement abonnement) {
        abonnementRepository.save(abonnement);
    }

    public Abonnement getAbonnementById(Long id) {
        return abonnementRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        abonnementRepository.deleteById(id);
    }

    public boolean estAbonnee(Adherent adherent, LocalDate dateDePret) {
        return abonnementRepository.estAbonne(adherent.getIdAdherent(), dateDePret) > 0;
    }
}

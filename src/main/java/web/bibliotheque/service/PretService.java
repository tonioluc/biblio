package web.bibliotheque.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Pret;
import web.bibliotheque.repository.PretRepository;

@Service
public class PretService {
    @Autowired
    PretRepository pretRepository;

    // @Autowired
    // AdherentService adherentService;

    public void sauvegarder(Pret pret) {
        pretRepository.save(pret);
    }

    public List<Pret> getByAdherent(Adherent adherent) {
        return pretRepository.findByAdherent(adherent);
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

}

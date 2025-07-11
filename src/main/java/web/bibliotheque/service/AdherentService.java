package web.bibliotheque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.bibliotheque.model.Adherent;
import web.bibliotheque.repository.AdherentRepository;
import java.util.List;
import java.util.Optional;
@Service
public class AdherentService {

    @Autowired
    private AdherentRepository adherentRepository;

    public void save(Adherent adherent) {
        adherentRepository.save(adherent);
    }

    public Optional<Adherent> getAdherentById(Long id) {
        return adherentRepository.findById(id);
    }

    public List<Adherent> getAllAdherents() {
        return adherentRepository.findAll();
    }

    public void delete(Long id) {
        adherentRepository.deleteById(id);
    }

}
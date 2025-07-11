package web.bibliotheque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.bibliotheque.model.Profil;
import web.bibliotheque.repository.ProfilRepository;
import java.util.List;
import java.util.Optional;
@Service
public class ProfilService {

    @Autowired
    private ProfilRepository ProfilRepository;

    public void save(Profil Profil) {
        ProfilRepository.save(Profil);
    }

    public Optional<Profil> getById(Long id) {
        return ProfilRepository.findById(id);
    }

    public List<Profil> getAll() {
        return ProfilRepository.findAll();
    }

    public void delete(Long id) {
        ProfilRepository.deleteById(id);
    }

}

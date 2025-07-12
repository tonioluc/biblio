package web.bibliotheque.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.Exemplaire;
import web.bibliotheque.repository.ExemplaireRepository;

@Service
public class ExemplaireService {
    @Autowired
    private ExemplaireRepository exemplaireRepository;

    public List<Exemplaire> getAll() {
        return exemplaireRepository.findAll();
    }

    public boolean estDisponible(LocalDate date_de_pret, Long id_exemplaire) {
        return (exemplaireRepository.nombreDePret(date_de_pret, id_exemplaire) == 0);
    }

    public Optional<Exemplaire> getByRef(String ref){
        return exemplaireRepository.findByRef(ref);
    }
}

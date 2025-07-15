package web.bibliotheque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.StatutExemplaire;

import web.bibliotheque.repository.StatutExemplaireRepository;

@Service
public class StatutExemplaireService {
    @Autowired
    private StatutExemplaireRepository statutExemplaireRepository;
    public StatutExemplaire getByLibelle(String libelle){
        return statutExemplaireRepository.findByLibelle(libelle).get();
    }

    public StatutExemplaire getById(Long id){
        return statutExemplaireRepository.findById(id).get();
    }
}

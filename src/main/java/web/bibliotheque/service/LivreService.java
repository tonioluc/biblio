package web.bibliotheque.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.Livre;
import web.bibliotheque.repository.LivreRepository;

@Service
public class LivreService {
    @Autowired
    private LivreRepository livreRepository;

    public List<Livre> getAll(){
        return livreRepository.findAll();
    }

    public Optional<Livre> getById(Long id){
        return livreRepository.findById(id);
    }
}

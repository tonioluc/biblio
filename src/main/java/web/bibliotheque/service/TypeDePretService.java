package web.bibliotheque.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.TypeDePret;
import web.bibliotheque.repository.TypeDePretRepository;

@Service
public class TypeDePretService {
    @Autowired
    private TypeDePretRepository typeDePretRepository;
    public List<TypeDePret> getAll(){
        return typeDePretRepository.findAll();
    }
    public Optional<TypeDePret> findById(Long id){
        return typeDePretRepository.findById(id);
    }
}

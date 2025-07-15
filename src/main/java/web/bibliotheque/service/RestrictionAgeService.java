package web.bibliotheque.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.Livre;
import web.bibliotheque.model.RestrictionAge;
import web.bibliotheque.repository.RestrcitionAgeRepository;

@Service
public class RestrictionAgeService {
    @Autowired
    private RestrcitionAgeRepository restrcitionAgeRepository;

    public Optional<RestrictionAge> getByLivre(Livre livre) {
        return restrcitionAgeRepository.findByLivre(livre);
    }
}

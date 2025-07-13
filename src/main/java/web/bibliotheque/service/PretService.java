package web.bibliotheque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.Pret;
import web.bibliotheque.repository.PretRepository;

@Service
public class PretService {
    @Autowired
    PretRepository pretRepository;

    public void sauvegarder(Pret pret) {
        pretRepository.save(pret);
    }
}

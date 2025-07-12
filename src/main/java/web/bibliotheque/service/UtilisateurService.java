package web.bibliotheque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.Utilisateur;
import web.bibliotheque.repository.UtilisateurRepository;

@Service
public class UtilisateurService {
    @Autowired
    UtilisateurRepository utilisateurRepository;
    public void save(Utilisateur utilisateur){
        utilisateurRepository.save(utilisateur);
    }
}

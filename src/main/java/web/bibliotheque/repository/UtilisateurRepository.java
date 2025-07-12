package web.bibliotheque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.bibliotheque.model.Utilisateur;

// UtilisateurRepository.java
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    public List<Utilisateur> findByRole(String role);
}
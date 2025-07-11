package web.bibliotheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.bibliotheque.model.Utilisateur;

// UtilisateurRepository.java
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
}
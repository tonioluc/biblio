package web.bibliotheque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import web.bibliotheque.model.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    public List<Utilisateur> findByRole(String role);
    public Optional<Utilisateur> findByNomUtilisateur(String username);
    public Optional<Utilisateur> findByNomUtilisateurAndMotDePasse(String username , String password);
}
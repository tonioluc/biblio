package web.bibliotheque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.bibliotheque.model.StatutExemplaire;

@Repository
public interface StatutExemplaireRepository extends JpaRepository<StatutExemplaire,Long> {
    public Optional<StatutExemplaire> findByLibelle(String libelle);
}

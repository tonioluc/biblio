package web.bibliotheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.bibliotheque.model.JourFerie;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Long> {
    
}

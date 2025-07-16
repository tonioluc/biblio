package web.bibliotheque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.bibliotheque.model.JourFerie;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Long> {
    public List<JourFerie> findAllByOrderByDateJourAsc();
}

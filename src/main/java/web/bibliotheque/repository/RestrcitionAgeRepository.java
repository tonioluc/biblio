package web.bibliotheque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.bibliotheque.model.Livre;
import web.bibliotheque.model.RestrictionAge;

@Repository
public interface RestrcitionAgeRepository extends JpaRepository<RestrictionAge, Long> {
    public Optional<RestrictionAge> findByLivre(Livre livre);
}

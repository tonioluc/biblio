package web.bibliotheque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Pret;

public interface PretRepository extends JpaRepository<Pret, Long> {
    public List<Pret> findByAdherent(Adherent adherent);
}

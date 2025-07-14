package web.bibliotheque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import web.bibliotheque.model.Pret;
import web.bibliotheque.model.Prolongement;

public interface ProlongementRepostirory extends JpaRepository<Prolongement,Long>{
    public Optional<Prolongement> findByPret(Pret pret);
}

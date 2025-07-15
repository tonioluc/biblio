package web.bibliotheque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import web.bibliotheque.model.Adherent;
import web.bibliotheque.model.Penalite;

public interface PenaliteRepository extends JpaRepository<Penalite, Long> {
    @Query(value = "select * from penalite where date_fin = (select max(date_fin) from penalite where id_adherent = :id and resolu = false)", nativeQuery = true)
    public Optional<Penalite> findMaxDatePenaliteByIdAdherent(@Param("id") Long idAdherent);
    public List<Penalite> findByAdherentOrderByDateFinDesc(Adherent adherent);
}

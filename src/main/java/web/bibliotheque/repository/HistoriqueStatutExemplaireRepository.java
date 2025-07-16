package web.bibliotheque.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import web.bibliotheque.model.Exemplaire;
import web.bibliotheque.model.HistoriqueStatutExemplaire;

@Repository
public interface HistoriqueStatutExemplaireRepository extends JpaRepository<HistoriqueStatutExemplaire, Long> {
    @Query("SELECT h FROM HistoriqueStatutExemplaire h WHERE h.exemplaire.id = :idExemplaire AND h.dateChangement <= :date ORDER BY h.dateChangement DESC")
    List<HistoriqueStatutExemplaire> findDernierStatutAvantDate(@Param("idExemplaire") Long idExemplaire,
            @Param("date") LocalDate date);

    List<HistoriqueStatutExemplaire> findByExemplaireOrderByDateChangementDesc(Exemplaire exemplaire);
}

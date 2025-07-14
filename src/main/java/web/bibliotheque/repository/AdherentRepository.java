package web.bibliotheque.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import web.bibliotheque.model.Adherent;

public interface AdherentRepository extends JpaRepository<Adherent, Long> {
    @Query(value = "SELECT count(*) from pret where id_adherent = :id and date_retour_effective is null", nativeQuery = true)
    int nombreDePretEnCours(@Param("id") Long idAdherent);

    @Query(value = "SELECT count(*) from penalite where id_adherent = :id and resolu = FALSE and date_debut <= :date and :date <= date_fin", nativeQuery = true)
    int estPenalise(@Param("id") Long idAdherent , @Param("date") LocalDate date);
}

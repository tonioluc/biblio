package web.bibliotheque.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import web.bibliotheque.model.Abonnement;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    @Query(value = "SELECT count(*) from abonnement where date_debut <= :date and :date <= date_expiration and id_adherent = :id",nativeQuery = true)
    public int estAbonne(@Param("id") Long idAdherent,@Param("date") LocalDate dateDePret);
}


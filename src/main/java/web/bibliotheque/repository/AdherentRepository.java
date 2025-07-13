package web.bibliotheque.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import web.bibliotheque.model.Adherent;

public interface AdherentRepository extends JpaRepository<Adherent, Long> {
    @Query(value = "SELECT count(*) from pret where id_adherent = :id and date_retour_effective is null",nativeQuery = true)
    int nombreDePretEnCours(@Param("id") Long idAdherent);
}

package web.bibliotheque.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import web.bibliotheque.model.Exemplaire;

public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
    @Query(value = "select count(*) from pret where date_de_pret <= :date_de_pret and :date_de_pret <= date_retour_prevue and id_exemplaire = :id_exemplaire",nativeQuery = true)
    public int nombreDePret(@Param("date_de_pret") LocalDate date, @Param("id_exemplaire") Long idExemplaire);

    @Query(value = "SELECT count(*) from reservation where date_de_reservation <= :date and :date <= date_de_pret and etat = \'ACCEPTE\' and id_exemplaire = :id",nativeQuery = true)
    public int estReserver(@Param("id") Long idExemplaire,@Param("date") LocalDate dateDePret);
}


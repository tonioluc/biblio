package web.bibliotheque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import web.bibliotheque.model.Pret;

public interface PretRepository extends JpaRepository<Pret, Long> {
    @Query(value = "SELECT * from pret where id_adherent = :idAdherent",nativeQuery = true)
    public List<Pret> findByIdAdherent(@Param("idAdherent") Long idAdherent);
}

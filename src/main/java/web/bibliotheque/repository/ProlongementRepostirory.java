package web.bibliotheque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import web.bibliotheque.model.Pret;
import web.bibliotheque.model.Prolongement;

public interface ProlongementRepostirory extends JpaRepository<Prolongement,Long>{
    public Optional<Prolongement> findByPret(Pret pret);
    @Query(value = "SELECT COUNT(*) from prolongement pr join pret p on pr.id_pret = p.id_pret where p.id_adherent = :id",nativeQuery=true)
    public int nombreQuotaUtilise(@Param("id") Long idAdherent);
}

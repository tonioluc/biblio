package web.bibliotheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.bibliotheque.model.Pret;

public interface PretRepository extends JpaRepository<Pret, Long> {

}

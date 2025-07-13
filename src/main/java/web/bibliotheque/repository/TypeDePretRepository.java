package web.bibliotheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.bibliotheque.model.TypeDePret;

public interface TypeDePretRepository extends JpaRepository<TypeDePret, Long> {

}

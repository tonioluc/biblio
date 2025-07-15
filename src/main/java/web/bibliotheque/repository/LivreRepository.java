package web.bibliotheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.bibliotheque.model.Livre;

public interface LivreRepository extends JpaRepository<Livre,Long> {
    
}

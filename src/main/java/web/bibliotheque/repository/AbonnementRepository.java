package web.bibliotheque.repository;

import org.springframework.data.jpa.repository.*;

import web.bibliotheque.model.Abonnement;

// AdherentRepository.java
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
}


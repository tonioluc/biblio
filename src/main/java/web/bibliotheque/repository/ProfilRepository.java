package web.bibliotheque.repository;

import org.springframework.data.jpa.repository.*;

import web.bibliotheque.model.Profil;

// AdherentRepository.java
public interface ProfilRepository extends JpaRepository<Profil, Long> {
}


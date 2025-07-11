package web.bibliotheque.model;

import java.time.LocalDate;

import jakarta.persistence.*;

// Abonnement.java
@Entity
@Table(name = "abonnement")
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAbonnement;
    
    private LocalDate dateDebut;
    private LocalDate dateExpiration;
    
    @ManyToOne
    @JoinColumn(name = "id_adherent")
    private Adherent adherent;

    public Long getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(Long idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    
    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }
    
    // Getters, Setters, Constructeurs
}
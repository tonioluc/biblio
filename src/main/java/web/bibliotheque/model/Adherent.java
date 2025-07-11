package web.bibliotheque.model;

import java.time.LocalDate;

import jakarta.persistence.*;

// Adherent.java
@Entity
@Table(name = "adherent")
public class Adherent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdherent;
    
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    
    @ManyToOne
    @JoinColumn(name = "id_profil")
    private Profil profil;

    public Long getIdAdherent() {
        return idAdherent;
    }

    public void setIdAdherent(Long idAdherent) {
        this.idAdherent = idAdherent;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }
    
    // Getters, Setters, Constructeurs
}
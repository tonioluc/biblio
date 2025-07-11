package web.bibliotheque.dto;

import java.time.LocalDate;

public class InscriptionAdherentDTO {

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private Long typeCompte; // "ETUDIANT" ou "PROFESSEUR"
    private String username;
    private String password;
    private LocalDate dateDebutAbonnement;
    private LocalDate dateExpirationAbonnement;

    // --- Getters et Setters ---
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

    public Long getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(Long typeCompte) {
        this.typeCompte = typeCompte;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateDebutAbonnement() {
        return dateDebutAbonnement;
    }

    public void setDateDebutAbonnement(LocalDate dateDebutAbonnement) {
        this.dateDebutAbonnement = dateDebutAbonnement;
    }

    public LocalDate getDateExpirationAbonnement() {
        return dateExpirationAbonnement;
    }

    public void setDateExpirationAbonnement(LocalDate dateExpirationAbonnement) {
        this.dateExpirationAbonnement = dateExpirationAbonnement;
    }
}

package web.bibliotheque.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profil")
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfil;

    private String libelle;
    private int quotaPret;
    private int quotaReservation;
    private int quotaProlongement;
    private int durreeDePret;
    private int durreePenalite;

    public int getDurreePenalite() {
        return durreePenalite;
    }

    public void setDurreePenalite(int durreePenalite) {
        this.durreePenalite = durreePenalite;
    }

    public int getDurreeDePret() {
        return durreeDePret;
    }

    public void setDurreeDePret(int durreeDePret) {
        this.durreeDePret = durreeDePret;
    }

    public Long getIdProfil() {
        return idProfil;
    }

    public void setIdProfil(Long idProfil) {
        this.idProfil = idProfil;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getQuotaPret() {
        return quotaPret;
    }

    public void setQuotaPret(int quotaPret) {
        this.quotaPret = quotaPret;
    }

    public int getQuotaReservation() {
        return quotaReservation;
    }

    public void setQuotaReservation(int quotaReservation) {
        this.quotaReservation = quotaReservation;
    }

    public int getQuotaProlongement() {
        return quotaProlongement;
    }

    public void setQuotaProlongement(int quotaProlongement) {
        this.quotaProlongement = quotaProlongement;
    }

    // Getters, Setters, Constructeurs
}
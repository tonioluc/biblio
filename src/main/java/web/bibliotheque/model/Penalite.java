package web.bibliotheque.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "penalite")
public class Penalite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPenalite;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private boolean resolu;

    @ManyToOne
    @JoinColumn(name = "id_adherent")
    private Adherent adherent;

    public Long getIdPenalite() {
        return idPenalite;
    }

    public void setIdPenalite(Long idPenalite) {
        this.idPenalite = idPenalite;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isResolu() {
        return resolu;
    }

    public void setResolu(boolean resolu) {
        this.resolu = resolu;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }
}

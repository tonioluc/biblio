package web.bibliotheque.dto;

import java.time.LocalDate;

public class PretExemplaireDTO {
    private Long idLivre;

    private String adherent;
    private LocalDate dateDePret;
    private int typePret;
    private LocalDate dateDeRetour;

    public LocalDate getDateDeRetour() {
        return dateDeRetour;
    }

    public Long getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(Long idLivre) {
        this.idLivre = idLivre;
    }

    public void setDateDeRetour(LocalDate dateDeRetour) {
        this.dateDeRetour = dateDeRetour;
    }

    public int getTypePret() {
        return typePret;
    }

    public void setTypePret(int typePret) {
        this.typePret = typePret;
    }

    public String getAdherent() {
        return adherent;
    }

    public void setAdherent(String adherent) {
        this.adherent = adherent;
    }

    public LocalDate getDateDePret() {
        return dateDePret;
    }

    public void setDateDePret(LocalDate dateDePret) {
        this.dateDePret = dateDePret;
    }
}

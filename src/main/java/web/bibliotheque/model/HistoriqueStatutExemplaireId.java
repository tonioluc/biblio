package web.bibliotheque.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class HistoriqueStatutExemplaireId implements Serializable {

    private Long exemplaire;
    private Long statutExemplaire;

    public HistoriqueStatutExemplaireId(Long exemplaire, Long statutExemplaire) {
        this.exemplaire = exemplaire;
        this.statutExemplaire = statutExemplaire;
    }

    public HistoriqueStatutExemplaireId() {
    }

    public Long getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Long exemplaire) {
        this.exemplaire = exemplaire;
    }

    public Long getStatutExemplaire() {
        return statutExemplaire;
    }

    public void setStatutExemplaire(Long statutExemplaire) {
        this.statutExemplaire = statutExemplaire;
    }

}

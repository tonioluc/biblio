package web.bibliotheque.dto;

import java.time.LocalDate;

public class PretExemplaireDTO {
    private String ref;
    private String adherent;
    private LocalDate dateDePret;
    private int typePret;

    public int getTypePret() {
        return typePret;
    }

    public void setTypePret(int typePret) {
        this.typePret = typePret;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
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

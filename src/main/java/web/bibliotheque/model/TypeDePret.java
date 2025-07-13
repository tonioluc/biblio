package web.bibliotheque.model;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "type_de_pret")
public class TypeDePret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTypeDePret;

    private String libelle;

    public Long getIdTypeDePret() {
        return idTypeDePret;
    }

    public void setIdTypeDePret(Long idTypeDePret) {
        this.idTypeDePret = idTypeDePret;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}

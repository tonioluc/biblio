package web.bibliotheque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exemplaire")
public class Exemplaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExemplaire;
    private String ref;
    private String titre;
    private int restriction_age;
    public Long getIdExemplaire() {
        return idExemplaire;
    }
    public void setIdExemplaire(Long idExemplaire) {
        this.idExemplaire = idExemplaire;
    }
    public String getRef() {
        return ref;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public int getRestriction_age() {
        return restriction_age;
    }
    public void setRestriction_age(int restriction_age) {
        this.restriction_age = restriction_age;
    }
    
}

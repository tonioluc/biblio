package web.bibliotheque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "restriction_age")
public class RestrictionAge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRestriction;

    private int age;

    @OneToOne
    @JoinColumn(name = "id_livre")
    private Livre livre;

    public Long getIdRestriction() {
        return idRestriction;
    }

    public void setIdRestriction(Long idRestriction) {
        this.idRestriction = idRestriction;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }
}

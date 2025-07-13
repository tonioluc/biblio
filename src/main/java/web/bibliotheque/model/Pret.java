package web.bibliotheque.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "pret")
public class Pret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPret;
    
    private LocalDate dateDePret;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;

    @ManyToOne
    @JoinColumn(name="id_exemplaire")
    private Exemplaire exemplaire;

    @ManyToOne
    @JoinColumn(name = "id_adherent")
    private Adherent adherent;

    @ManyToOne
    @JoinColumn(name = "id_type_de_pret")
    private TypeDePret typeDePret;

    public TypeDePret getTypeDePret() {
        return typeDePret;
    }

    public void setTypeDePret(TypeDePret typeDePret) {
        this.typeDePret = typeDePret;
    }

    public Long getIdPret() {
        return idPret;
    }

    public void setIdPret(Long idPret) {
        this.idPret = idPret;
    }

    public LocalDate getDateDePret() {
        return dateDePret;
    }

    public void setDateDePret(LocalDate dateDePret) {
        this.dateDePret = dateDePret;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

}
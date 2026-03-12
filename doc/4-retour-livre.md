# Module Retour de Livre

## Description

Ce module gère le retour d'un livre emprunté. Le bibliothécaire saisit la date de retour effective et le système détermine si l'adhérent est en retard. En cas de retard, une pénalité est automatiquement créée.

---

## Règles de gestion

| # | Règle | Description |
|---|-------|-------------|
| RG-R1 | **Date de retour effective** | Le bibliothécaire doit saisir la date à laquelle le livre est effectivement rendu. |
| RG-R2 | **Détection du retard** | Si la `dateRetourEffective` est postérieure à la `dateRetourPrevue`, l'adhérent est en retard. |
| RG-R3 | **Pénalité automatique** | En cas de retard, une pénalité est créée automatiquement avec une durée dépendant du profil de l'adhérent. |
| RG-R4 | **Empilement des pénalités** | Si l'adhérent a déjà une pénalité en cours, la nouvelle pénalité commence **après** la fin de la précédente (les pénalités ne se chevauchent pas, elles s'empilent). |
| RG-R5 | **Durée de pénalité** | La durée dépend du profil : 10 jours (étudiant), 9 jours (enseignant), 8 jours (professionnel). |
| RG-R6 | **Libération de l'exemplaire** | L'exemplaire rendu repasse au statut "Disponible" dans l'historique des statuts. |
| RG-R7 | **Mise à jour du prêt** | Le champ `dateRetourEffective` est renseigné, ce qui sort le prêt de la liste "en cours". |

---

## Flux de fonctionnement

```
1. Le bibliothécaire consulte /liste-prets-en-cours (liste des prêts actifs)
2. Il clique sur "Rendre" à côté d'un prêt → GET /rendre-livre/{id}
3. Il saisit la date de retour effective
4. POST /rendre-livre/{id} → Le système :
   ├── Compare dateRetourEffective avec dateRetourPrevue
   │   ├── Retard détecté :
   │   │   ├── Calcul de la date de début de pénalité
   │   │   ├── Création pénalité (début → début + durée profil)
   │   │   └── Message : "Rendement effectué avec succès, et pénalisé"
   │   └── Pas de retard :
   │       └── Message : "Rendement effectué avec succès"
   ├── Mise à jour du prêt (dateRetourEffective renseignée)
   └── Statut exemplaire → "Disponible"
```

---

## Extraits de code

### Controller — `PretController.java`

```java
@PostMapping("/rendre-livre/{id}")
public String rendreLivre(@PathVariable("id") Long idPret,
        @RequestParam("dateDeRetour") LocalDate dateRetourEffective, Model model) {
    boolean estPenalise = pretService.rendreUnLivre(idPret, dateRetourEffective);
    loadPretsEnCours(model);
    String message = "Rendement d'un livre effectué avec succes";
    if (estPenalise) {
        message += " , et pénalisé";
    }
    model.addAttribute("message", message);
    return "liste-pret-en-cours-biblio";
}
```

### Service — `PretService.java`

```java
public boolean rendreUnLivre(Long idPret, LocalDate dateDeRetourEffective) {
    boolean result = false;
    Pret pret = this.getPretById(idPret);
    LocalDate dateRetourPrevue = pret.getDateRetourPrevue();
    pret.setDateRetourEffective(dateDeRetourEffective);

    // Vérification du retard
    if (dateRetourPrevue.isBefore(dateDeRetourEffective)) {
        Adherent adherent = pret.getAdherent();

        // Calcul de la date de début (après pénalités existantes)
        LocalDate dateDeDebutPenalite = penaliteService
            .getDateDebutPenalite(dateDeRetourEffective, adherent.getIdAdherent());

        // Création de la pénalité
        Penalite penalite = new Penalite();
        penalite.setAdherent(adherent);
        penalite.setDateDebut(dateDeDebutPenalite);
        penalite.setDateFin(dateDeDebutPenalite.plusDays(adherent.getProfil().getDurreePenalite()));
        penalite.setResolu(false);
        penaliteService.create(penalite);
        result = true;
    }

    this.updatePret(pret);

    // Remise de l'exemplaire en "Disponible"
    HistoriqueStatutExemplaire historiqueStatutExemplaire = new HistoriqueStatutExemplaire();
    historiqueStatutExemplaire.setDateChangement(dateRetourPrevue);
    historiqueStatutExemplaire.setExemplaire(pret.getExemplaire());
    historiqueStatutExemplaire.setStatutExemplaire(statutExemplaireService.getById((long) 1)); // "Disponible"
    historiqueStatutExemplaireService.create(historiqueStatutExemplaire);
    return result;
}
```

### Service — `PenaliteService.java` (empilement des pénalités)

```java
public LocalDate getDateDebutPenalite(LocalDate dateRetourEffective, Long idAdherent) {
    Optional<Penalite> penaliteOptional =
        penaliteRepository.findMaxDatePenaliteByIdAdherent(idAdherent);

    if (!penaliteOptional.isPresent()) {
        return dateRetourEffective; // Première pénalité : commence au retour
    }

    Penalite penalite = penaliteOptional.get();
    LocalDate dateFinPenalite = penalite.getDateFin();

    // Si le retour est avant la fin de la dernière pénalité,
    // la nouvelle commence après celle-ci
    if (dateRetourEffective.isBefore(dateFinPenalite)) {
        return dateFinPenalite;
    }
    return dateRetourEffective;
}
```

---

## Exemple concret de pénalité empilée

```
Scénario : Étudiant (durée pénalité = 10 jours)

📕 Prêt 1 :
   - Date retour prévue : 15/02/2025
   - Date retour effective : 20/02/2025 (5 jours de retard)
   - Pénalité 1 : du 20/02/2025 au 02/03/2025

📕 Prêt 2 (rendu pendant que pénalité 1 est active) :
   - Date retour effective : 28/02/2025
   - Pénalité 1 encore active (jusqu'au 02/03/2025)
   - Pénalité 2 : du 02/03/2025 au 12/03/2025 (empilée après la 1ère)

➡ L'adhérent est bloqué du 20/02/2025 au 12/03/2025
```

---

## Modèle de données

```
penalite
├── id_penalite (PK)
├── date_debut
├── date_fin (date_debut + durée du profil)
├── resolu (boolean)
└── id_adherent (FK) ──────► adherent
```

---

## Durées de pénalité par profil

| Profil | Durée pénalité |
|--------|---------------|
| ETUDIANT | 10 jours |
| ENSEIGNANT | 9 jours |
| PROFESSIONNEL | 8 jours |

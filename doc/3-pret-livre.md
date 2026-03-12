# Module Prêt de Livre

## Description

Ce module gère la création d'un prêt de livre par le bibliothécaire. Avant d'autoriser le prêt, le système effectue plusieurs vérifications (abonnement, quota, pénalité, âge, disponibilité).

---

## Règles de gestion

| # | Règle | Description |
|---|-------|-------------|
| RG-P1 | **Livre existant** | Le livre demandé doit exister en base. |
| RG-P2 | **Exemplaire disponible** | Au moins un exemplaire du livre doit avoir le statut "Disponible" à la date du prêt. |
| RG-P3 | **Adhérent existant** | L'adhérent identifié par son nom d'utilisateur doit exister. |
| RG-P4 | **Abonnement valide** | L'adhérent doit avoir un abonnement actif à la date du prêt. |
| RG-P5 | **Quota de prêt non atteint** | Le nombre de prêts en cours de l'adhérent doit être strictement inférieur à son quota de prêt (défini par le profil). |
| RG-P6 | **Pas de pénalité active** | L'adhérent ne doit pas être pénalisé à la date du prêt. |
| RG-P7 | **Restriction d'âge** | Si le livre a une restriction d'âge, l'âge de l'adhérent doit être supérieur ou égal à l'âge requis. |
| RG-P8 | **Type de prêt : Sur place** | Si le type est "Sur place" (id=1), la durée est de 0 jours (retour le même jour). |
| RG-P9 | **Type de prêt : Emporté** | Si le type est "Emporté" (id=2), la durée est celle définie dans le profil de l'adhérent. |
| RG-P10 | **Date de retour cohérente** | La date de retour prévue ne peut pas être antérieure à la date de prêt. |
| RG-P11 | **Jours fériés** | Si la date de retour prévue tombe un jour férié, elle est décalée au prochain jour ouvré. |
| RG-P12 | **Changement de statut** | L'exemplaire prêté passe du statut "Disponible" à "Emprunté" avec traçabilité dans l'historique. |

---

## Flux de fonctionnement

```
1. Le bibliothécaire accède à /preter-livre
2. Il sélectionne : adhérent, livre, type de prêt, date de prêt, date de retour (optionnel)
3. POST /preter-livre → Le système vérifie dans cet ordre :
   ├── Le livre existe ?
   ├── Un exemplaire est disponible ?
   ├── L'adhérent existe ?
   ├── L'abonnement est valide ?
   ├── Le quota de prêt n'est pas atteint ?
   ├── L'adhérent n'est pas pénalisé ?
   ├── L'âge minimum est respecté ?
   │
   ├── Toutes les conditions OK :
   │   ├── Calcul de la date de retour prévue
   │   ├── Ajustement pour jours fériés
   │   ├── Création du prêt
   │   ├── Mise à jour du statut de l'exemplaire → "Emprunté"
   │   └── Message : "Prêt ajouté avec succès"
   │
   └── Une condition KO :
       └── Message d'erreur spécifique
```

---

## Extraits de code

### Controller — `ExemplaireController.java`

```java
@PostMapping("/preter-livre")
public String preterExemplaire(@ModelAttribute PretExemplaireDTO pretExemplaireDTO, Model model) {
    try {
        Pret pret = exemplaireService.autoriserAPreter(pretExemplaireDTO);
        pretService.sauvegarder(pret);
        loadModelForm(model);
        model.addAttribute("message", "Pret ajouter avec success");
        return "preter-livre";
    } catch (Exception e) {
        loadModelForm(model);
        model.addAttribute("erreur", e.getMessage());
        return "preter-livre";
    }
}
```

### Service — `ExemplaireService.java` (vérifications principales)

```java
public Pret autoriserAPreter(PretExemplaireDTO pretExemplaireDTO) throws Exception {
    // 1. Vérifier que le livre existe
    Optional<Livre> livreOpt = livreService.getById(pretExemplaireDTO.getIdLivre());
    if (!livreOpt.isPresent()) throw new Exception("Livre n'existe pas");

    // 2. Vérifier qu'un exemplaire est disponible
    List<Exemplaire> exemplairesDispo = this.getExemplairesDisponibles(livre, pretExemplaireDTO.getDateDePret());
    if (exemplairesDispo.size() == 0) throw new Exception("Livre non disponible");

    // 3. Vérifier que l'adhérent existe
    Optional<Utilisateur> utilisateurOpt = utilisateurService.getByUserName(pretExemplaireDTO.getAdherent());
    if (!utilisateurOpt.isPresent()) throw new Exception("Adhérent n'existe pas");

    // 4. Vérifier l'abonnement
    if (!abonnementService.estAbonnee(adherent, datePret))
        throw new Exception("Adhérent non abonné");

    // 5. Vérifier le quota de prêt
    if (quotaDePret <= nombreDePretEnCours)
        throw new Exception("Nombre de quota de pret insuffisant.");

    // 6. Vérifier la pénalité
    if (adherentService.estPenalise(adherent, datePret))
        throw new Exception("Adhérent pénalisé.");

    // 7. Vérifier la restriction d'âge
    if (ageAdherent < ageRequis)
        throw new Exception("L'adhérent ne peut pas prêter ce livre. Age requis : ...");

    // ... création du prêt et mise à jour du statut
}
```

### Calcul de la disponibilité — `ExemplaireService.java`

```java
public List<Exemplaire> getExemplairesDisponibles(Livre livre, LocalDate date) {
    List<Exemplaire> exemplaires = exemplaireRepository.findByLivre(livre);
    List<Exemplaire> exemplairesDisponibles = new ArrayList<>();

    for (Exemplaire ex : exemplaires) {
        List<HistoriqueStatutExemplaire> historiques =
            historiqueStatutExemplaireService.getDernierStatutAvantDate(ex.getIdExemplaire(), date);

        if (historiques.isEmpty()) {
            exemplairesDisponibles.add(ex); // Pas de statut = disponible
        } else {
            HistoriqueStatutExemplaire dernier = historiques.get(0);
            if (dernier.getStatutExemplaire().getLibelle().equalsIgnoreCase("Disponible")) {
                exemplairesDisponibles.add(ex);
            }
        }
    }
    return exemplairesDisponibles;
}
```

### Calcul de la durée selon le type de prêt

```java
int durreeDePret = profil.getDurreeDePret();

// Sur place (type=1) : retour le même jour
if (pretExemplaireDTO.getTypePret() == 1) {
    durreeDePret = 0;
}

// Calcul automatique si pas de date de retour fournie
if (dateRetourPrevue == null || pretExemplaireDTO.getTypePret() == 1) {
    pret.setDateRetourPrevue(datePret.plusDays(durreeDePret));
} else if (dateRetourPrevue.isBefore(datePret)) {
    throw new Exception("La date de retour prévu ne dois pas être avant la date de prêt");
}

// Ajustement pour les jours fériés
LocalDate dateRetourActif = jourFerieService.getDateActif(pret.getDateRetourPrevue());
pret.setDateRetourPrevue(dateRetourActif);
```

---

## Types de prêt

| ID | Libellé | Durée | Description |
|----|---------|-------|-------------|
| 1 | Sur place | 0 jours | Consultation sur place, retour le même jour |
| 2 | Emporté | Selon profil | 7j (étudiant), 9j (enseignant), 12j (professionnel) |

---

## Modèle de données

```
pret
├── id_pret (PK)
├── date_de_pret
├── date_retour_prevue
├── date_retour_effective (NULL tant que non rendu)
├── id_exemplaire (FK) ──────► exemplaire
├── id_adherent (FK) ────────► adherent
└── id_type_de_pret (FK) ───► type_de_pret

exemplaire                       livre
├── id_exemplaire (PK)           ├── id_livre (PK)
└── id_livre (FK) ──────────►   └── titre

historique_statut_exemplaire
├── id_exemplaire (FK)
├── id_statut (FK) ─────────► statut_exemplaire (Disponible/Emprunté/Réservé)
└── date_changement

restriction_age
├── id_restriction (PK)
├── age (âge minimum)
└── id_livre (FK, UNIQUE) ──► livre
```

---

## Données de test

**Livres et exemplaires :**
| Livre | Titre | Exemplaires |
|-------|-------|-------------|
| 1 | Les Misérables | 3 (id: 1, 2, 3) |
| 2 | L'Étranger | 2 (id: 4, 5) |
| 3 | Harry Potter à l'école des sorciers | 1 (id: 6) |

**Jours fériés (dates fermées) :**
13/07, 19/07, 20/07, 26/07, 27/07, 03/08, 10/08, 17/08 (2025)

# Module Prolongement (Extension de prêt)

## Description

Ce module permet à un adhérent de demander une prolongation de la durée de son prêt. La demande doit ensuite être validée ou refusée par le bibliothécaire. C'est un workflow en deux étapes : demande par l'adhérent → décision par le bibliothécaire.

---

## Règles de gestion

| # | Règle | Description |
|---|-------|-------------|
| RG-PR1 | **Un seul prolongement par prêt** | Chaque prêt ne peut être prolongé qu'une seule fois (contrainte UNIQUE sur `id_pret` dans la table `prolongement`). |
| RG-PR2 | **Calcul de la nouvelle date** | La nouvelle date de retour = date retour prévue actuelle + durée de prêt du profil (7, 9 ou 12 jours). |
| RG-PR3 | **Abonnement valide** | L'abonnement de l'adhérent doit être valide jusqu'à la nouvelle date de retour calculée. |
| RG-PR4 | **Quota de prolongement** | Le nombre total de prolongements utilisés par l'adhérent ne doit pas dépasser le quota défini par son profil. |
| RG-PR5 | **Pas de pénalité active** | L'adhérent ne doit pas être pénalisé à la date de la nouvelle échéance. |
| RG-PR6 | **Statut en attente** | La demande est créée avec `checked=false` et `accepted=false` (en attente de validation). |
| RG-PR7 | **Validation bibliothécaire** | Le bibliothécaire peut accepter ou refuser la demande de prolongement. |
| RG-PR8 | **Ajustement jours fériés** | En cas d'acceptation, la nouvelle date de retour est ajustée si elle tombe un jour férié. |

---

## Flux de fonctionnement

### Côté Adhérent (demande)

```
1. L'adhérent consulte /liste-pret-en-cours-adh (ses prêts actifs)
2. Pour chaque prêt, il voit s'il a déjà demandé un prolongement
3. Il clique sur "Demander prolongement" → GET /demande-prolongement/{id}
4. Le système vérifie :
   ├── Le prêt n'a pas déjà un prolongement ?
   ├── L'abonnement couvre la nouvelle date ?
   ├── Le quota de prolongement n'est pas atteint ?
   ├── Pas de pénalité active ?
   │
   ├── OK → Création de la demande (checked=false, accepted=false)
   │        Message : "Prolongement fait avec succès"
   └── KO → Message d'erreur spécifique
```

### Côté Bibliothécaire (validation)

```
1. Le bibliothécaire consulte /liste-prolongements (demandes en attente)
2. Pour chaque demande, il peut :
   ├── Accepter → /accepter-prolongement/{id}
   │   ├── Date retour prévue mise à jour (ajustée pour jours fériés)
   │   ├── checked=true, accepted=true
   │   └── Message de confirmation
   └── Refuser → /refuser-prolongement/{id}
       ├── checked=true, accepted=false
       └── Message de confirmation
```

---

## Extraits de code

### Controller adhérent — `AdherentController.java`

```java
@GetMapping("/demande-prolongement/{id}")
public String prolongerPret(@PathVariable Long id, HttpSession session, Model model) {
    Pret pret = pretService.getPretById(id);
    if (pret != null) {
        boolean isProlonged = prolongementService.pretProlonger(pret);
        if (!isProlonged) {
            try {
                prolongementService.autoriserAProlonger(pret, (Long) session.getAttribute("idUser"));
                chargerModelListePretEnCours(session, model);
                model.addAttribute("message", "Prolongement fait avec success");
                return "liste-pret-en-cours-adh";
            } catch (Exception e) {
                chargerModelListePretEnCours(session, model);
                model.addAttribute("erreur", e.getMessage());
                return "liste-pret-en-cours-adh";
            }
        }
        chargerModelListePretEnCours(session, model);
        model.addAttribute("erreur", "Pret déjà prolongé");
        return "liste-pret-en-cours-adh";
    }
    chargerModelListePretEnCours(session, model);
    model.addAttribute("erreur", "Pret non trouvé");
    return "liste-pret-en-cours-adh";
}
```

### Service — `ProlongementService.java` (vérifications)

```java
public void autoriserAProlonger(Pret pret, Long idUtilisateur) throws Exception {
    Utilisateur utilisateur = utilisateurService.getById(idUtilisateur);
    Adherent adherent = utilisateur.getAdherent();
    Profil profil = adherent.getProfil();

    // Nouvelle date = date retour actuelle + durée du profil
    LocalDate dateApresProlongement = pret.getDateRetourPrevue().plusDays(profil.getDurreeDePret());

    // Vérification abonnement valide à la nouvelle date
    if (!abonnementService.estAbonnee(adherent, dateApresProlongement)) {
        throw new Exception("Adhérent non abonnée");
    }

    // Vérification quota de prolongement
    int quotaProlongement = profil.getQuotaProlongement();
    int quotaUtilise = this.nombreQuotaUtilise(adherent.getIdAdherent());
    if (quotaProlongement <= quotaUtilise) {
        throw new Exception("Quota de prolongement atteint pour l'adhérent, utilisé : " + quotaUtilise);
    }

    // Vérification pas de pénalité
    if (adherentService.estPenalise(adherent, dateApresProlongement)) {
        throw new Exception("Vous êtes pénalisé, vous ne pouvez pas faire un prolongement");
    }

    // Création de la demande (en attente)
    Prolongement prolongement = new Prolongement();
    prolongement.setPret(pret);
    prolongement.setDateRetourApresProlongement(dateApresProlongement);
    prolongement.setAccepted(false);  // pas encore accepté
    prolongement.setChecked(false);   // pas encore traité
    prolongementRepostirory.save(prolongement);
}
```

### Service — `ProlongementService.java` (acceptation / refus)

```java
public void accepterProlongement(Prolongement prolongement) {
    Pret pret = prolongement.getPret();
    LocalDate dateRetourPrevue = pret.getDateRetourPrevue();

    // Ajustement pour les jours fériés
    pret.setDateRetourPrevue(jourFerieService.getDateActif(dateRetourPrevue));
    pretService.updatePret(pret);

    prolongement.setAccepted(true);
    prolongement.setChecked(true);
    this.sauvergarder(prolongement);
}

public void refuserProlongement(Prolongement prolongement) {
    prolongement.setChecked(true);  // traité, mais non accepté
    this.sauvergarder(prolongement);
}
```

---

## Quotas de prolongement par profil

| Profil | Quota prolongement | Durée ajoutée |
|--------|--------------------|---------------|
| ETUDIANT | 3 max | +7 jours |
| ENSEIGNANT | 5 max | +9 jours |
| PROFESSIONNEL | 7 max | +12 jours |

---

## Exemple concret

```
Scénario : Étudiant avec un prêt en cours

📕 Prêt initial :
   - Date de prêt : 01/03/2025
   - Date retour prévue : 08/03/2025 (7 jours)
   - Quota prolongement utilisé : 0/3

📝 Demande de prolongement :
   - Nouvelle date calculée : 08/03 + 7 = 15/03/2025
   - Vérifications : abonnement OK, quota OK (0 < 3), pas pénalisé
   - Demande créée (checked=false, accepted=false)

✅ Bibliothécaire accepte :
   - 15/03/2025 n'est pas un jour férié → pas d'ajustement
   - Date retour prévue mise à jour : 15/03/2025
   - checked=true, accepted=true
   - Quota prolongement utilisé : 1/3
```

---

## Modèle de données

```
prolongement
├── id_prolongement (PK)
├── id_pret (FK, UNIQUE) ──────► pret (1 prolongement max par prêt)
├── accepted (boolean)          → true si le bibliothécaire a accepté
├── checked (boolean)           → true si le bibliothécaire a traité la demande
└── date_retour_apres_prolongement (nouvelle date de retour calculée)
```

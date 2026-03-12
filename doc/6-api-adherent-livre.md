# Module API REST (Consultation adhérent & livre)

## Description

Ce module expose deux endpoints REST qui retournent des informations détaillées sur un adhérent ou un livre au format JSON. Ces APIs sont utilisées côté front pour afficher dynamiquement les données sans rechargement de page.

---

## Règles de gestion

| # | Règle | Description |
|---|-------|-------------|
| RG-A1 | **Adhérent existant** | Si l'ID de l'adhérent n'existe pas, retourner une erreur `"Adherent non trouvé"`. |
| RG-A2 | **Statut abonnement** | L'API calcule en temps réel si l'adhérent est abonné à la date du jour. |
| RG-A3 | **Quota restant** | Nombre de prêts restants = quota du profil − nombre de prêts en cours. |
| RG-A4 | **Statut pénalité** | Si l'adhérent est pénalisé, la dernière pénalité est incluse dans la réponse. |
| RG-A5 | **Livre existant** | Si l'ID du livre n'existe pas, retourner une erreur `"Livre non trouvé"`. |
| RG-A6 | **Disponibilité** | L'API retourne le nombre d'exemplaires disponibles et le statut de chaque exemplaire. |

---

## Endpoint 1 : Informations adhérent

### `POST /api/adherent`

**Paramètre :** `id` (Long) — ID de l'adhérent

**Réponse JSON (succès) :**
```json
{
    "idAdherent": 1,
    "nom": "Bensaid",
    "prenom": "Amine",
    "est abonnée": true,
    "nombre de pret en cours": 1,
    "reste quota de prêt": 1,
    "est pénalisé": false
}
```

**Réponse JSON (adhérent pénalisé) :**
```json
{
    "idAdherent": 2,
    "nom": "El Khattabi",
    "prenom": "Sarah",
    "est abonnée": true,
    "nombre de pret en cours": 2,
    "reste quota de prêt": 0,
    "est pénalisé": "true. Dernière pénalité : Penalite{dateDebut=2025-03-01, dateFin=2025-03-11}"
}
```

**Réponse JSON (erreur) :**
```json
{
    "error": "Adherent non trouvé"
}
```

### Extrait de code — `ApiController.java`

```java
@PostMapping("/api/adherent")
public Map<String, Object> endPointAdherent(@RequestParam("id") Long id) {
    Map<String, Object> response = new HashMap<>();
    Optional<Adherent> adherentOpt = adherentService.getAdherentById(id);
    if (!adherentOpt.isPresent()) {
        response.put("error", "Adherent non trouvé");
        return response;
    }
    Adherent adherent = adherentOpt.get();
    Profil profil = adherent.getProfil();

    response.put("idAdherent", id);
    response.put("nom", adherent.getNom());
    response.put("prenom", adherent.getPrenom());
    response.put("est abonnée", abonnementService.estAbonnee(adherent, LocalDate.now()));
    response.put("nombre de pret en cours", adherentService.nombreDePretEnCours(adherent));
    response.put("reste quota de prêt",
        profil.getQuotaPret() - adherentService.nombreDePretEnCours(adherent));

    boolean estPenalise = adherentService.estPenalise(adherent, LocalDate.now());
    if (estPenalise) {
        response.put("est pénalisé", Boolean.toString(estPenalise)
            + ". Dernière pénalité : " + penaliteService.getDernierByAdherent(adherent));
    } else {
        response.put("est pénalisé", estPenalise);
    }
    return response;
}
```

---

## Endpoint 2 : Informations livre

### `POST /api/livre`

**Paramètre :** `id` (Long) — ID du livre

**Réponse JSON (succès) :**
```json
{
    "idLivre": 1,
    "titre": "Les Misérables",
    "nombreExemplairesDisponibles": 2,
    "exemplaires": [
        { "idExemplaire": 1, "disponible": true },
        { "idExemplaire": 2, "disponible": false },
        { "idExemplaire": 3, "disponible": true }
    ]
}
```

**Réponse JSON (erreur) :**
```json
{
    "error": "Livre non trouvé"
}
```

### Extrait de code — `ApiController.java`

```java
@PostMapping("/api/livre")
public Map<String, Object> endPointLivre(@RequestParam("id") Long id) {
    Map<String, Object> response = new HashMap<>();
    Optional<Livre> livreOpt = livreService.getById(id);
    if (!livreOpt.isPresent()) {
        response.put("error", "Livre non trouvé");
        return response;
    }
    Livre livre = livreOpt.get();
    response.put("idLivre", id);
    response.put("titre", livre.getTitre());

    List<Exemplaire> exemplairesDisponibles =
        exemplaireService.getExemplairesDisponibles(livre, LocalDate.now());
    response.put("nombreExemplairesDisponibles", exemplairesDisponibles.size());

    List<Map<String, Object>> exemplairesList = new ArrayList<>();
    List<Exemplaire> exemplaires = exemplaireService.getByLivre(livre);
    for (Exemplaire exemplaire : exemplaires) {
        Map<String, Object> exJson = new HashMap<>();
        exJson.put("idExemplaire", exemplaire.getIdExemplaire());

        List<HistoriqueStatutExemplaire> historiques =
            historiqueStatutExemplaireService.getAllByExemplaire(exemplaire);
        if (historiques.isEmpty()) {
            exJson.put("disponible", true);
        } else {
            String libelle = historiques.get(0).getStatutExemplaire().getLibelle();
            exJson.put("disponible", "DISPONIBLE".equals(libelle));
        }
        exemplairesList.add(exJson);
    }
    response.put("exemplaires", exemplairesList);
    return response;
}
```

---

## Modèle de données impliqué

```
adherent ◄─── profil (quotas, durées)
    │
    ├──► abonnement (date_debut, date_expiration)
    ├──► penalite (date_debut, date_fin, resolu)
    └──► pret (en cours = date_retour_effective IS NULL)

livre ◄─── exemplaire
               │
               └──► historique_statut_exemplaire → statut_exemplaire
```

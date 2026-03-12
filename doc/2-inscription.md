# Module Inscription (Enregistrement d'un adhérent)

## Description

Ce module permet au bibliothécaire d'inscrire un nouvel adhérent dans le système. L'inscription crée simultanément l'adhérent, son compte utilisateur et son abonnement dans une transaction unique.

---

## Règles de gestion

| # | Règle | Description |
|---|-------|-------------|
| RG-I1 | **Informations obligatoires** | Nom, prénom, date de naissance, type de compte (profil), identifiants et dates d'abonnement sont requis. |
| RG-I2 | **Profil valide** | Le type de compte choisi doit correspondre à un profil existant en base (ETUDIANT, ENSEIGNANT ou PROFESSIONNEL). |
| RG-I3 | **Création transactionnelle** | L'inscription crée 3 entités en une seule transaction (`@Transactional`) : Adherent → Utilisateur → Abonnement. Si l'une échoue, tout est annulé. |
| RG-I4 | **Rôle automatique** | Le rôle de l'utilisateur créé est toujours `"ADHERENT"`. |
| RG-I5 | **Abonnement avec dates** | L'abonnement est créé avec une date de début et une date d'expiration fournies par le formulaire. |
| RG-I6 | **Quotas liés au profil** | Les quotas de prêt, réservation, prolongement et les durées sont définis par le profil choisi (voir tableau ci-dessous). |

---

## Profils et quotas associés

| Profil | Quota prêt | Durée prêt (jours) | Quota réservation | Quota prolongement | Durée pénalité (jours) |
|--------|-----------|--------------------|--------------------|---------------------|------------------------|
| ETUDIANT | 2 | 7 | 1 | 3 | 10 |
| ENSEIGNANT | 3 | 9 | 2 | 5 | 9 |
| PROFESSIONNEL | 4 | 12 | 3 | 7 | 8 |

---

## Flux de fonctionnement

```
1. Le bibliothécaire accède à /inscription
2. Le formulaire charge la liste des profils disponibles
3. Il remplit : nom, prénom, date de naissance, type de compte,
   nom d'utilisateur, mot de passe, dates d'abonnement
4. Soumission du formulaire (POST /inscrire-adherent)
5. Le système crée dans une transaction :
   a. L'adhérent (table adherent)
   b. Le compte utilisateur (table utilisateur, rôle = ADHERENT)
   c. L'abonnement (table abonnement)
   ├── OK  → Redirection vers /inscription?success
   └── KO  → Redirection vers /inscription?error avec message
```

---

## Extraits de code

### Controller — `InscriptionController.java`

```java
@Transactional
@PostMapping("/inscrire-adherent")
public String inscrireAdherent(@ModelAttribute InscriptionAdherentDTO dto, Model model) {
    try {
        // Vérification que le profil existe
        Optional<Profil> profilOpt = profilService.getById(dto.getTypeCompte());
        if (profilOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Profil non trouvé pour l'ID : " + dto.getTypeCompte());
            return "inscription";
        }
        Profil profil = profilOpt.get();

        // 1. Création de l'adhérent
        Adherent adherent = new Adherent();
        adherent.setDateNaissance(dto.getDateNaissance());
        adherent.setNom(dto.getNom());
        adherent.setPrenom(dto.getPrenom());
        adherent.setProfil(profil);
        adherentService.save(adherent);

        // 2. Création du compte utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setAdherent(adherent);
        utilisateur.setMotDePasse(dto.getPassword());
        utilisateur.setNomUtilisateur(dto.getUsername());
        utilisateur.setRole("ADHERENT");
        utilisateurService.save(utilisateur);

        // 3. Création de l'abonnement
        Abonnement abonnement = new Abonnement();
        abonnement.setDateDebut(dto.getDateDebutAbonnement());
        abonnement.setDateExpiration(dto.getDateExpirationAbonnement());
        abonnement.setAdherent(adherent);
        abonnementService.save(abonnement);

        return "redirect:/inscription?success";
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Erreur lors de l'inscription : " + e.getMessage());
        return "redirect:/inscription?error";
    }
}
```

### DTO — `InscriptionAdherentDTO.java`

```java
public class InscriptionAdherentDTO {
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private Long typeCompte;       // ID du profil (1, 2, ou 3)
    private String username;
    private String password;
    private LocalDate dateDebutAbonnement;
    private LocalDate dateExpirationAbonnement;
}
```

---

## Modèle de données

```
adherent                         profil
├── id_adherent (PK)             ├── id_profil (PK)
├── nom                          ├── libelle
├── prenom                       ├── quota_pret
├── date_naissance               ├── durree_de_pret
└── id_profil (FK) ─────────►   ├── quota_reservation
                                 ├── quota_prolongement
abonnement                      └── durree_penalite
├── id_abonnement (PK)
├── date_debut
├── date_expiration
└── id_adherent (FK) ──────────► adherent

utilisateur
├── id_utilisateur (PK)
├── nom_utilisateur (UNIQUE)
├── mot_de_passe
├── role = "ADHERENT"
└── id_adherent (FK) ──────────► adherent
```

---

## Données de test

| ID | Nom | Prénom | Profil | Date naissance | Abonnement |
|----|-----|--------|--------|----------------|------------|
| 1 | Bensaid | Amine | ETUDIANT | 2000-01-01 | 01/02/2025 → 24/07/2025 |
| 2 | El Khattabi | Sarah | ETUDIANT | 2001-02-02 | 01/02/2025 → 01/07/2025 |
| 3 | Moujahid | Youssef | ETUDIANT | 2002-03-03 | 01/04/2025 → 01/12/2025 |
| 4 | Benali | Nadia | ENSEIGNANT | 1999-04-04 | 01/07/2025 → 01/07/2025 |
| 5 | Haddadi | Karima | ENSEIGNANT | 2003-05-05 | 01/08/2025 → 01/05/2025 |
| 6 | Touhami | Salima | ENSEIGNANT | 2004-06-06 | 01/07/2025 → 01/06/2025 |
| 7 | El Mansouri | Rachid | PROFESSIONNEL | 2005-07-07 | 01/06/2025 → 01/12/2025 |
| 8 | Zerouali | Amina | PROFESSIONNEL | 2006-08-08 | 01/10/2025 → 01/06/2025 |

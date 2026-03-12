# Module Login (Authentification)

## Description

Ce module gère la connexion des utilisateurs à l'application. Deux rôles existent : **ADHERENT** (membre de la bibliothèque) et **BIBLIOTHECAIRE** (gestionnaire).

---

## Règles de gestion

| # | Règle | Description |
|---|-------|-------------|
| RG-L1 | **Identifiants obligatoires** | L'utilisateur doit fournir un nom d'utilisateur et un mot de passe pour se connecter. |
| RG-L2 | **Vérification en base** | Le couple (nom_utilisateur, mot_de_passe) doit exister dans la table `utilisateur`. |
| RG-L3 | **Session utilisateur** | En cas de succès, l'`idUtilisateur` et le `role` sont stockés en session HTTP. |
| RG-L4 | **Redirection selon le rôle** | Après connexion, l'utilisateur est redirigé vers `/accueil` qui affiche la page correspondant à son rôle (accueil adhérent ou accueil bibliothécaire). |
| RG-L5 | **Message d'erreur** | Si les identifiants sont incorrects, un message d'erreur s'affiche : *"Nom d'utilisateur ou mot de passe n'est pas correct"*. |
| RG-L6 | **Unicité du nom d'utilisateur** | Le champ `nom_utilisateur` est unique en base de données (contrainte `UNIQUE`). |

---

## Flux de fonctionnement

```
1. L'utilisateur accède à /se-connecter (ou / qui redirige)
2. Il saisit son nom d'utilisateur et son mot de passe
3. Le système vérifie les identifiants en base
   ├── OK  → Session créée → Redirection vers /accueil
   └── KO  → Message d'erreur affiché sur la page login
```

---

## Extraits de code

### Controller — `AuthController.java`

```java
@PostMapping("/se-connecter")
public String traitement(@RequestParam("username") String username,
                         @RequestParam("password") String mdp,
                         HttpSession session, Model model) {
    try {
        Utilisateur utilisateur = utilisateurService.checkLogin(username, mdp);
        session.setAttribute("idUser", utilisateur.getIdUtilisateur());
        session.setAttribute("role", utilisateur.getRole());
        return "redirect:/accueil";
    } catch (Exception e) {
        model.addAttribute("erreur", e.getMessage());
        return "login";
    }
}
```

### Service — `UtilisateurService.java`

```java
public Utilisateur checkLogin(String username, String mdp) throws Exception {
    Optional<Utilisateur> utilisateurOpt =
        utilisateurRepository.findByNomUtilisateurAndMotDePasse(username, mdp);
    if (utilisateurOpt.isPresent()) {
        return utilisateurOpt.get();
    }
    throw new Exception("Nom d'utilisateur ou mot de passe n'est pas correct");
}
```

### Redirection selon le rôle — `GlobalController.java`

```java
@GetMapping("/accueil")
public String showHome(HttpSession session) {
    String roleUserConnecter = (String) session.getAttribute("role");
    if (roleUserConnecter.equalsIgnoreCase("ADHERENT")) {
        return "accueil-adh";
    } else {
        return "accueil-biblio";
    }
}
```

---

## Modèle de données

```
utilisateur
├── id_utilisateur (PK, auto-incrémenté)
├── nom_utilisateur (UNIQUE)
├── mot_de_passe
├── role ("ADHERENT" | "BIBLIOTHECAIRE")
├── id_adherent (FK vers adherent, nullable)
└── id_bibliothecaire (FK vers bibliothecaire, nullable)
```

---

## Données de test

| Nom utilisateur | Mot de passe | Rôle |
|-----------------|-------------|------|
| ETU001 | mdp123 | ADHERENT |
| ETU002 | mdp123 | ADHERENT |
| ENS001 | mdp123 | ADHERENT |
| biblio | mdp123 | BIBLIOTHECAIRE |

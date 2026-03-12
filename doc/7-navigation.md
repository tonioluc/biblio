# Module Navigation et Accès par Rôle

## Description

Ce module gère la navigation globale de l'application et l'accès aux fonctionnalités selon le rôle de l'utilisateur connecté (ADHERENT ou BIBLIOTHECAIRE). Chaque rôle a sa propre page d'accueil et ses propres fonctionnalités accessibles.

---

## Règles de gestion

| # | Règle | Description |
|---|-------|-------------|
| RG-N1 | **Redirection racine** | L'accès à `/` redirige automatiquement vers `/se-connecter`. |
| RG-N2 | **Page d'accueil par rôle** | `/accueil` affiche `accueil-adh.html` pour un ADHERENT ou `accueil-biblio.html` pour un BIBLIOTHECAIRE. |
| RG-N3 | **Session obligatoire** | Le rôle et l'ID utilisateur sont lus depuis la session HTTP. Sans session, l'accès est impossible. |

---

## Fonctionnalités par rôle

### Adhérent (ADHERENT)

| Fonctionnalité | URL | Description |
|----------------|-----|-------------|
| Accueil | `/accueil` | Page d'accueil adhérent |
| Mes prêts en cours | `/liste-pret-en-cours-adh` | Liste des prêts actifs de l'adhérent connecté |
| Demander un prolongement | `/demande-prolongement/{id}` | Demande d'extension pour un prêt |

### Bibliothécaire (BIBLIOTHECAIRE)

| Fonctionnalité | URL | Description |
|----------------|-----|-------------|
| Accueil | `/accueil` | Page d'accueil bibliothécaire |
| Inscrire un adhérent | `/inscription` | Formulaire d'inscription |
| Prêter un livre | `/preter-livre` | Formulaire de prêt |
| Liste des prêts en cours | `/liste-prets-en-cours` | Tous les prêts actifs |
| Rendre un livre | `/rendre-livre/{id}` | Formulaire de retour |
| Prolongements à traiter | `/liste-prolongements` | Demandes en attente de validation |
| Accepter prolongement | `/accepter-prolongement/{id}` | Approuver une demande |
| Refuser prolongement | `/refuser-prolongement/{id}` | Rejeter une demande |
| API Adhérent | `/api/adherent` | Consulter les infos d'un adhérent (JSON) |
| API Livre | `/api/livre` | Consulter les infos d'un livre (JSON) |

---

## Extrait de code — `GlobalController.java`

```java
@Controller
public class GlobalController {

    @GetMapping("/accueil")
    public String showHome(HttpSession session) {
        String roleUserConnecter = (String) session.getAttribute("role");
        if (roleUserConnecter.equalsIgnoreCase("ADHERENT")) {
            return "accueil-adh";
        } else {
            return "accueil-biblio";
        }
    }
}
```

---

## Pages HTML (templates Thymeleaf)

| Template | Rôle | Description |
|----------|------|-------------|
| `login.html` | Tous | Page de connexion |
| `inscription.html` | Bibliothécaire | Formulaire d'inscription |
| `accueil-adh.html` | Adhérent | Menu principal adhérent |
| `accueil-biblio.html` | Bibliothécaire | Menu principal bibliothécaire |
| `preter-livre.html` | Bibliothécaire | Formulaire de prêt |
| `rendre-livre.html` | Bibliothécaire | Formulaire de retour |
| `liste-pret-en-cours-adh.html` | Adhérent | Prêts en cours de l'adhérent |
| `liste-pret-en-cours-biblio.html` | Bibliothécaire | Tous les prêts en cours |
| `liste-prets-prolonges.html` | Bibliothécaire | Demandes de prolongement |
| `api-adherent.html` | Bibliothécaire | Interface consultation adhérent |
| `api-livre.html` | Bibliothécaire | Interface consultation livre |

---

## Schéma de navigation

```
                    ┌──────────────┐
                    │  /           │
                    │  (racine)    │
                    └──────┬───────┘
                           │ redirect
                    ┌──────▼───────┐
                    │ /se-connecter│
                    │   (LOGIN)    │
                    └──────┬───────┘
                           │ succès
                    ┌──────▼───────┐
                    │  /accueil    │
                    └──────┬───────┘
                           │
              ┌────────────┴────────────┐
              │                         │
     ┌────────▼────────┐     ┌─────────▼─────────┐
     │   ADHERENT       │     │   BIBLIOTHECAIRE   │
     │  accueil-adh     │     │   accueil-biblio   │
     └────────┬─────────┘     └─────────┬──────────┘
              │                         │
     ┌────────▼─────────┐     ┌─────────▼──────────┐
     │ Mes prêts        │     │ Inscription         │
     │ Prolongement     │     │ Prêter livre        │
     └──────────────────┘     │ Rendre livre        │
                              │ Prêts en cours      │
                              │ Prolongements       │
                              │ API Adhérent/Livre  │
                              └─────────────────────┘
```

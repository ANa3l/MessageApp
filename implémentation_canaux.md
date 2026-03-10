# Plan d'implémentation — Canaux & Messages

## Etat des lieux du core existant

| Couche | Channels | Messages |
|---|---|---|
| Data model | `Channel` (creator, name, isPrivate, users) | `Message` (sender, recipient UUID, emissionDate, text) |
| DataManager | `getChannels()`, `sendChannel()` | `getMessages()`, `sendMessage()`, `getMessagesFrom()`, `getMessagesTo()` |
| EntityManager | `writeChannelFile()`, extract, delete | `writeMessageFile()`, extract, delete |
| IDatabaseObserver | `notifyChannelAdded/Deleted` | `notifyMessageAdded/Deleted/Modified` |
| **Manque** | `deleteChannel()` dans DataManager | `deleteMessage()` dans DataManager |

---

## Feature 1 — Gestion des Canaux (CHN-001 a CHN-008)

### Specs couvertes

| Spec | Description |
|---|---|
| SRS-MAP-CHN-001 | Consulter la liste des canaux enregistres |
| SRS-MAP-CHN-002 | Rechercher un canal |
| SRS-MAP-CHN-003 | Creer un canal public |
| SRS-MAP-CHN-004 | Creer un canal prive |
| SRS-MAP-CHN-005 | Quitter un canal prive (non proprietaire) |
| SRS-MAP-CHN-006 | Supprimer un canal prive (proprietaire) |
| SRS-MAP-CHN-007 | Ajouter un utilisateur a un canal prive (proprietaire) |
| SRS-MAP-CHN-087 | Supprimer un utilisateur d'un canal prive (proprietaire) |

### Composants a creer

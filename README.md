# Spotify Clone 🎵

Un lecteur audio JavaFX inspiré de Spotify, développé dans le cadre du TP3 - Java Avancé (Ynov Toulouse).

## Stack technique

- Java 25
- JavaFX 21
- JavaFX Media (lecture audio)
- Jackson Databind (persistance JSON)
- Maven

## Fonctionnalités

### Core
- Chargement automatique des fichiers MP3/WAV depuis le dossier `resources/music/`
- Interface graphique thème Spotify (vert/noir)
- Liste des morceaux avec titre, artiste et durée
- Lecture audio complète (play, pause, stop)
- Barre de progression synchronisée en temps réel
- Affichage du morceau en cours (titre + artiste)
- Navigation entre les morceaux (next / previous)
- Contrôle du volume
- Lecture automatique du morceau suivant

### Bonus
- 🔀 **Shuffle** — mélange aléatoire de la file de lecture
- 🔁 **Repeat** — répétition du morceau en cours
- 🔍 **Recherche** — filtre la tracklist en temps réel
- 📋 **Playlists** — création, renommage, suppression et ajout de morceaux
- 💾 **Persistance JSON** — les playlists sont sauvegardées entre les sessions
- 📊 **Visualiseur** — égaliseur animé via AudioSpectrumListener
- 🖱️ **Drag & Drop** — glisser des fichiers audio directement dans la fenêtre

## Architecture MVC
src/main/java/com/spotify/
├── Main.java                    # Point d'entrée
├── SpotifyApp.java              # Setup fenêtre JavaFX
├── model/
│   ├── Track.java               # Modèle d'un morceau
│   └── Playlist.java            # Modèle d'une playlist
├── service/
│   ├── AudioPlayerService.java  # Lecture audio (MediaPlayer)
│   ├── LibraryService.java      # Scan du dossier musique
│   ├── PlaybackQueue.java       # File de lecture
│   └── PlaylistService.java     # Gestion des playlists + JSON
├── controller/
│   └── MainController.java      # Lien vue / services
└── ui/
├── MainView.java            # Layout principal
├── PlayerBar.java           # Barre de contrôle
├── TrackCell.java           # Cellule personnalisée ListView
└── SpectrumVisualizer.java  # Égaliseur animé

## Installation

### Prérequis
- Java 17+
- Maven

### Lancer le projet

```bash
git clone https://github.com/alexbzz/spotify.git
cd spotify-clone
mvn javafx:run
```

### Ajouter de la musique

Place tes fichiers `.mp3` ou `.wav` dans :
src/main/resources/music/

Ou glisse-les directement dans la fenêtre via le **drag & drop**.

## Utilisation

| Action | Comment |
|---|---|
| Lancer un morceau | Double-clic sur un morceau |
| Ajouter à une playlist | Clic droit sur un morceau |
| Créer une playlist | Bouton "+ Nouvelle playlist" |
| Renommer / Supprimer une playlist | Clic droit sur la playlist |
| Ajouter des fichiers | Glisser-déposer dans la liste |
| Rechercher | Taper dans la barre de recherche |


## Auteur

Alexandre BOZZI — Ynov Toulouse B2 Java Avancé

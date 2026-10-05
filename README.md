# Music Playlist Shuffler 🎵

Eine Java-Desktopanwendung zur Verwaltung von Musik-Playlists mit einer grafischen Swing-Oberfläche. Das Projekt simuliert grundlegende Funktionen eines Musikplayers und verbindet Benutzeroberfläche, Datenmodell und dateibasierte Speicherung.

## Funktionen

- Playlists aus CSV-Dateien importieren und benennen
- Songs in einer Tabellenansicht anzeigen
- Wiedergabestatus zwischen Play und Pause wechseln
- Zum nächsten oder vorherigen Song wechseln
- Player- und Playlist-Daten über JSON laden und speichern

Die Wiedergabe wird derzeit als Zustand im Datenmodell simuliert; eine Audioausgabe ist noch nicht implementiert.

## Technologien

- **Java 17**
- **Swing** für die Benutzeroberfläche
- **Maven** für Build und Abhängigkeiten
- **Jackson** für JSON-Verarbeitung
- **Apache Commons CSV** für CSV-Import
- **JUnit 5** für automatisierte Tests

## Lokal starten

1. Repository klonen:

   ```bash
   git clone https://github.com/ThatsMeGIT/music_playlist_shuffler.git
   cd music_playlist_shuffler
   ```

2. Das Projekt als Maven-Projekt in einer Java-IDE öffnen und JDK 17 oder neuer auswählen.

3. Die Klasse `musicplaylistshuffler.MusicPlaylistShuffler` starten. Als Arbeitsverzeichnis das Projektverzeichnis verwenden.

Beim Start lädt die Anwendung Beispieldaten aus `src/main/resources/test.json`.

## Tests ausführen

```bash
mvn test
```

Tests sind für die Modelle `Player`, `Playlist` und `Song` sowie den CSV-Import vorhanden.

## Projektstruktur

- `model/` – Songs, Playlists und Player-Zustand
- `ui/` – Swing-Oberfläche und Verarbeitung von Benutzeraktionen
- `storage/` – Laden, Speichern und Playlist-Import
- `utils/` – CSV- und JSON-Verarbeitung
- `src/test/` – automatisierte Tests

## Geplante Erweiterungen

- Zufällige Song-Reihenfolge im Shuffle-Modus
- Weitere Tests für Speicherung und Fehlerfälle
- Verbesserte Fehlermeldungen in der Oberfläche

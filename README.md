# Music Playlist Shuffler 🎵

Eine Java-Desktopanwendung zur Verwaltung von Musik-Playlists mit einer grafischen Swing-Oberfläche. Das Projekt simuliert grundlegende Funktionen eines Musikplayers und verbindet Benutzeroberfläche, Datenmodell und dateibasierte Speicherung.

## Funktionen

- Playlists aus CSV-Dateien importieren und benennen
- Songs in einer Tabellenansicht anzeigen
- Wiedergabestatus zwischen Play und Pause wechseln
- Zum nächsten oder vorherigen Song wechseln
- Shuffle und Wiederholung der Playlist steuern
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

Beim Start erstellt die Anwendung zwei Beispiel-Playlists. Player-Daten können über die JSON-Speicherung gesichert werden.

## Tests ausführen

```bash
mvn test
```

Tests sind für die Modelle `Player`, `Playlist` und `Song` sowie den CSV-Import vorhanden.

## Automatische Qualitätsprüfung

```bash
mvn --batch-mode --no-transfer-progress clean verify -DfailIfNoTests=true
```

Dieser Befehl baut das Projekt, führt die JUnit-Tests aus und prüft anschließend
Produktionscode und Tests mit Checkstyle. Der bestehende GitHub-Workflow führt
dieselbe Prüfung bei jedem Push und Pull Request aus. Fehler in Tests oder
Checkstyle-Verstöße lassen die Prüfung fehlschlagen; die Details stehen im
Actions-Protokoll.

Die Regeln in `config/checkstyle/checkstyle.xml` prüfen unbenutzte und redundante
Imports, leere Anweisungen, zusammengehörige `equals`-/`hashCode`-Methoden,
unbeabsichtigtes Durchfallen in `switch`-Anweisungen und mehrere Anweisungen
in einer Zeile. Nur den Linter ausführen: `mvn checkstyle:check`.

Checkstyle ist kostenlos. Für dieses öffentliche Repository sind auch die
verwendeten Standard-GitHub-Actions-Runner kostenlos. Die Prüfung ergänzt
CodeRabbit-Reviews und benötigt keinen API-Schlüssel oder kostenpflichtigen Dienst.

## Projektstruktur

- `model/` – Songs, Playlists und Player-Zustand
- `ui/` – Swing-Oberfläche und Verarbeitung von Benutzeraktionen
- `storage/` – Laden, Speichern und Playlist-Import
- `utils/` – CSV- und JSON-Verarbeitung
- `src/test/` – automatisierte Tests

## Geplante Erweiterungen

- Weitere Tests für Speicherung und Fehlerfälle
- Verbesserte Fehlermeldungen in der Oberfläche

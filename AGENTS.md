# Hinweise für Coding-Agenten

## Beratende Rolle und erlaubte Änderungen

- Arbeite grundsätzlich beratend: Analysiere den Code, erkläre Probleme und
  schlage Lösungen oder Codebeispiele vor, ohne sie selbstständig umzusetzen.
- Selbstständige Änderungen sind ausschließlich für klar abgegrenzte
  Optimierungen und Stilverbesserungen erlaubt, die das bestehende Verhalten,
  öffentliche Schnittstellen und Dateiformate unverändert lassen. Dazu zählen
  beispielsweise Formatierung, das Entfernen ungenutzter Imports und lokale
  Vereinfachungen ohne Änderung der Logik.
- Implementiere neue Funktionen, Fehlerkorrekturen mit Verhaltensänderungen,
  Architekturänderungen sowie Änderungen an Abhängigkeiten, Build oder CI nur
  auf ausdrücklichen Auftrag des Nutzers. Ein Befund oder die Bitte um eine
  Einschätzung ist kein Auftrag zur Umsetzung.
- Wenn unklar ist, ob eine Änderung ausschließlich Optimierung oder Stil betrifft,
  beschreibe den Vorschlag und seine Auswirkungen und bleibe bis zu einem
  ausdrücklichen Umsetzungsauftrag beratend.
- Diese Grenze gilt auch für nebenbei entdeckte Probleme. Setze sie nicht
  eigenständig im Rahmen einer anderen Aufgabe um.
- Die folgenden Entwicklungs- und Prüfregeln gelten nur für Änderungen, die
  nach diesen Vorgaben erlaubt oder ausdrücklich beauftragt sind; sie erteilen
  selbst keine Erlaubnis zu weiteren Änderungen.

## Projekt

Music Playlist Shuffler ist eine Java-17-Desktopanwendung mit Swing und Maven.
Die Wiedergabe wird im Datenmodell simuliert; es gibt derzeit keine Audioausgabe.
Jackson verarbeitet JSON, Apache Commons CSV verarbeitet CSV und JUnit 5 prüft
das Verhalten. Lies bei Bedarf `README.md`, `pom.xml` und die betroffenen Klassen.

## Orientierung im Repository

- `src/main/java/musicplaylistshuffler/MusicPlaylistShuffler.java`: Einstiegspunkt.
- `src/main/java/musicplaylistshuffler/model/`: Songs, Playlists, Player und Zustände.
- `src/main/java/musicplaylistshuffler/ui/`: Swing-Komponenten, Tabellenmodelle und Aktionen.
- `src/main/java/musicplaylistshuffler/storage/`: Import sowie Laden und Speichern.
- `src/main/java/musicplaylistshuffler/utils/`: CSV-, JSON- und Zeit-Hilfsfunktionen.
- `src/main/resources/`: Icons, Beispieldaten und Dokumentvorlagen.
- `src/test/java/musicplaylistshuffler/`: JUnit-Tests in derselben Paketstruktur.
- `config/checkstyle/checkstyle.xml`: verbindliche Checkstyle-Regeln.
- `.github/workflows/java-ci.yml`: Build, Tests und Checkstyle in CI.

## Arbeitsweise und Stil

- Kommuniziere standardmäßig auf Deutsch. Verwende englische Klassen-, Methoden-
  und Variablennamen passend zum bestehenden Code.
- Prüfe vor Änderungen `git status --short`. Erhalte vorhandene Änderungen und
  beschränke Anpassungen auf die Aufgabe; vermeide nebenläufige Umformatierungen.
- Halte den Code mit Java 17 kompatibel. Übernimm den Stil der bearbeiteten Klasse
  und speichere Textdateien als UTF-8.
- Behalte die Paketaufteilung bei: Modelllogik gehört ins Modell, Dateioperationen
  in `storage`/`utils` und Darstellung sowie Benutzeraktionen in `ui`.
- Ergänze Abhängigkeiten nur, wenn vorhandene Bibliotheken und Java-Bordmittel
  die konkrete Aufgabe nicht sinnvoll lösen. Pflege sie in `pom.xml`.
- Bearbeite generierte Dateien unter `target/` und lokale IDE-Dateien unter
  `.idea/` nicht als Projektquellcode.

## Verhalten, das bei Änderungen zu beachten ist

- Halte aktuellen Song, Songindex, Playlistindex und Wiedergabezeit konsistent.
  Prüfe bei Änderungen an Navigation, Shuffle oder Repeat auch leere Playlists,
  den ersten und letzten Song sowie Änderungen an der aktiven Playlist.
- Shuffle verwendet eine separate Reihenfolge. Die ursprüngliche Songreihenfolge
  soll erhalten bleiben; beim Umschalten soll der aktuelle Song erhalten bleiben,
  sofern er weiterhin in der Playlist liegt.
- CSV verwendet eine Kopfzeile und vier durch Semikolon getrennte Spalten:
  Titel, Künstler, Dauer in Sekunden und Genre. Ändere das Format nur als Teil
  einer entsprechenden Aufgabe und passe dann Import, Export und Tests gemeinsam an.
- Beachte beim Ändern von Modellen Jackson-Annotationen und vorhandene JSON-Dateien.
  Sichere Änderungen an persistierten Feldern durch passende Lade-/Speichertests ab.
- `StorageService` meldet Fehler derzeit über `false` bzw. `null`, während die
  Hilfsfunktionen Exceptions weitergeben können. Beachte diese Verträge und ihre
  Aufrufer, wenn du die Fehlerbehandlung anpasst.
- Erzeuge und aktualisiere Swing-Komponenten auf dem Event Dispatch Thread.
  Vermeide längere Dateioperationen auf diesem Thread; aktualisiere nach Änderungen
  die betroffenen Ansichten und Tabellenmodelle.
- Die Anwendung speichert beim Schließen aktuell nach
  `src/main/resources/test.json`. Verwende für manuelle Versuche eine Kopie bzw.
  einen temporären Speicherpfad, damit vorhandene Beispieldaten erhalten bleiben.

## Prüfen und Starten

Führe Befehle im Projektverzeichnis mit JDK 17 und installiertem Maven aus.
Es gibt derzeit keinen Maven Wrapper.

```text
mvn test
mvn "-Dtest=PlayerTest" test
mvn checkstyle:check
mvn --batch-mode --no-transfer-progress clean verify -DfailIfNoTests=true
```

- Nutze gezielte Tests während der Entwicklung. Führe nach Änderungen an Java-Code,
  Build-Konfiguration oder verhaltensrelevanten Ressourcen die vollständige
  CI-Prüfung mit `clean verify` aus. Für reine Dokumentationsänderungen genügt
  eine Prüfung des Inhalts und des Diffs.
- Ergänze für Fehlerkorrekturen und neues Verhalten aussagekräftige JUnit-5-Tests
  im betroffenen Paket. Verwende `@TempDir` für Dateitests statt Repository-Dateien.
- Prüfe bei Shuffle-Tests Inhalt und Zustandskonsistenz; verlange keine bestimmte
  zufällige Reihenfolge und keine bei jedem Aufruf veränderte Reihenfolge.
- Umgehe fehlgeschlagene Tests oder Checkstyle nicht durch Abschalten der Prüfung.
  Berichte bestehende, aufgabenfremde Fehler und fehlende lokale Voraussetzungen.
- Starte für manuelle UI-Prüfungen die Klasse
  `musicplaylistshuffler.MusicPlaylistShuffler` in der IDE mit dem Projektverzeichnis
  als Arbeitsverzeichnis. Prüfe die von der Änderung betroffenen Bedienabläufe.
- Fasse zum Abschluss die Änderung und die tatsächlich ausgeführten Prüfungen
  zusammen; kennzeichne nicht ausgeführte Prüfungen und deren Grund.

## Paketname

`musicplaylistshuffler.ui`

**Kurzbeschreibung**

Das Paket enthält die Swing-Oberfläche, die Verarbeitung von Benutzeraktionen und die Rückmeldung zu Dateioperationen. Die Fehlerbehandlung aus [Issue #6](https://github.com/ThatsMeGIT/music_playlist_shuffler/issues/6) zeigt verständliche Meldungen und hält das Fenster bei fehlgeschlagenem Speichern offen.

## Gestaltung

Die gemeinsame dunkle Waldgrün-Gestaltung ist in `UiStyle` ausgelagert. Die [Styling-Dokumentation](STYLING.md) beschreibt Farben, Schriftgrößen, Abstände, Komponentenhilfen, Layouts, Zustandsdarstellung und die visuelle Prüfung mit Vorschauen.

## Verzeichnis

```text
src/main/java/musicplaylistshuffler/ui/
```

## Enthaltene Klassen

### `MainFrame.java`

Das Hauptfenster zeigt die Playlist-Tabelle und die Wiedergabesteuerung. Es aktualisiert die Ansichten nach Änderungen und übergibt Schließversuche an `CloseHandler`. Fehler beim Speichern erscheinen als Fehlerdialog.

### `MainActionHandler.java`

Der Aktionshandler verarbeitet Wiedergabe und Pause, Navigation, Zufallswiedergabe und Wiederholung sowie Playlist-Aktionen. Beim CSV-Import und -Export startet er Hintergrundaufgaben, sperrt vorübergehend das Hauptfenster und zeigt anschließend eine Erfolgs- oder Fehlermeldung. Importierte Playlists werden erst im Swing-Ereignisthread (Event Dispatch Thread, EDT) zum Player hinzugefügt.

### `CloseHandler.java`

Die Klasse steuert das Speichern vor dem Schließen:

- Er pausiert die Wiedergabe und sperrt das Hauptfenster während des Speicherns.
- Bei Erfolg führt er die übergebene Schließaktion aus.
- Bei einem Fehler gibt er das Fenster wieder frei, stellt den vorherigen Wiedergabestatus wieder her und löst die Fehlermeldung aus.
- Ein weiterer Schließversuch kann das Speichern erneut starten.
- Mehrere Schließversuche während einer laufenden Speicherung starten keine zusätzlichen Schreibvorgänge.

### `FileOperationWorker.java`

Dieser generische `SwingWorker` führt Dateioperationen außerhalb des EDT aus. Ergebnis und Fehler werden über Rückruffunktionen im EDT verarbeitet. Ausnahmen werden nicht als ungefangene Fehler an die Oberfläche weitergereicht. Die Klasse und ihr Konstruktor sind innerhalb des UI-Pakets zugänglich.

### Weitere Komponenten

| Klasse | Aufgabe |
|---|---|
| `PlaybackControlPanel` | Wiedergabesteuerung, Songinformationen und Fortschritt; startet und stoppt den Timer mit dem Lebenszyklus des Panels. |
| `PlaylistDialog` | Songübersicht, Playlist-Statistik sowie Wiedergabe-, Export- und Löschaktionen. |
| `PlaylistTableModel` | Liefert Namen, Songanzahl, Dauer und häufigstes Genre für die Playlist-Tabelle. |
| `MenuBar` | Erstellt das Dateimenü und leitet dessen Aktionen weiter. |

## Methoden

### `MainFrame(Player player, String path, boolean saveOnClose)`

#### Beschreibung

Erstellt das Hauptfenster und verbindet die Benutzeraktionen mit dem Player. `path` bezeichnet die JSON-Datei für das Speichern beim Schließen. `saveOnClose` steuert, ob vor dem Schließen gespeichert werden darf.

#### Beispiel

```java
SwingUtilities.invokeLater(() -> new MainFrame(player, path, saveAllowed));
```

#### Hinweise

- Das Fenster muss im EDT erstellt werden.
- Der Konstruktor mit zwei Parametern aktiviert das Speichern beim Schließen.
- Nach einem Ladefehler setzt der Anwendungseinstieg `saveOnClose` auf `false`, damit vorhandene Daten nicht durch den Beispiel-Player überschrieben werden.
- Bei aktivierter Speicherung beendet das Fenster die Anwendung erst nach erfolgreichem Speichern.

### `CloseHandler.requestClose()`

#### Beschreibung

Ist automatisches Speichern deaktiviert, wird die Schließaktion direkt ausgeführt. Andernfalls startet die Klasse eine Hintergrundaufgabe mit `StorageService.savePlayerToJson()`. Dessen Rückgabewert `false` wird als Fehler behandelt: Das Fenster bleibt offen und die Fehlerrückmeldung wird ausgeführt.

#### Beispiel

Beispiel innerhalb des UI-Pakets mit Rückmeldungen an das Hauptfenster:

```java
CloseHandler handler = new CloseHandler(
        player,
        path,
        true,
        mainFrame::dispose,
        () -> JOptionPane.showMessageDialog(
                mainFrame, "Die Playlists konnten nicht gespeichert werden.",
                "Speichern fehlgeschlagen", JOptionPane.ERROR_MESSAGE),
        saving -> mainFrame.setEnabled(!saving)
);
SwingUtilities.invokeLater(handler::requestClose);
```

#### Hinweise

- Aufruf und Rückruffunktionen gehören in den EDT.
- `closeWindow` legt fest, wie die Sitzung beendet wird. Das produktive Hauptfenster ruft zusätzlich zu `dispose()` auch `System.exit(0)` auf.
- Während der Speicherung pausiert der Player, damit sich der Wiedergabezustand nicht durch den Timer verändert.
- Die Klasse erhält die bestehende Sperre gegen automatisches Speichern nach einem Ladefehler.

### `FileOperationWorker<T>(Callable<T> operation, Consumer<T> onSuccess, Runnable onFailure)`

#### Beschreibung

`execute()` startet `operation` im Hintergrund. `done()` verarbeitet das Ergebnis im EDT: Bei einer erfolgreich abgeschlossenen Operation erhält `onSuccess` den Rückgabewert. Ausnahmen und ein Abbruch führen zu `onFailure`.

#### Beispiel

Beispiel innerhalb des UI-Pakets:

```java
new FileOperationWorker<>(
        () -> CsvUtils.loadSongsFromCsv(csvPath),
        songs -> {
            player.addPlaylist(new Playlist(playlistName, songs));
            mainFrame.refreshPlayerView();
        },
        () -> JOptionPane.showMessageDialog(
                mainFrame, "Die Playlist konnte nicht importiert werden.",
                "Import fehlgeschlagen", JOptionPane.ERROR_MESSAGE)
).execute();
```

#### Hinweise

- Die Hintergrundoperation darf keine Swing-Komponenten verändern.
- Modelländerungen, die die Oberfläche verwendet, erfolgen in der Ergebnisrückruffunktion.
- `onSuccess` bedeutet, dass die Operation keine Ausnahme ausgelöst hat. Fachliche Fehlerwerte wie `false` oder eine leere Songliste müssen von der Rückruffunktion geprüft werden.
- Fehlermeldungen beschreiben die gescheiterte Aktion und einen möglichen nächsten Schritt. Sie zeigen keine Aufrufstapel oder internen Ausnahmetexte.

### `MainActionHandler.actionPerformed(ActionEvent event, Playlist playlist)`

#### Beschreibung

Verarbeitet `playPlaylist`, `deletePlaylist` und `exportPlaylist`. Der Export arbeitet mit einer Kopie der ursprünglichen Songliste. Ein Exportfehler öffnet einen Fehlerdialog, statt eine `RuntimeException` im EDT auszulösen.

#### Hinweise

- Beim Import werden Dateien ohne `.csv`-Endung mit einer Meldung abgewiesen; die Prüfung ignoriert Groß- und Kleinschreibung.
- Leere Importergebnisse und Ausnahmen beim Import lösen eine Fehlermeldung aus.
- Die bestehenden CSV-Validierungsregeln bleiben erhalten. Insbesondere kann der Parser bei einer Zeile mit falscher Spaltenanzahl die zuvor gelesenen Songs zurückgeben. Eine umfassendere Validierung ist Gegenstand von [Issue #13](https://github.com/ThatsMeGIT/music_playlist_shuffler/issues/13).
- Der Menüeintrag „Add Song“ besitzt derzeit noch keine angebundene Aktion.

## Abhängigkeiten

- `model.Player`, `model.Playlist` und `model.Song`: Zustände und Playlist-Inhalte.
- `storage.StorageService`: Speichern des Players mit dem bestehenden `boolean`-Fehlervertrag.
- `utils.CsvUtils`: CSV-Import und -Export; Ausnahmen verarbeitet die Hintergrundaufgabe.
- `utils.TimeUtils`: Darstellung der Wiedergabezeit und Playlist-Dauer.
- Java Swing und AWT: Fenster, Komponenten, Ereignisse und Hintergrundaufgaben.

## Tests

Die Tests liegen unter `src/test/java/musicplaylistshuffler/ui/`:

- `CloseHandlerTest`: erfolgreicher Abschluss, Speicherfehler, erneuter Speicherversuch, doppelte Schließversuche und Erhalt geschützter Dateien.
- `FileOperationWorkerTest`: Ausführung außerhalb des EDT, Rückmeldungen im EDT sowie Import-, Export- und sonstige Ausnahmen.
- `PlaybackControlPanelTest`: Anzeige ohne Song oder Playlist und nach dem Löschen der aktiven Playlist.

Gezielte Prüfung:

```text
mvn "-Dtest=CloseHandlerTest,FileOperationWorkerTest,PlaybackControlPanelTest" test
```

Vollständige Prüfung:

```text
mvn --batch-mode --no-transfer-progress clean verify -DfailIfNoTests=true
```

Die automatischen Tests prüfen Rückmeldungen über Rückruffunktionen. Die sichtbaren Dialoge und die Bedienbarkeit müssen zusätzlich manuell geprüft werden. Dafür eine Kopie der JSON-Datei als Speicherpfad verwenden, damit die Beispieldaten erhalten bleiben.

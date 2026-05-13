# Music Playlist Shuffler

This project is a small music player simulation with a user interface. Its goal is to recreate core music player features in a simple way.

## Development Notes

- `MainActionHandler` is currently a independent class (Does it need to be a inner class of `MainFrame`?)
- Tests not fully implemented yet (Is there another option to automaticly run the tests? Like a CI/CD Pipeline)
- UI has no functions yet
- Shuffle has not been started

## Features

- Song, playlist, and player models with logic
- Basic validation for songs and playlists
- JSON-based player loading via Jackson
- CSV playlist import
- File utility helpers for common file operations
- JUnit 5 tests for some classes
- Sample JSON and CSV data in `src/main/resources`

## Dependencies

- Maven
- Swing
- Jackson Databind
- JUnit 5

## Project Structure

```text
src/main/java/musicplaylistshuffler
|-- MusicPlaylistShuffler.java      # Application entry point
|-- model/                          # Player, Playlist, Song, Status, Mode
|-- storage/                        # StorageService facade
|-- ui/                             # Swing UI and action handling
`-- utils/                          # CSV, JSON, and file helpers

src/test/java/musicplaylistshuffler
|-- model/                          # Model unit tests
`-- utils/                          # Utility unit tests
```

## Roadmap

### Kevin

- [ ] Harden CSV and JSON input handling
- [ ] Finish storage and save logic
- [ ] Connect the UI to the player state
- [ ] Display playlists and songs in the UI
- [ ] Add search/filter behavior

### Umut

- [ ] Implement song/player actions such as play, pause, skip, and previous
- [ ] Implement shuffle behavior
- [ ] Display playlists and songs in the UI

### Nice to Have

- [ ] Logging system
- [ ] automated tests?
- [ ] Better user-facing error messages (ErrorDialogs and ErrorHandling)
- [ ] More tests for storage, CSV import, JSON import, and UI-independent player logic
- [ ] Alternatively TUI



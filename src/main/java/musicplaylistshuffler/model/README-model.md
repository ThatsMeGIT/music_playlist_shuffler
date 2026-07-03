## model

**Short Description**
The Model Package is for storing and using all the important Attributes you will need for managing a Music-Playing-App

## Location

```
musicplaylistshuffler/model
```

## Current Contents

# `Playlist.java`

The `Playlist` class is responsible for:

* storing songs in a List<Song>
* showing different statistics about the selected Playlist (Ø-Playtime, summed-up Playtime, Top-Genre)
* enabling and disabling Shuffle
* getting the currently selected List<Song>

## Methods

### `Playlist(String name, List<Song> songs)`

#### Description

Is the main Constructor for every Playlist, containing the name and the list of songs

#### Example

```java
Playlist playlist = new Playlist("PlaylistName",List.of(song1, song2, song3));
```

#### Notes

* JsonProperty for JUnit Tests

### Dependencies

`Playlist()` currently depends on:

* class Song
* enum Mode
* `java.util.List`
* `java.util.ArrayList`


### `averageSongLength()`

#### Description

calculates the average length of every Song from a Playlist

#### Example

```java
Playlist playlist = playlist.averageSongLength();
```

#### Notes

* this is for playlist statistics

### Dependencies

`averageSongLength()` currently depends on:

* `getDuration()` from class Song

### `playlistTimeLength()`

#### Description

calculates the combined time of every Song from a Playlist

#### Example

```java
Playlist playlist = playlist.playlistTimeLength();
```

#### Notes

* this is for playlist statistics

### Dependencies

`playlistTimeLength()` currently depends on:

* `getDuration()` from class Song

### `topGenre()`

#### Description

searches for the most represented genre in a playlist

#### Example

```java
Playlist playlist = playlist.topGenre();
```

#### Notes

* this is for playlist statistics

### Dependencies

`topGenre()` currently depends on:

* `java.util.Map`
* `java.util.HashMap`

### `shuffleON()`

#### Description

activates Shuffle with using the so-called Fisher Yates Shuffle and using it on the currently selected Playlist

#### Example

```java
Mode mode = Mode.SHUFFLED;
```

#### Notes

* takes a random number between our counting index and the playlist size and swaps them

### Dependencies

`shuffleON()` currently depends on:

* `java.util.Random`
* `java.util.ArrayList`
* `java.util.List`
* enum Mode

### `shuffleOFF()`

#### Description

deactivates the shuffle

#### Example

```java
Mode mode = Mode.NORMAL;
```

#### Notes

* /

### Dependencies

`shuffleOFF()` currently depends on:

* enum Mode

### `getCurrentList()`

#### Description

gets the current Mode selected and either puts out the normal Playlist, or the shuffled Version of the Playlist

#### Example

```java
Playlist playlist = playlist.getCurrentList();
```

#### Notes

* /

### Dependencies

`getCurrentList()` currently depends on:

* enum Mode

# `Player.java`

The `Player` class is responsible for:

* manages every Playlist and their contents
* manages play, pause, skip, previous; basically implements simulation features

## Methods

### `Player(int playedSeconds, Song currentSong, List<Playlist> playlists, int currentPlaylistIndex, int currentSongIndex, Status status, Mode mode)`

#### Description

Is the main Constructor for every Playlist, containing the name and the list of songs

#### Example

```java
Player player = new Player(
        30,
        song,
        List.of(playlist),
        0,
        0,
        Status.PLAYING,
        Mode.SHUFFLED
);
```

#### Notes

* JsonProperty for JUnit Tests

### Dependencies

`Player()` currently depends on:

* class Song
* enum Mode
* `java.util.List`
* `java.util.ArrayList`
* class Song
* class Playlist

### `play()`

#### Description

sets the Status to Play so the Song can start

#### Example

```java
Status status = Status.PLAYING;
Player player = player.play();
```

#### Notes

* this is for Player Management

### Dependencies

`play()` currently depends on:

* enum Status

### `pause()`

#### Description

sets the Status to Pause so the Song can be paused

#### Example

```java
Status status = Status.PAUSED;
Player player = player.pause();
```

#### Notes

* this is for Player Management

### Dependencies

`pause()` currently depends on:

* enum Status

### `skip()`

#### Description

skips the Song that's currently Playing and jumps to the next position in Playlist

#### Example

```java
Player player = player.skip();
```

#### Notes

* this is for Player Management

### Dependencies

`skip()` currently depends on:

* `currentSongIndex`
* `getSpecificSong()`

### `previous()`

#### Description

skips the Song that's currently Playing and jumps to the previous position in Playlist

#### Example

```java
Player player = player.previous();
```

#### Notes

* this is for Player Management

### Dependencies

`previous()` currently depends on:

* `currentSongIndex`
* `getSpecificSong()`

### `getCurrentPlaylist()`

#### Description

sets the size of `currentPlaylistIndex` to the Size of the Selected Playlist

#### Example

```java
Playlist currentPlaylist = getCurrentPlaylist();
```

#### Notes

* Also checks if `currentPlaylistIndex` is glitched out

### Dependencies

`getCurrentPlaylist()` currently depends on:

* `currentSongIndex`

# `Song.java`

The `Song` class is responsible for:

* Makes Song Method so a Song is defined (Song is defined by title, artist, duration and genre)
* Implements Basic Song logic

## Methods

### `Song(String title, String artist, int duration, String genre)`
#### Description

Is the Main Constructor and defines of what a Song has to implement

#### Example

```java
Song song = new Song("Title", "Artist", 120, "Pop");
```

#### Notes

* JsonProperty for JUnit Tests

### Dependencies

`Song()` currently depends on:

* /
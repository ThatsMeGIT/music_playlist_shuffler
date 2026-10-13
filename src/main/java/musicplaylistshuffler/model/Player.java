package musicplaylistshuffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int playedSeconds;
    private Song currentSong;
    private List<Playlist> playlists;
    private int currentPlaylistIndex;
    private int currentSongIndex;
    private Status status;
    @JsonProperty("loop")
    private Loop loop;

    public Player(int playedSeconds, Song currentSong, List<Playlist> playlists,
                  int currentPlaylistIndex, int currentSongIndex, Status status) {
        this(playedSeconds, currentSong, playlists, currentPlaylistIndex,
                currentSongIndex, status, Loop.REPEAT_OFF);
    }

    @JsonCreator
    public Player(
            @JsonProperty("playedSeconds") int playedSeconds,
            @JsonProperty("currentSong") Song currentSong,
            @JsonProperty("playlists") List<Playlist> playlists,
            @JsonProperty("currentPlaylistIndex") int currentPlaylistIndex,
            @JsonProperty("currentSongIndex") int currentSongIndex,
            @JsonProperty("status") Status status,
            @JsonProperty("loop") Loop loop
    ) {
        this.playedSeconds = playedSeconds;
        this.currentSong = currentSong;
        this.playlists = playlists != null ? new ArrayList<>(playlists) : null;
        this.currentPlaylistIndex = currentPlaylistIndex;
        this.currentSongIndex = currentSongIndex;
        this.status = status != null ? status : Status.PAUSED;
        this.loop = loop != null ? loop : Loop.REPEAT_OFF;
        if (this.playlists != null && this.playlists.stream().anyMatch(playlist -> playlist == null)) {
            throw new IllegalArgumentException("playlists must not contain null");
        }
        if (this.playlists == null || currentPlaylistIndex < 0
                || currentPlaylistIndex >= this.playlists.size()) {
            this.currentPlaylistIndex = 0;
            clearPlayback();
            return;
        }

        Playlist playlist = this.playlists.get(currentPlaylistIndex);
        int songIndex = currentSong == null ? -1
                : findSongIndex(playlist.getCurrentList(), currentSong, currentSongIndex);
        if (songIndex < 0) {
            clearPlayback();
        } else {
            this.currentSongIndex = songIndex;
            this.currentSong = playlist.getSpecificSong(songIndex);
        }
        if (this.loop == Loop.REPEAT_ON) {
            playlist.repeatOn();
        }
    }

    public void playPlaylist(Playlist playlist) {
        if (playlist == null || playlist.getCurrentList().isEmpty()) {
            return;
        }

        int playlistIndex = playlists == null ? -1 : playlists.indexOf(playlist);
        if (playlistIndex < 0) {
            return;
        }
        currentPlaylistIndex = playlistIndex;
        currentSongIndex = 0;
        currentSong = playlist.getSpecificSong(0);
        playedSeconds = 0;
        loop = playlist.isOnRepeat() ? Loop.REPEAT_ON : Loop.REPEAT_OFF;
        play();
    }

    public void tick() {
        if (status != Status.PLAYING || currentSong == null) {
            return;
        }

        playedSeconds++;

        if (playedSeconds >= currentSong.getDuration()) {
            skip();
        }
    }

    public int getPlayedSeconds() {
        return playedSeconds;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void removePlaylist(Playlist playlist) {
        if (playlists == null) {
            return;
        }
        int removedIndex = playlists.indexOf(playlist);
        if (removedIndex < 0) {
            return;
        }
        playlists.remove(removedIndex);
        if (removedIndex < currentPlaylistIndex) {
            currentPlaylistIndex--;
        } else if (removedIndex == currentPlaylistIndex) {
            currentPlaylistIndex = 0;
            clearPlayback();
            Playlist currentPlaylist = getCurrentPlaylist();
            loop = currentPlaylist != null && currentPlaylist.isOnRepeat()
                    ? Loop.REPEAT_ON : Loop.REPEAT_OFF;
        }
    }

    public boolean addPlaylist(Playlist playlist) {
        if (playlists == null || playlist == null) {
            return false;
        }
        return playlists.add(playlist);
    }

    public void removeSong(Playlist playlist, Song song) {
        if (playlist == null || song == null) {
            return;
        }
        boolean activePlaylist = playlist == getCurrentPlaylist();
        playlist.removeSong(song);
        if (activePlaylist) {
            int songIndex = playlist.getCurrentList().indexOf(currentSong);
            if (songIndex < 0) {
                clearPlayback();
            } else {
                currentSongIndex = songIndex;
            }
        }
    }

    public int getCurrentPlaylistIndex() {
        return currentPlaylistIndex;
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public Status getStatus() {
        return status;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    public void play() {
        status = Status.PLAYING;
    }

    public void pause() {
        status = Status.PAUSED;
    }

    public void repeatAll() {
        Playlist currentPlaylist = getCurrentPlaylist();
        if (currentPlaylist == null) {
            return;
        }

        if (currentPlaylist.isOnRepeat()) {
            currentPlaylist.repeatOff();
            loop = Loop.REPEAT_OFF;
        } else {
            loop = Loop.REPEAT_ON;
            currentPlaylist.repeatOn();
        }
    }

    public void skip() {
        Playlist currentPlaylist = getCurrentPlaylist();

        if (currentPlaylist == null || currentPlaylist.getCurrentList().isEmpty()) {
            clearPlayback();
            return;
        }

        currentSongIndex++;

        if (loop == Loop.REPEAT_OFF && currentSongIndex >= currentPlaylist.getCurrentList().size()) {
            pause();
            currentSongIndex = 0;
        } else if (loop == Loop.REPEAT_ON && currentSongIndex >= currentPlaylist.getCurrentList().size()) {
            currentSongIndex = 0;
            play();
        }

        currentSong = currentPlaylist.getSpecificSong(currentSongIndex);
        playedSeconds = 0;
    }

    public void previous() {
        Playlist currentPlaylist = getCurrentPlaylist();

        if (currentPlaylist == null || currentPlaylist.getCurrentList().isEmpty()) {
            clearPlayback();
            return;
        }

        currentSongIndex--;

        if (currentSongIndex < 0) {
            currentSongIndex = currentPlaylist.getCurrentList().size() - 1;
        }

        currentSong = currentPlaylist.getSpecificSong(currentSongIndex);
        playedSeconds = 0;
    }

    @JsonIgnore
    public Playlist getCurrentPlaylist() {
        if (playlists == null || playlists.isEmpty()) {
            return null;
        }

        if (currentPlaylistIndex < 0 || currentPlaylistIndex >= playlists.size()) {
            currentPlaylistIndex = 0;
        }

        return playlists.get(currentPlaylistIndex);
    }

    public void toggleShuffle() {
        Playlist playlist = getCurrentPlaylist();

        if (playlist == null) {
            return;
        }

        Song oldSong = currentSong;

        if (playlist.isShuffled()) {
            playlist.shuffleOff();
        } else {
            playlist.shuffleOn();
        }

        int songIndex = playlist.getCurrentList().indexOf(oldSong);
        if (songIndex < 0) {
            clearPlayback();
            return;
        }
        currentSongIndex = songIndex;
        currentSong = playlist.getSpecificSong(currentSongIndex);
    }

    private void clearPlayback() {
        currentSong = null;
        currentSongIndex = 0;
        playedSeconds = 0;
        pause();
    }

    private static int findSongIndex(List<Song> songs, Song selected, int preferredIndex) {
        if (preferredIndex >= 0 && preferredIndex < songs.size()
                && sameSongData(songs.get(preferredIndex), selected)) {
            return preferredIndex;
        }
        for (int i = 0; i < songs.size(); i++) {
            if (sameSongData(songs.get(i), selected)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean sameSongData(Song first, Song second) {
        return first.getTitle().equals(second.getTitle())
                && first.getArtist().equals(second.getArtist())
                && first.getDuration() == second.getDuration()
                && first.getGenre().equals(second.getGenre());
    }
}

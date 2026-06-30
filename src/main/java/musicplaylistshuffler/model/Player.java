package musicplaylistshuffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Player {

    private int playedSeconds;
    private Song currentSong;
    private List<Playlist> playlists;
    private int currentPlaylistIndex;
    private int currentSongIndex;
    private Status status;
    private Mode mode;

    @JsonCreator
    public Player(
            @JsonProperty("playedSeconds") int playedSeconds,
            @JsonProperty("currentSong") Song currentSong,
            @JsonProperty("playlists") List<Playlist> playlists,
            @JsonProperty("currentPlaylistIndex") int currentPlaylistIndex,
            @JsonProperty("currentSongIndex") int currentSongIndex,
            @JsonProperty("status") Status status,
            @JsonProperty("mode") Mode mode
    ) {
        this.playedSeconds = playedSeconds;
        this.currentSong = currentSong;
        this.playlists = playlists;
        this.currentPlaylistIndex = currentPlaylistIndex;
        this.currentSongIndex = currentSongIndex;
        this.status = status != null ? status : Status.PAUSED;
        this.mode = mode != null ? mode : Mode.NORMAL;
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

    public int getCurrentPlaylistIndex() {
        return currentPlaylistIndex;
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public Status getStatus() {
        return status;
    }

    public Mode getMode() {
        return mode;
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

    public void skip() {
        Playlist currentPlaylist = getCurrentPlaylist();

        if (currentPlaylist == null || currentPlaylist.getSongs().isEmpty()) {
            currentSong = null;
            playedSeconds = 0;
            return;
        }

        currentSongIndex++;

        if (currentSongIndex >= currentPlaylist.getSongs().size()) {
            currentSongIndex = 0;
        }

        currentSong = currentPlaylist.getSpecificSong(currentSongIndex);
        playedSeconds = 0;
    }

    public void previous() {
        Playlist currentPlaylist = getCurrentPlaylist();

        if (currentPlaylist == null || currentPlaylist.getSongs().isEmpty()) {
            currentSong = null;
            playedSeconds = 0;
            return;
        }

        currentSongIndex--;

        if (currentSongIndex <= 0) {
            currentSongIndex = currentPlaylist.getSongs().size();
        }

        currentSong = currentPlaylist.getSpecificSong(currentSongIndex);
        playedSeconds = 0;
    }

    private Playlist getCurrentPlaylist() {
        if (playlists == null || playlists.isEmpty()) {
            return null;
        }

        if (currentPlaylistIndex < 0 || currentPlaylistIndex >= playlists.size()) {
            currentPlaylistIndex = 0;
        }

        return playlists.get(currentPlaylistIndex);
    }
}
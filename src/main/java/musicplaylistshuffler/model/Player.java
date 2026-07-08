package musicplaylistshuffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Player {

    private int playedSeconds;
    private Song currentSong;
    private List<Playlist> playlists;
    private int currentPlaylistIndex;
    private int currentSongIndex;
    private Status status;
    private Loop loop;

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
        this.playlists = playlists;
        this.currentPlaylistIndex = currentPlaylistIndex;
        this.currentSongIndex = currentSongIndex;
        this.status = status != null ? status : Status.PAUSED;
        this.loop = loop != null ? loop : Loop.REPEAT_OFF;
    }

    public void playPlaylist(Playlist playlist) {
        if (playlist == null || playlist.getCurrentList().isEmpty()) {
            return;
        }

        currentPlaylistIndex = playlists.indexOf(playlist);
        currentSongIndex = 0;
        currentSong = playlist.getSpecificSong(0);
        playedSeconds = 0;
        play();
    }

    public void tick(){
        if(status != Status.PLAYING || currentSong == null){
            return;
        }

        playedSeconds = playedSeconds + 1;

        if (playedSeconds >= currentSong.getDuration()){
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

    public boolean addPlaylist(Playlist playlist){
        if (playlists == null ){
            return false;
        }
        return playlists.add(playlist);
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
        loop = Loop.REPEAT_ON;

        if(playlists == null){
            return;
        }

        if (currentPlaylist.isOnRepeat()) {
            currentPlaylist.repeatOff();
            loop = Loop.REPEAT_OFF;
        } else  {
            currentPlaylist.repeatOn();
        }
    }

    public void skip() {
        Playlist currentPlaylist = getCurrentPlaylist();

        if (currentPlaylist == null || currentPlaylist.getCurrentList().isEmpty()) {
            currentSong = null;
            playedSeconds = 0;
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
            currentSong = null;
            playedSeconds = 0;
            return;
        }

        currentSongIndex--;

        if (currentSongIndex < 0) {
            currentSongIndex = currentPlaylist.getCurrentList().size()-1;
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
        } else  {
            playlist.shuffleOn();
        }

        currentSongIndex = playlist.getCurrentList().indexOf(oldSong);
        currentSong = playlist.getSpecificSong(currentSongIndex);
    }
}

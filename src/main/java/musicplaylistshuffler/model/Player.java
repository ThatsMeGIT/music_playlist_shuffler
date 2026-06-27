package musicplaylistshuffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Status;
import musicplaylistshuffler.model.Mode;

public class Player {

    private int playedSeconds;
    private Song playingSong;
    private List<Playlist> playlists;
    private Status status; // from ENUM Status
    private Mode mode; // from ENUM Mode

    @JsonCreator
    public Player(@JsonProperty("playedSeconds") int playedSeconds,
                  @JsonProperty("playingSong") Song playingSong,
                  @JsonProperty("playlists") List<Playlist> playlists) {
        this.playedSeconds = playedSeconds;
        this.playingSong = playingSong;
        this.playlists = playlists;
        // this.status = status;
        // this.mode = mode;
    }

    public void changeMode() {
        //gui button implementation for mode change
    }

    public void skipSong() {

    }

    public void toggleStatus() {
        //gui button implementation for status change
        if (status == Status.PLAYING) {
            status = Status.PAUSED;
        } else {
            status = Status.PLAYING;
        }
    }

    public void startPlaying() throws InterruptedException {
        status = Status.PLAYING;
        while (status == Status.PLAYING) {
            for (int i = 0; i <= playingSong.getDuration(); i++) {
                System.out.println(i);
                Thread.sleep(100);
            }
            // currentlyPlaying.skipSong();
            status = Status.PAUSED;
        }
    }

    public int getPlayedSeconds() {
        return playedSeconds;
    }

    public Status getStatus() {
        return status;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public Song getCurrentSong() {
        return playingSong;
    }

    public void setCurrentSong(Song playingSong) {
        this.playingSong = playingSong;
    }
}

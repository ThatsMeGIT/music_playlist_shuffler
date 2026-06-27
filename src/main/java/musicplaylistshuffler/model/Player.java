package musicplaylistshuffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Player {

    private int playedSeconds;
    private Song currentSong;
    private List<Playlist> playlists;
    private Status status; // from ENUM Status
    private Mode mode; // from ENUM Mode

    @JsonCreator
    public Player(@JsonProperty("playedSeconds") int playedSeconds,
                  @JsonProperty("currentSong") Song currentSong,
                  @JsonProperty("playlists") List<Playlist> playlists) {
        if (playedSeconds <= 0) {
            throw new IllegalArgumentException("playedSeconds cannot be negative");
        }
        if (currentSong == null) {
            throw new IllegalArgumentException("currentSong cannot be null");
        }
        if (playlists == null) {
            throw new IllegalArgumentException("playlists cannot be null");
        }

        this.playedSeconds = playedSeconds;
        this.currentSong = currentSong;
        this.playlists = playlists;
        // this.status = status;
        // this.mode = mode;
    }

    public void changeMode() {
        //gui button implementation for mode change
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
            for (int i = 0; i <= currentSong.getDuration(); i++) {
                System.out.println(i);
                Thread.sleep(1000);
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
        return currentSong;
    }

}

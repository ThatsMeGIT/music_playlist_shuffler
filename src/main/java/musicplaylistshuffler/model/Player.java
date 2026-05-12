package musicplaylistshuffler.model;

import java.util.List;

public class Player {

    private int playedSeconds;
    private Song playingSong;
    private List<Playlist> playlists;
    private Status status;
    private Mode mode;

    public Player (int playedSeconds, Song playingSong, List<Playlist> playlists, Status status, Mode mode) {
        this.playedSeconds = playedSeconds;
        this.playingSong = playingSong;
        this.playlists = playlists;
        this.status = status;
        this.mode = mode;
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
            for (int i = 0; i <= playingSong.getDuration(); i++) {
                System.out.println(i);
                Thread.sleep(100);
            }
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

    public Song getPlayingSong() {
        return playingSong;
    }

    public void setPlayingSong(Song playingSong) {
        this.playingSong = playingSong;
    }
}

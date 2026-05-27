package musicplaylistshuffler.model;

import java.util.List;

public class Player {

    private int playedSeconds;
    private Song currentSong;
    private List<Playlist> playlists;
    private Status status;
    private Mode mode;


    public Player (int playedSeconds, Song currentSong, List<Playlist> playlists, Status status, Mode mode) {
        this.playedSeconds = playedSeconds;
        this.currentSong = currentSong;
        this.playlists = playlists;
        this.status = status;
        this.mode = mode;
    }

    public void changeMode() {
        //gui button implementation for mode change
        if (mode == Mode.SHUFFLED) {

        }
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
                Thread.sleep(100);
            }
            status = Status.PAUSED;
        }
    }

}

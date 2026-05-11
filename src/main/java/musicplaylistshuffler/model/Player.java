package musicplaylistshuffler.model;

import java.util.List;

public class Player {

    private int playedSeconds;
    private Song playingSong;
    private List<Playlist> playlists;

    enum Status {
        PLAYING,
        PAUSED;
    }
    private Status status;

    enum mode {
        NORMAL,
        SHUFFLE;
    }

    public void changeMode() {

    }

    public void toggleStatus() {
        // hier könnte man noch den Button in der GUI hinzufügen der letzlich den Modus switcht
    }

    public void startPlaying() throws InterruptedException {
        status = Status.PLAYING;
        while (status == Status.PLAYING) {
            for (int i = 1; i < playingSong.getDuration(); i++) {
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

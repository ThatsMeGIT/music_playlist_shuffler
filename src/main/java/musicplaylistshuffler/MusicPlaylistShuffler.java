package musicplaylistshuffler;

import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.ui.MainFrame;

import java.util.List;

public class MusicPlaylistShuffler {
    public static void main(String[] args) {
        Playlist playlist = new Playlist(
                "My Playlist",
                List.of(
                        new Song("Bohemian Rhapsody", "Queen", 355, "Rock"),
                        new Song("Billie Jean", "Michael Jackson", 290, "Pop"),
                        new Song("Stairway to Heaven", "Led Zeppelin", 475, "Rock")
                )
        );

        for (Song song : playlist.getSongs()) {
            System.out.println(song.getTitle() + " | " + song.getArtist() + " | " + song.getDuration() + "s | " + song.getGenre());
        }


        new MainFrame();
    }
}

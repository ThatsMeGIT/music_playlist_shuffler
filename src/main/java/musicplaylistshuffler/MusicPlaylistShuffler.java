package main.java.musicplaylistshuffler;

import main.java.musicplaylistshuffler.model.Playlist;
import main.java.musicplaylistshuffler.model.Song;

import java.util.List;

public class MusicPlaylistShuffler {
    static void main(String[] args) {
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

    }

}

package musicplaylistshuffler;

import com.fasterxml.jackson.core.JsonProcessingException;
import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
<<<<<<< src/main/java/musicplaylistshuffler/MusicPlaylistShuffler.java
import musicplaylistshuffler.ui.MainFrame;
=======
import musicplaylistshuffler.storage.StorageService;
>>>>>>> src/main/java/musicplaylistshuffler/MusicPlaylistShuffler.java

import java.io.IOException;
import java.util.List;

public class MusicPlaylistShuffler {
<<<<<<< src/main/java/musicplaylistshuffler/MusicPlaylistShuffler.java
    public static void main(String[] args) {

        // Here we need to start the program and read all saved playlists in
        // then launch the gui

=======
    public static void main(String[] args){
>>>>>>> src/main/java/musicplaylistshuffler/MusicPlaylistShuffler.java
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

<<<<<<< src/main/java/musicplaylistshuffler/MusicPlaylistShuffler.java

        new MainFrame();
=======
        Player player = StorageService.startPlayer("C:\\Users\\kev\\IdeaProjects\\music_playlist_shuffler\\src\\main\\resources\\test.json");

        if(player == null){
            System.out.println("player ist null");
        }

        System.out.println(player.getPlayingSong().getTitle());
>>>>>>> src/main/java/musicplaylistshuffler/MusicPlaylistShuffler.java
    }
}

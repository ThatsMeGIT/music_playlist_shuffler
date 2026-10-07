package musicplaylistshuffler;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.model.Status;
import musicplaylistshuffler.ui.MainFrame;

import java.util.List;

public class MusicPlaylistShuffler {
    public static void main(String[] args) {

        String path = "src/main/resources/test.json";
        //path = "/data/home/sek43952/IdeaProjects/music_playlist_shuffler/src/main/resources/test.json";
        String s = "src/main/resources/test.json";

        Player player;

        Song song1 = new Song("Test 1", "Artist 1", 5, "Pop");
        Song song2 = new Song("Test 2", "Artist 2", 5, "Pop");

        List<Song> songs = List.of(song1, song2);

        Playlist playlist1 = new Playlist("2. Playlist ", songs);
        Playlist playlist2 = new Playlist("1. Playlist ", songs);

        player = new Player(1, song1, List.of(playlist1, playlist2), 0, 0, Status.PAUSED);

        try {
            //player = StorageService.loadPlayerFromJson(s);
            if(player != null){
                new MainFrame(player, s);
            } else {
                System.out.println("Error while loading Player");
            }
        } catch (Exception e) {
            System.out.println(e);
        }



    }
}

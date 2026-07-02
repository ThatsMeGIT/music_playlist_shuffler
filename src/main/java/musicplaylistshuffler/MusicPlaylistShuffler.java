package musicplaylistshuffler;

import com.fasterxml.jackson.core.JsonProcessingException;
import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.storage.StorageService;
import musicplaylistshuffler.ui.MainFrame;

import java.io.IOException;
import java.util.List;

public class MusicPlaylistShuffler {
    public static void main(String[] args){
        Player player = StorageService.startPlayer("src/main/resources/test.json");
        if(player != null){
            new MainFrame(player);
        } else {
            System.out.println("Error: Reading json File");
        }
    }
}
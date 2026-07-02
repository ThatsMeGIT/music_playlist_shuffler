package musicplaylistshuffler;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.storage.StorageService;
import musicplaylistshuffler.ui.MainFrame;

public class MusicPlaylistShuffler {
    public static void main(String[] args) {

        String path = "src/main/resources/test.json";
        Player player = StorageService.loadPlayerFromJson(path);

        if(player != null){
            new MainFrame(player);
        } else {
            System.out.println("Error while loading Player");
        }

    }
}
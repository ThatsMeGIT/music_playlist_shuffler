package musicplaylistshuffler;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.model.Status;
import musicplaylistshuffler.storage.StorageService;
import musicplaylistshuffler.ui.MainFrame;
import musicplaylistshuffler.ui.UiStyle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.List;

public class MusicPlaylistShuffler {
    public static void main(String[] args) {

        String path = "src/main/resources/test.json";

        Song song1 = new Song("Test 1", "Artist 1", 5, "Pop");
        Song song2 = new Song("Test 2", "Artist 2", 5, "Pop");

        List<Song> songs = List.of(song1, song2);

        Playlist playlist1 = new Playlist("2. Playlist ", songs);
        Playlist playlist2 = new Playlist("1. Playlist ", songs);

        Player fallback = new Player(1, song1, List.of(playlist1, playlist2), 0, 0, Status.PAUSED);
        StorageService.StartupState startup = StorageService.loadPlayerForStartup(path, fallback);
        SwingUtilities.invokeLater(() -> {
            UiStyle.install();
            if (!startup.saveAllowed()) {
                JOptionPane.showMessageDialog(null,
                        "Could not load saved playlists. Starting with sample playlists.\n"
                                + "Automatic saving is disabled to preserve the existing file.",
                        "Could not load playlists", JOptionPane.WARNING_MESSAGE);
            }
            new MainFrame(startup.player(), path, startup.saveAllowed());
        });
    }
}

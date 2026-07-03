package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Status;
import musicplaylistshuffler.storage.StorageService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainActionHandler implements ActionListener {

    private final Player player;
    private final MainFrame mainFrame;

    public MainActionHandler(Player player, MainFrame mainFrame) {
        this.player = player;
        this.mainFrame = mainFrame;
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "previous":
                player.previous();
                break;
            case "pause/play":
                if (player.getStatus() == Status.PAUSED) {
                    player.play();
                } else {
                    player.pause();
                }
                break;
            case "skip":
                player.skip();
                break;
            case "addPlaylist":
                handleAddPlaylist();
                break;
            case "shuffle":
                player.toggleShuffle();
                break;
            default:
                System.out.println("Unknown action: " + e.getActionCommand());
        }


        mainFrame.refreshPlayerView();
    }

    private void handleAddPlaylist() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose your Playlist");

        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV-File (*.csv)", "csv");

        fileChooser.setFileFilter(csvFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(mainFrame);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String playlistName = JOptionPane.showInputDialog(
                mainFrame,
                "Enter playlist name:",
                "Add Playlist",
                JOptionPane.PLAIN_MESSAGE
        );

        if (playlistName == null || playlistName.isBlank()) {
            JOptionPane.showMessageDialog(mainFrame, "Playlist name is required.");
            return;
        }

        String csvPath = fileChooser.getSelectedFile().getAbsolutePath();

        boolean success = StorageService.addNewPlaylistFromCsv(
                player,
                csvPath,
                playlistName
        );

        if (success) {
            JOptionPane.showMessageDialog(mainFrame, "Playlist added successfully.");
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Could not add playlist.");
        }
    }
}
package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Status;
import musicplaylistshuffler.storage.StorageService;
import musicplaylistshuffler.utils.CsvUtils;

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
            case "repeat":
                player.repeatAll();
                System.out.println("[]Action:" + e.getActionCommand());
                break;
            case "previous":
                player.previous();
                System.out.println("[]Action:" + e.getActionCommand());
                break;
            case "pause/play":
                if (player.getStatus() == Status.PAUSED) {
                    player.play();
                    System.out.println("[]Action:" + e.getActionCommand());
                } else {
                    player.pause();
                    System.out.println("[]Action:" + e.getActionCommand());
                }
                break;
            case "skip":
                player.skip();
                System.out.println("[]Action:" + e.getActionCommand());
                break;
            case "addPlaylist":
                handleAddPlaylist();
                System.out.println("[]Action:" + e.getActionCommand());
                break;
            case "shuffle":
                player.toggleShuffle();
                System.out.println("[]Action:" + e.getActionCommand());
                break;
            default:
                System.out.println("[???] Unknown Action: " + e.getActionCommand());
        }


        mainFrame.refreshPlayerView();
    }

    public void actionPerformed(ActionEvent e, Playlist playlist){
        switch (e.getActionCommand()) {
            case "exportPlaylist":
                try {
                    CsvUtils.exportPlaylistToCsv(playlist);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "deletePlaylist":
                player.removePlaylist(playlist);
                break;
                case "playPlaylist":
                    player.playPlaylist(playlist);
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

    private void handleAddSong() {
        JFileChooser songFileChooser = new JFileChooser();
        songFileChooser.setDialogTitle("Choose your Song");

        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV-File (*.csv)", "csv");

        songFileChooser.setFileFilter(csvFilter);
        songFileChooser.setAcceptAllFileFilterUsed(false);

        int result = songFileChooser.showOpenDialog(mainFrame);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String songName = JOptionPane.showInputDialog(
                mainFrame,
                "Enter playlist name:",
                "Add Playlist",
                JOptionPane.PLAIN_MESSAGE
        );

        if (songName == null || songName.isBlank()) {
            JOptionPane.showMessageDialog(mainFrame, "Song name is required.");
            return;
        }

        String csvPath = songFileChooser.getSelectedFile().getAbsolutePath();

        boolean success = StorageService.addNewPlaylistFromCsv(
                player,
                csvPath,
                songName
        );

        if (success) {
            JOptionPane.showMessageDialog(mainFrame, "Song added successfully.");
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Could not add Song.");
        }
    }
}
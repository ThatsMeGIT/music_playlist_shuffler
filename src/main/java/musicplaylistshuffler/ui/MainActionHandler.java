package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Status;

import javax.swing.*;
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
            default:
                System.out.println("Unknown action: " + e.getActionCommand());
        }


        mainFrame.refreshPlayerView();
    }
}
package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Status;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainActionHandler implements ActionListener {

    private final Player player;
    private final MainFrame mainFrame;

    public MainActionHandler(Player player, MainFrame mainFrame){
        this.player = player;
        this.mainFrame = mainFrame;
    }

    public void actionPerformed(ActionEvent e){
        mainFrame.refreshPlayerView();

    }
}

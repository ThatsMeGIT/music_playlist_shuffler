package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainActionHandler implements ActionListener {

    private final Player player;

    public MainActionHandler(Player player){
        this.player = player;
    }

    public void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "previous":
                break;
            case "pause/play":
                //hier methode aufrufen
                System.out.println("Pause/Play");
                break;
            case "next":
                System.out.println("Next");
                break;
            case "new_playlist":
                System.out.println("Add new Playlist");
                break;
            default:
                System.out.println("Error: Action could not be performed");
        }

    }
}

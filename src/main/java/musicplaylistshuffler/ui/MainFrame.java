package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Playlist;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {


    public MainFrame(){
        setTitle("Best music Player");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        //setMinimumSize(900, 600);

        new JTable();

        JPanel playerBar = new JPanel();
        playerBar.setLayout(new BorderLayout());
        playerBar.add(new JLabel("Playing Song"));
        playerBar.add(new JProgressBar(0, 100));
        JPanel controls = new JPanel();
        controls.add(new JButton("Previous"));
        controls.add(new JButton("Play/Pause"));
        controls.add(new JButton("Next"));

        playerBar.add(controls, BorderLayout.SOUTH);


        add(playerBar, BorderLayout.SOUTH);





        pack();
        setVisible(true);

    }
}

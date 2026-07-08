package musicplaylistshuffler.ui;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    public MenuBar(MainActionHandler actionHandler) {
        add(createFileMenu(actionHandler));
    }

    private JMenu createFileMenu(MainActionHandler actionHandler) {
        JMenu menu = new JMenu("File");

        JMenuItem addPlaylistItem = new JMenuItem("Add Playlist");
        addPlaylistItem.setActionCommand("addPlaylist");
        addPlaylistItem.addActionListener(actionHandler);

        JMenuItem addSongItem = new JMenuItem("Add Song");
        addSongItem.setActionCommand("addSong");
        addSongItem.addActionListener(actionHandler);

        menu.add(addPlaylistItem);
        menu.add(addSongItem);

        return menu;
    }
}
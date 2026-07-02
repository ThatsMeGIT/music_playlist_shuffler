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

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setActionCommand("exit");
        exitItem.addActionListener(actionHandler);

        menu.add(addPlaylistItem);
        menu.addSeparator();
        menu.add(exitItem);

        return menu;
    }
}
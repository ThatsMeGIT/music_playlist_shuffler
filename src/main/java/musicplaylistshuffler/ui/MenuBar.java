package musicplaylistshuffler.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;

public class MenuBar extends JMenuBar {

    public MenuBar(MainActionHandler actionHandler) {
        setUI(new BasicMenuBarUI());
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        setBackground(UiStyle.SURFACE);
        add(createFileMenu(actionHandler));
    }

    private JMenu createFileMenu(MainActionHandler actionHandler) {
        JMenu menu = new JMenu("File");
        menu.setUI(new BasicMenuUI());
        menu.setFont(UiStyle.BODY_FONT);
        menu.setForeground(UiStyle.TEXT);
        menu.getPopupMenu().setUI(new BasicPopupMenuUI());
        menu.getPopupMenu().setBackground(UiStyle.SURFACE);
        menu.getPopupMenu().setBorder(BorderFactory.createLineBorder(UiStyle.BORDER));

        JMenuItem addPlaylistItem = new JMenuItem("Add Playlist");
        addPlaylistItem.setActionCommand("addPlaylist");
        addPlaylistItem.addActionListener(actionHandler);

        JMenuItem addSongItem = new JMenuItem("Add Song");
        addSongItem.setActionCommand("addSong");
        addSongItem.addActionListener(actionHandler);

        addPlaylistItem.setUI(new BasicMenuItemUI());
        addSongItem.setUI(new BasicMenuItemUI());
        addPlaylistItem.setForeground(UiStyle.TEXT);
        addSongItem.setForeground(UiStyle.TEXT);
        addPlaylistItem.setBackground(UiStyle.SURFACE);
        addSongItem.setBackground(UiStyle.SURFACE);
        addPlaylistItem.setFont(UiStyle.BODY_FONT);
        addSongItem.setFont(UiStyle.BODY_FONT);
        menu.add(addPlaylistItem);
        menu.add(addSongItem);

        return menu;
    }
}
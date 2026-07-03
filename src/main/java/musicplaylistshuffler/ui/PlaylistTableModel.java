package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;

import javax.swing.table.AbstractTableModel;

public class PlaylistTableModel extends AbstractTableModel {

    private final Player player;
    private static final String[] COLUMNS = {"Name", "Duration (m)", "Created at", "Edited at"};

    public PlaylistTableModel(Player player){
        this.player = player;
    }

    @Override
    public int getRowCount() {
        if (player.getPlaylists() == null) {
            return 0;
        }

        return player.getPlaylists().size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Playlist playlist = player.getPlaylists().get(rowIndex);

        switch (columnIndex){
            case 0:
                return playlist.getName();
            case 1:
                return getTotalDuration(playlist);
            case 2:
                return "";
                //return playlist.getCreatedDate
            case 3:
                //return playlist.getEditedAdd
                return "";
            default:
                return "";
        }
    }

    private int getTotalDuration(Playlist playlist){
        int totalDuration = 0;

        for(Song song : playlist.getSongs()){
            totalDuration = totalDuration + song.getDuration();

        }
        return totalDuration / 60;
    }
}

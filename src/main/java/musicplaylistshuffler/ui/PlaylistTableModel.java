package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.utils.TimeUtils;

import javax.swing.table.AbstractTableModel;

public class PlaylistTableModel extends AbstractTableModel {

    private final Player player;private static final String[] COLUMNS = {"Name", "Songs", "Duration", "Top Genre"};

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
    public boolean isCellEditable(int rowIndex, int columnIndex){
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Playlist playlist = player.getPlaylists().get(rowIndex);

        switch (columnIndex) {
            case 0:
                return playlist.getName();
            case 1:
                return playlist.getSongs().size();
            case 2:
                return TimeUtils.formatDuration(playlist.playlistTimeLength());
            case 3:
                return playlist.topGenre();
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

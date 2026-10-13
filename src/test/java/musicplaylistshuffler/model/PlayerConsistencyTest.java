package musicplaylistshuffler.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerConsistencyTest {
    @Test
    void preservesSongAndProgressWhenTogglingShuffle() {
        Song first = song("First");
        Song second = song("Second");
        Playlist playlist = new Playlist("Favorites", List.of(first, second));
        Player player = new Player(30, second, List.of(playlist), 0, 1, Status.PLAYING);

        player.toggleShuffle();
        assertSame(second, player.getCurrentSong());
        assertSame(second, playlist.getSpecificSong(player.getCurrentSongIndex()));
        assertEquals(30, player.getPlayedSeconds());
        assertEquals(List.of(first, second), playlist.getSongs());

        player.toggleShuffle();
        assertSame(second, player.getCurrentSong());
        assertEquals(1, player.getCurrentSongIndex());
        assertEquals(30, player.getPlayedSeconds());
    }

    @Test
    void preservesActivePlaylistWhenRemovingAnEarlierPlaylist() {
        Song selected = song("Selected");
        Playlist first = new Playlist("First", List.of(song("Other")));
        Playlist active = new Playlist("Active", List.of(selected));
        Player player = new Player(30, selected, List.of(first, active), 1, 0, Status.PLAYING);

        player.removePlaylist(first);

        assertSame(active, player.getCurrentPlaylist());
        assertEquals(0, player.getCurrentPlaylistIndex());
        assertSame(selected, player.getCurrentSong());
        assertEquals(30, player.getPlayedSeconds());
        assertEquals(Status.PLAYING, player.getStatus());
    }

    @Test
    void stopsPlaybackWhenRemovingTheActivePlaylist() {
        Song selected = song("Selected");
        Playlist active = new Playlist("Active", List.of(selected));
        Player player = new Player(30, selected, List.of(active), 0, 0, Status.PLAYING);

        player.removePlaylist(active);

        assertNull(player.getCurrentPlaylist());
        assertStopped(player);
        assertDoesNotThrow(player::tick);
        assertDoesNotThrow(player::skip);
        assertDoesNotThrow(player::previous);
        assertDoesNotThrow(player::toggleShuffle);
    }

    @Test
    void preservesPlaybackWhenRemovingALaterPlaylist() {
        Song selected = song("Selected");
        Playlist active = new Playlist("Active", List.of(selected));
        Playlist other = new Playlist("Other", List.of(song("Other")));
        Player player = new Player(30, selected, List.of(active, other), 0, 0, Status.PLAYING);

        player.removePlaylist(other);

        assertSame(active, player.getCurrentPlaylist());
        assertSame(selected, player.getCurrentSong());
        assertEquals(30, player.getPlayedSeconds());
        assertEquals(Status.PLAYING, player.getStatus());
    }

    @Test
    void ignoresPlaybackOfAnUnregisteredPlaylist() {
        Song selected = song("Selected");
        Playlist active = new Playlist("Active", List.of(selected));
        Player player = new Player(30, selected, List.of(active), 0, 0, Status.PAUSED);

        player.playPlaylist(new Playlist("Other", List.of(song("Other"))));

        assertSame(active, player.getCurrentPlaylist());
        assertSame(selected, player.getCurrentSong());
        assertEquals(30, player.getPlayedSeconds());
        assertEquals(Status.PAUSED, player.getStatus());
    }

    @Test
    void acceptsPlaylistChangesWithAnImmutableConstructorList() {
        Playlist first = new Playlist("First", List.of(song("First")));
        Playlist second = new Playlist("Second", List.of(song("Second")));
        List<Playlist> original = List.of(first);
        Player player = new Player(0, null, original, 0, 0, Status.PAUSED);

        assertTrue(player.addPlaylist(second));
        assertFalse(player.addPlaylist(null));
        player.removePlaylist(first);

        assertEquals(List.of(second), player.getPlaylists());
        assertEquals(List.of(first), original);
    }

    @Test
    void preservesSelectedSongWhenRemovingAnotherSongDuringShuffle() {
        Song first = song("First");
        Song selected = song("Selected");
        Playlist playlist = new Playlist("Favorites", List.of(first, selected));
        Player player = new Player(30, selected, List.of(playlist), 0, 1, Status.PLAYING);
        player.toggleShuffle();

        player.removeSong(playlist, first);

        assertSame(selected, player.getCurrentSong());
        assertSame(selected, playlist.getSpecificSong(player.getCurrentSongIndex()));
        assertEquals(30, player.getPlayedSeconds());
        assertEquals(Status.PLAYING, player.getStatus());
    }

    @Test
    void stopsPlaybackWhenRemovingTheSelectedSong() {
        Song selected = song("Selected");
        Playlist playlist = new Playlist("Favorites", List.of(selected));
        Player player = new Player(30, selected, List.of(playlist), 0, 0, Status.PLAYING);

        player.removeSong(playlist, selected);

        assertStopped(player);
        assertTrue(playlist.getSongs().isEmpty());
    }

    @Test
    void adoptsRepeatSettingWhenSwitchingPlaylists() {
        Song first = song("First");
        Song second = song("Second");
        Playlist repeating = new Playlist("Repeating", List.of(first));
        Playlist normal = new Playlist("Normal", List.of(second));
        Player player = new Player(0, first, List.of(repeating, normal), 0, 0, Status.PLAYING);
        player.repeatAll();
        player.skip();
        assertEquals(Status.PLAYING, player.getStatus());

        player.playPlaylist(normal);
        player.skip();
        assertEquals(Status.PAUSED, player.getStatus());

        player.playPlaylist(repeating);
        player.skip();
        assertEquals(Status.PLAYING, player.getStatus());
    }

    @Test
    void advancesOnSongEndAndStopsAtPlaylistEnd() {
        Song first = song("First");
        Song second = song("Second");
        Playlist playlist = new Playlist("Favorites", List.of(first, second));
        Player player = new Player(59, first, List.of(playlist), 0, 0, Status.PLAYING);

        player.tick();
        assertSame(second, player.getCurrentSong());
        assertEquals(1, player.getCurrentSongIndex());
        assertEquals(0, player.getPlayedSeconds());

        for (int i = 0; i < second.getDuration(); i++) {
            player.tick();
        }
        assertEquals(Status.PAUSED, player.getStatus());
        assertSame(first, player.getCurrentSong());
        assertEquals(0, player.getCurrentSongIndex());
        assertEquals(0, player.getPlayedSeconds());
    }

    private static void assertStopped(Player player) {
        assertNull(player.getCurrentSong());
        assertEquals(0, player.getCurrentSongIndex());
        assertEquals(0, player.getPlayedSeconds());
        assertEquals(Status.PAUSED, player.getStatus());
    }

    private static Song song(String title) {
        return new Song(title, "Artist", 60, "Pop");
    }
}

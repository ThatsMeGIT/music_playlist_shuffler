package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.model.Status;
import musicplaylistshuffler.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.SwingUtilities;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CloseHandlerTest {
    @TempDir
    Path tempDir;

    @Test
    void savesBeforeClosingAndHandlesRepeatedCloseRequests() throws Exception {
        Path path = tempDir.resolve("player.json");
        Session session = new Session(player(Status.PLAYING), path, true);

        SwingUtilities.invokeAndWait(() -> {
            session.handler.requestClose();
            session.handler.requestClose();
        });
        session.awaitCompletion();

        assertEquals(1, session.closes);
        assertEquals(0, session.errors);
        assertEquals(List.of(true), session.savingStates);
        Player saved = JsonUtils.loadPlayerFromJson(path.toString());
        assertEquals("Title", saved.getCurrentSong().getTitle());
        assertEquals(30, saved.getPlayedSeconds());
        assertEquals(Status.PAUSED, saved.getStatus());
        assertTrue(session.callbacksOnEdt);
    }

    @Test
    void keepsWindowOpenAndRestoresPlaybackWhenSavingFails() throws Exception {
        Player player = player(Status.PLAYING);
        Session session = new Session(player, tempDir, true);

        session.requestAndAwait();

        assertEquals(0, session.closes);
        assertEquals(1, session.errors);
        assertEquals(List.of(true, false), session.savingStates);
        assertEquals(Status.PLAYING, player.getStatus());
        assertEquals(30, player.getPlayedSeconds());
        assertTrue(session.callbacksOnEdt);
    }

    @Test
    void retriesSavingAfterFailure() throws Exception {
        Path folder = tempDir.resolve("missing");
        Path path = folder.resolve("player.json");
        Session session = new Session(player(Status.PAUSED), path, true);
        session.requestAndAwait();
        assertEquals(0, session.closes);
        assertEquals(1, session.errors);

        Files.createDirectory(folder);
        session.requestAndAwait();

        assertEquals(1, session.closes);
        assertEquals(1, session.errors);
        assertEquals("Title", JsonUtils.loadPlayerFromJson(path.toString()).getCurrentSong().getTitle());
        assertTrue(session.callbacksOnEdt);
    }

    @Test
    void preservesPausedStatusAfterSaveFailure() throws Exception {
        Player player = player(Status.PAUSED);
        Session session = new Session(player, tempDir, true);

        session.requestAndAwait();

        assertEquals(Status.PAUSED, player.getStatus());
        assertEquals(0, session.closes);
        assertEquals(1, session.errors);
    }

    @Test
    void preservesProtectedFileWhenAutomaticSavingIsDisabled() throws Exception {
        Path path = tempDir.resolve("broken.json");
        String contents = "{broken JSON";
        Files.writeString(path, contents);
        Session session = new Session(player(Status.PAUSED), path, false);

        session.requestAndAwait();

        assertEquals(contents, Files.readString(path));
        assertEquals(1, session.closes);
        assertEquals(0, session.errors);
        assertTrue(session.savingStates.isEmpty());
    }

    private static Player player(Status status) {
        Song song = new Song("Title", "Artist", 180, "Pop");
        return new Player(30, song, List.of(new Playlist("Favorites", List.of(song))), 0, 0, status);
    }

    private static class Session {
        private final Semaphore completed = new Semaphore(0);
        private final List<Boolean> savingStates = new ArrayList<>();
        private final CloseHandler handler;
        private int closes;
        private int errors;
        private boolean callbacksOnEdt = true;

        Session(Player player, Path path, boolean saveOnClose) {
            handler = new CloseHandler(player, path.toString(), saveOnClose, () -> {
                callbacksOnEdt &= SwingUtilities.isEventDispatchThread();
                closes++;
                completed.release();
            }, () -> {
                callbacksOnEdt &= SwingUtilities.isEventDispatchThread();
                errors++;
                completed.release();
            }, saving -> {
                callbacksOnEdt &= SwingUtilities.isEventDispatchThread();
                savingStates.add(saving);
            });
        }

        void requestAndAwait() throws Exception {
            SwingUtilities.invokeAndWait(handler::requestClose);
            awaitCompletion();
        }

        void awaitCompletion() throws InterruptedException {
            assertTrue(completed.tryAcquire(5, TimeUnit.SECONDS), "Close operation did not complete");
        }
    }
}

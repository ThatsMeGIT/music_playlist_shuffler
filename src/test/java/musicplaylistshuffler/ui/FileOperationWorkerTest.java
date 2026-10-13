package musicplaylistshuffler.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.utils.CsvUtils;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class FileOperationWorkerTest {
    @TempDir
    Path tempDir;

    @Test
    void handlesInvalidCsvImportWithoutEscapingToTheEdt() throws Exception {
        Path path = tempDir.resolve("invalid.csv");
        Files.writeString(path, "title;artist;duration;genre\nSong;Artist;invalid;Pop\n");

        assertFailure(() -> CsvUtils.loadSongsFromCsv(path.toString()));
    }

    @Test
    void handlesCsvExportToMissingDirectoryWithoutEscapingToTheEdt() throws Exception {
        Playlist playlist = new Playlist(tempDir.resolve("missing/export").toString(), List.of());

        assertFailure(() -> {
            CsvUtils.exportPlaylistToCsv(playlist);
            return true;
        });
    }

    private static void assertFailure(Callable<?> operation) throws Exception {
        CountDownLatch completed = new CountDownLatch(1);
        AtomicBoolean succeeded = new AtomicBoolean(false);
        AtomicBoolean failureOnEdt = new AtomicBoolean(false);
        SwingUtilities.invokeAndWait(() -> new FileOperationWorker<>(operation, result -> {
            succeeded.set(true);
            completed.countDown();
        }, () -> {
            failureOnEdt.set(SwingUtilities.isEventDispatchThread());
            completed.countDown();
        }).execute());

        assertTrue(completed.await(5, TimeUnit.SECONDS));
        assertFalse(succeeded.get());
        assertTrue(failureOnEdt.get());
    }

    @Test
    void runsOperationOffEdtAndDeliversResultOnEdt() throws Exception {
        CountDownLatch completed = new CountDownLatch(1);
        AtomicBoolean operationOnEdt = new AtomicBoolean(true);
        AtomicBoolean callbackOnEdt = new AtomicBoolean(false);
        AtomicReference<String> result = new AtomicReference<>();
        AtomicBoolean failed = new AtomicBoolean(false);

        SwingUtilities.invokeAndWait(() -> new FileOperationWorker<>(() -> {
            operationOnEdt.set(SwingUtilities.isEventDispatchThread());
            return "playlist data";
        }, value -> {
            callbackOnEdt.set(SwingUtilities.isEventDispatchThread());
            result.set(value);
            completed.countDown();
        }, () -> {
            failed.set(true);
            completed.countDown();
        }).execute());

        assertTrue(completed.await(5, TimeUnit.SECONDS));
        assertFalse(operationOnEdt.get());
        assertTrue(callbackOnEdt.get());
        assertEquals("playlist data", result.get());
        assertFalse(failed.get());
    }

    @Test
    void reportsFileExceptionOnEdtWithoutCallingSuccess() throws Exception {
        CountDownLatch completed = new CountDownLatch(1);
        AtomicBoolean succeeded = new AtomicBoolean(false);
        AtomicBoolean failureOnEdt = new AtomicBoolean(false);

        SwingUtilities.invokeAndWait(() -> new FileOperationWorker<>(() -> {
            throw new IOException("Internal details must not reach the user");
        }, result -> {
            succeeded.set(true);
            completed.countDown();
        }, () -> {
            failureOnEdt.set(SwingUtilities.isEventDispatchThread());
            completed.countDown();
        }).execute());

        assertTrue(completed.await(5, TimeUnit.SECONDS));
        assertFalse(succeeded.get());
        assertTrue(failureOnEdt.get());
    }

    @Test
    void reportsInvalidInputExceptionWithoutCallingSuccess() throws Exception {
        CountDownLatch completed = new CountDownLatch(1);
        AtomicBoolean succeeded = new AtomicBoolean(false);
        AtomicBoolean failed = new AtomicBoolean(false);

        SwingUtilities.invokeAndWait(() -> new FileOperationWorker<>(() -> {
            throw new IllegalArgumentException("Invalid input");
        }, result -> {
            succeeded.set(true);
            completed.countDown();
        }, () -> {
            failed.set(true);
            completed.countDown();
        }).execute());

        assertTrue(completed.await(5, TimeUnit.SECONDS));
        assertFalse(succeeded.get());
        assertTrue(failed.get());
    }
}

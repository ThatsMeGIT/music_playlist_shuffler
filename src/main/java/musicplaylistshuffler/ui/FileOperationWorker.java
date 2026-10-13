package musicplaylistshuffler.ui;

import javax.swing.SwingWorker;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/** Runs file operations in the background and delivers their outcome on the EDT. */
class FileOperationWorker<T> extends SwingWorker<T, Void> {
    private final Callable<T> operation;
    private final Consumer<T> onSuccess;
    private final Runnable onFailure;

    FileOperationWorker(Callable<T> operation, Consumer<T> onSuccess, Runnable onFailure) {
        this.operation = operation;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
    }

    @Override
    protected T doInBackground() throws Exception {
        return operation.call();
    }

    @Override
    protected void done() {
        T result;
        try {
            result = get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            onFailure.run();
            return;
        } catch (ExecutionException | CancellationException e) {
            onFailure.run();
            return;
        }
        onSuccess.accept(result);
    }
}

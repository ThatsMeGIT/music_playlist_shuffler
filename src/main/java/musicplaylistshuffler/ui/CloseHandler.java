package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Status;
import musicplaylistshuffler.storage.StorageService;

import java.util.function.Consumer;

/** Closes a session only after its data has been saved successfully. */
class CloseHandler {
    private final Player player;
    private final String path;
    private final boolean saveOnClose;
    private final Runnable closeWindow;
    private final Runnable showSaveError;
    private final Consumer<Boolean> setSaving;
    private boolean saving;

    CloseHandler(Player player, String path, boolean saveOnClose, Runnable closeWindow,
                 Runnable showSaveError, Consumer<Boolean> setSaving) {
        this.player = player;
        this.path = path;
        this.saveOnClose = saveOnClose;
        this.closeWindow = closeWindow;
        this.showSaveError = showSaveError;
        this.setSaving = setSaving;
    }

    void requestClose() {
        if (saving) {
            return;
        }
        if (!saveOnClose) {
            closeWindow.run();
            return;
        }
        Status previousStatus = player.getStatus();
        player.pause();
        saving = true;
        setSaving.accept(true);
        Runnable onFailure = () -> {
            saving = false;
            setSaving.accept(false);
            if (previousStatus == Status.PLAYING) {
                player.play();
            }
            showSaveError.run();
        };
        new FileOperationWorker<>(() -> StorageService.savePlayerToJson(player, path), success -> {
            if (Boolean.TRUE.equals(success)) {
                closeWindow.run();
            } else {
                onFailure.run();
            }
        }, onFailure).execute();
    }
}

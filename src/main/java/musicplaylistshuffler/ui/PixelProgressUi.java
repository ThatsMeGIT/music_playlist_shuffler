package musicplaylistshuffler.ui;

import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.*;
import java.awt.*;

/** Read-only playback indicator: leaf-green progress remains readable when disabled. */
final class PixelProgressUi extends BasicProgressBarUI {
    @Override
    protected void paintDeterminate(Graphics graphics, JComponent component) {
        int width = component.getWidth();
        int height = component.getHeight();
        graphics.setColor(UiStyle.BORDER);
        graphics.fillRect(0, height - 5, width, 4);
        graphics.setColor(UiStyle.ACCENT);
        graphics.fillRect(0, height - 5, (int) (width * progressBar.getPercentComplete()), 4);
        if (progressBar.isStringPainted()) {
            String text = progressBar.getString();
            graphics.setFont(UiStyle.BODY_FONT.deriveFont(12f));
            graphics.setColor(UiStyle.MUTED);
            FontMetrics metrics = graphics.getFontMetrics();
            graphics.drawString(text, Math.max(0, (width - metrics.stringWidth(text)) / 2), metrics.getAscent());
        }
    }
}

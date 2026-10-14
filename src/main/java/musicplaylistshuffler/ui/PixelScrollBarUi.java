package musicplaylistshuffler.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/** Flat dark scrollbars retain arrow buttons, dragging, keyboard and wheel navigation. */
final class PixelScrollBarUi extends BasicScrollBarUI {
    @Override
    protected JButton createDecreaseButton(int direction) { return arrow(direction); }

    @Override
    protected JButton createIncreaseButton(int direction) { return arrow(direction); }

    private JButton arrow(int direction) {
        JButton button = new BasicArrowButton(direction, UiStyle.SURFACE, UiStyle.BORDER,
                UiStyle.MUTED, UiStyle.SURFACE);
        button.setBorder(BorderFactory.createEmptyBorder());
        return button;
    }

    @Override
    protected void paintTrack(Graphics graphics, JComponent component, Rectangle bounds) {
        graphics.setColor(UiStyle.SURFACE);
        graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    protected void paintThumb(Graphics graphics, JComponent component, Rectangle bounds) {
        graphics.setColor(UiStyle.BORDER);
        graphics.fillRect(bounds.x + 2, bounds.y + 2, Math.max(0, bounds.width - 4), Math.max(0, bounds.height - 4));
    }
}

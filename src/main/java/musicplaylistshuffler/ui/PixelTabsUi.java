package musicplaylistshuffler.ui;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

/** Quiet square tabs; the active tab has a leaf-green underline. */
final class PixelTabsUi extends BasicTabbedPaneUI {
    @Override
    protected void paintTabBackground(Graphics g, int placement, int index, int x, int y,
                                      int width, int height, boolean selected) {
        g.setColor(selected ? UiStyle.SURFACE : UiStyle.BACKGROUND);
        g.fillRect(x, y, width, height);
    }

    @Override
    protected void paintTabBorder(Graphics g, int placement, int index, int x, int y,
                                  int width, int height, boolean selected) {
        g.setColor(selected ? UiStyle.ACCENT : UiStyle.BORDER);
        g.fillRect(x, y + height - 2, width, 2);
    }

    @Override
    protected void paintContentBorder(Graphics g, int placement, int selectedIndex) {
        // Panels own their spacing and borders; omit the look-and-feel bevel.
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int placement, Rectangle[] rectangles, int index,
                                      Rectangle icon, Rectangle text, boolean selected) {
        if (tabPane.hasFocus() && selected) {
            Rectangle bounds = rectangles[index];
            g.setColor(UiStyle.ACCENT);
            g.drawRect(bounds.x + 3, bounds.y + 3, bounds.width - 7, bounds.height - 7);
        }
    }
}

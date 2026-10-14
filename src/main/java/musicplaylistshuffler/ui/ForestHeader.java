package musicplaylistshuffler.ui;

import javax.swing.*;
import java.awt.*;

/** Decorative forest built on a four-pixel grid. Rendering has no timers or external assets. */
final class ForestHeader extends JPanel {
    ForestHeader() {
        super(new BorderLayout(UiStyle.GAP, 0));
        setBackground(UiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setPreferredSize(new Dimension(0, 108));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics g = graphics.create();
        int unit = 4;
        int width = getWidth() / unit;
        int height = getHeight() / unit;
        g.setColor(new Color(0x142A1E));
        for (int x = width / 3; x < width; x += 9) {
            tree(g, x, height - 6, 8 + x % 7, unit);
        }
        g.setColor(new Color(0x09160F));
        for (int x = width / 2; x < width; x += 12) {
            tree(g, x, height - 3, 10 + x % 9, unit);
        }
        // A small crescent and quiet reflections leave the text area unobstructed.
        int moonX = width * 3 / 4;
        g.setColor(new Color(0x8B9C69));
        g.fillRect(moonX * unit, 3 * unit, 3 * unit, 5 * unit);
        g.fillRect((moonX - 1) * unit, 4 * unit, unit, 3 * unit);
        g.setColor(UiStyle.BACKGROUND);
        g.fillRect((moonX + 1) * unit, 3 * unit, 3 * unit, 4 * unit);
        g.setColor(new Color(0x3F573A));
        for (int x = width / 2; x < width - 3; x += 17) {
            g.fillRect(x * unit, (height - 3) * unit, unit, unit);
            g.fillRect((x - 1) * unit, (height - 1) * unit, 3 * unit, unit);
        }
        g.dispose();
    }

    private void tree(Graphics g, int x, int base, int height, int unit) {
        for (int level = 0; level < height; level++) {
            int halfWidth = level / 3;
            g.fillRect((x - halfWidth) * unit, (base - height + level) * unit,
                    (halfWidth * 2 + 1) * unit, unit);
        }
        g.fillRect(x * unit, base * unit, unit, 2 * unit);
    }
}

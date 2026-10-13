package musicplaylistshuffler.ui;

import javax.swing.*;
import java.awt.*;

/** Small, code-drawn bitmap motifs; no platform-dependent symbol fonts. */
final class PixelIcon implements Icon {
    enum Kind { NOTE, COVER, PLAY, PAUSE, PREVIOUS, NEXT, SHUFFLE, REPEAT, IMPORT }

    private final Kind kind;
    private final int size;

    PixelIcon(Kind kind, int size) {
        this.kind = kind;
        this.size = size;
    }

    @Override
    public int getIconWidth() { return size; }

    @Override
    public int getIconHeight() { return size; }

    @Override
    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        Graphics g = graphics.create(x, y, size, size);
        Color ink = component.getForeground();
        if (component instanceof AbstractButton button
                && (button.isSelected() || Boolean.TRUE.equals(button.getClientProperty("pixel.primary")))) {
            ink = UiStyle.BACKGROUND;
        }
        int unit = Math.max(1, size / (kind == Kind.COVER ? 10 : 8));
        if (kind == Kind.COVER) {
            g.setColor(UiStyle.SURFACE);
            g.fillRect(0, 0, size, size);
            g.setColor(UiStyle.BORDER);
            g.drawRect(0, 0, size - 1, size - 1);
            g.drawRect(3, 3, size - 7, size - 7);
            g.setColor(UiStyle.TEXT);
            g.fillRect(unit * 3, unit, unit * 4, unit * 8);
            g.fillRect(unit * 2, unit * 2, unit * 6, unit * 6);
            g.fillRect(unit, unit * 3, unit * 8, unit * 4);
            ink = UiStyle.SURFACE;
        }
        g.setColor(component.isEnabled() ? ink : UiStyle.MUTED);
        String[] pattern = switch (kind) {
            case NOTE, COVER -> new String[]{"00011110", "00010010", "00010010", "00010010",
                    "00010010", "01110010", "01100110", "00000110"};
            case PLAY -> new String[]{"01000000", "01100000", "01110000", "01111000",
                    "01111000", "01110000", "01100000", "01000000"};
            case PAUSE -> new String[]{"01100110", "01100110", "01100110", "01100110",
                    "01100110", "01100110", "01100110", "01100110"};
            case PREVIOUS -> new String[]{"01000010", "01000110", "01001110", "01011110",
                    "01011110", "01001110", "01000110", "01000010"};
            case NEXT -> new String[]{"01000010", "01100010", "01110010", "01111010",
                    "01111010", "01110010", "01100010", "01000010"};
            case SHUFFLE -> new String[]{"00000100", "11000110", "00100111", "00011000",
                    "00011000", "00100111", "11000110", "00000100"};
            case REPEAT -> new String[]{"00111100", "01000010", "11000010", "11100010",
                    "01000111", "01000011", "01000010", "00111100"};
            case IMPORT -> new String[]{"00011000", "00111100", "01111110", "00011000",
                    "00011000", "00000000", "01000010", "01111110"};
        };
        int offset = (size - 8 * unit) / 2;
        for (int row = 0; row < pattern.length; row++) {
            for (int col = 0; col < pattern[row].length(); col++) {
                if (pattern[row].charAt(col) == '1') {
                    g.fillRect(offset + col * unit, offset + row * unit, unit, unit);
                }
            }
        }
        g.dispose();
    }
}

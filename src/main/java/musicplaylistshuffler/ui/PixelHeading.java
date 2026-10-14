package musicplaylistshuffler.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Map;

/** Bitmap lettering for short, fixed brand headings; body text uses a standard font. */
final class PixelHeading extends JLabel {
    private static final Map<Character, String> GLYPHS = Map.ofEntries(
            Map.entry('A', "01110/10001/10001/11111/10001/10001/10001"),
            Map.entry('B', "11110/10001/10001/11110/10001/10001/11110"),
            Map.entry('C', "01111/10000/10000/10000/10000/10000/01111"),
            Map.entry('D', "11110/10001/10001/10001/10001/10001/11110"),
            Map.entry('E', "11111/10000/10000/11110/10000/10000/11111"),
            Map.entry('F', "11111/10000/10000/11110/10000/10000/10000"),
            Map.entry('G', "01111/10000/10000/10111/10001/10001/01110"),
            Map.entry('H', "10001/10001/10001/11111/10001/10001/10001"),
            Map.entry('I', "11111/00100/00100/00100/00100/00100/11111"),
            Map.entry('J', "00111/00010/00010/00010/00010/10010/01100"),
            Map.entry('K', "10001/10010/10100/11000/10100/10010/10001"),
            Map.entry('L', "10000/10000/10000/10000/10000/10000/11111"),
            Map.entry('M', "10001/11011/10101/10101/10001/10001/10001"),
            Map.entry('N', "10001/11001/11001/10101/10011/10011/10001"),
            Map.entry('O', "01110/10001/10001/10001/10001/10001/01110"),
            Map.entry('P', "11110/10001/10001/11110/10000/10000/10000"),
            Map.entry('Q', "01110/10001/10001/10001/10101/10010/01101"),
            Map.entry('R', "11110/10001/10001/11110/10100/10010/10001"),
            Map.entry('S', "01111/10000/10000/01110/00001/00001/11110"),
            Map.entry('T', "11111/00100/00100/00100/00100/00100/00100"),
            Map.entry('U', "10001/10001/10001/10001/10001/10001/01110"),
            Map.entry('V', "10001/10001/10001/10001/10001/01010/00100"),
            Map.entry('W', "10001/10001/10001/10101/10101/11011/10001"),
            Map.entry('X', "10001/10001/01010/00100/01010/10001/10001"),
            Map.entry('Y', "10001/10001/01010/00100/00100/00100/00100"),
            Map.entry('Z', "11111/00001/00010/00100/01000/10000/11111"));
    private final int scale;

    PixelHeading(String text, int scale) {
        super(text);
        this.scale = scale;
        setForeground(UiStyle.TEXT);
        getAccessibleContext().setAccessibleName(text);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Math.max(0, getText().length() * 6 - 1) * scale, 9 * scale);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics g = graphics.create();
        g.setColor(getForeground());
        String text = getText().toUpperCase(Locale.ROOT);
        int top = Math.max(0, (getHeight() - 7 * scale) / 2);
        for (int index = 0; index < text.length(); index++) {
            char letter = text.charAt(index);
            if (letter == ' ') {
                continue;
            }
            String[] rows = GLYPHS.getOrDefault(letter, "01110/10001/00010/00100/00100/00000/00100").split("/");
            for (int row = 0; row < rows.length; row++) {
                for (int col = 0; col < rows[row].length(); col++) {
                    if (rows[row].charAt(col) == '1') {
                        g.fillRect((index * 6 + col) * scale, top + row * scale, scale, scale);
                    }
                }
            }
        }
        g.dispose();
    }
}

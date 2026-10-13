package musicplaylistshuffler.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/** Shared forest theme and component styling. See STYLING.md for usage and design rules. */
public final class UiStyle {
    static final Color BACKGROUND = new Color(0x0C1812);
    static final Color SURFACE = new Color(0x14271D);
    static final Color TEXT = new Color(0xE7E7D3);
    static final Color MUTED = new Color(0xA5B39B);
    static final Color ACCENT = new Color(0xA8CC78);
    static final Color BORDER = new Color(0x34513E);
    static final Color ALTERNATE_ROW = new Color(0x102219);
    static final Color SELECTION = new Color(0x20382B);
    static final Color DANGER = new Color(0xE5A18C);

    static final int GAP = 12;
    static final int PADDING = 20;
    static final int ROW_HEIGHT = 36;
    static final Font BODY_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 16);

    private UiStyle() {
    }

    /** Install once on the EDT, before constructing any windows or dialogs. */
    public static void install() {
        for (UIManager.LookAndFeelInfo lookAndFeel : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(lookAndFeel.getName())) {
                try {
                    UIManager.setLookAndFeel(lookAndFeel.getClassName());
                } catch (UnsupportedLookAndFeelException | ClassNotFoundException
                         | InstantiationException | IllegalAccessException ignored) {
                    // Retain the current look and feel if Nimbus is unavailable.
                }
                break;
            }
        }
        UIManager.put("MenuBarUI", "javax.swing.plaf.basic.BasicMenuBarUI");
        UIManager.put("MenuBar.background", SURFACE);
        UIManager.put("OptionPane.messageFont", BODY_FONT);
        UIManager.put("OptionPane.buttonFont", BODY_FONT);
        UIManager.put("ButtonUI", PixelButtonUi.class.getName());
        UIManager.put("ToggleButtonUI", PixelButtonUi.class.getName());
        UIManager.put("defaultFont", BODY_FONT);
        for (String component : new String[]{"Label", "Button", "ToggleButton", "Table", "TableHeader",
                "Menu", "MenuItem", "TabbedPane", "TextField", "TextArea", "OptionPane", "ComboBox"}) {
            UIManager.put(component + ".font", BODY_FONT);
        }
        UIManager.put("control", BACKGROUND);
        UIManager.put("text", TEXT);
        UIManager.put("nimbusBase", SURFACE);
        UIManager.put("nimbusBlueGrey", SURFACE);
        UIManager.put("nimbusLightBackground", SURFACE);
        UIManager.put("nimbusBorder", BORDER);
        UIManager.put("nimbusOrange", ACCENT);
        UIManager.put("ProgressBar[Disabled].textForeground", TEXT);
        UIManager.put("nimbusFocus", ACCENT);
        UIManager.put("nimbusSelectionBackground", ACCENT);
        UIManager.put("nimbusSelectedText", BACKGROUND);
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("textForeground", TEXT);
        UIManager.put("textBackground", SURFACE);
        UIManager.put("nimbusDisabledText", MUTED);
        for (String component : new String[]{"Label", "Button", "ToggleButton", "Menu", "MenuItem",
                "TabbedPane", "TextField", "TextArea", "ComboBox", "List", "Tree", "ToolTip", "OptionPane"}) {
            UIManager.put(component + ".foreground", TEXT);
            UIManager.put(component + ".background", SURFACE);
            UIManager.put(component + ".disabledText", MUTED);
        }
        UIManager.put("TextField.selectionBackground", ACCENT);
        UIManager.put("TextField.selectionForeground", BACKGROUND);
        UIManager.put("TextField.caretForeground", TEXT);
        UIManager.put("TabbedPane.contentAreaColor", BACKGROUND);
        UIManager.put("TabbedPane.selected", SURFACE);
        UIManager.put("TabbedPane.focus", ACCENT);
        UIManager.put("Menu.selectionBackground", SELECTION);
        UIManager.put("Menu.selectionForeground", TEXT);
        UIManager.put("MenuItem.selectionBackground", SELECTION);
        UIManager.put("MenuItem.selectionForeground", TEXT);
    }

    static JPanel panel(LayoutManager layout, Color background) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(background);
        return panel;
    }

    static JLabel heading(String text, float size) {
        JLabel label = new JLabel(text);
        label.setFont(BODY_FONT.deriveFont(Font.BOLD, size));
        label.setForeground(TEXT);
        return label;
    }

    static JLabel secondary(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY_FONT);
        label.setForeground(MUTED);
        return label;
    }

    static void button(AbstractButton button, String tooltip, boolean primary) {
        button.setUI(new PixelButtonUi());
        button.putClientProperty("pixel.primary", primary);
        button.setFont(BODY_FONT.deriveFont(primary ? Font.BOLD : Font.PLAIN));
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        button.setForeground(primary ? BACKGROUND : TEXT);
        button.setBackground(primary ? ACCENT : SURFACE);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setRolloverEnabled(true);
        button.setIconTextGap(8);
        button.setToolTipText(tooltip);
        button.getAccessibleContext().setAccessibleName(button.getText().isBlank() ? tooltip : button.getText());
        button.getAccessibleContext().setAccessibleDescription(tooltip);
        // Keep the look-and-feel focus indicator for keyboard navigation.
    }

    /** Apply to both playlist and song tables; column indices are model indices. */
    static JScrollPane table(JTable table, int... rightAlignedColumns) {
        table.setFont(BODY_FONT);
        table.setRowHeight(ROW_HEIGHT);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setBackground(SURFACE);
        table.setForeground(TEXT);
        table.setSelectionBackground(SELECTION);
        table.setSelectionForeground(TEXT);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable owner, Object value, boolean selected,
                                                           boolean focused, int row, int column) {
                super.getTableCellRendererComponent(owner, value, selected, focused, row, column);
                int modelColumn = owner.convertColumnIndexToModel(column);
                setHorizontalAlignment(SwingConstants.LEFT);
                for (int rightAlignedColumn : rightAlignedColumns) {
                    if (modelColumn == rightAlignedColumn) {
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                }
                setFont(BODY_FONT.deriveFont(modelColumn == 0 ? Font.BOLD : Font.PLAIN));
                setIcon(modelColumn == 0 ? new PixelIcon(PixelIcon.Kind.NOTE, 16) : null);
                setIconTextGap(10);
                setBackground(selected ? SELECTION : row % 2 == 0 ? SURFACE : ALTERNATE_ROW);
                setForeground(TEXT);
                setBorder(BorderFactory.createCompoundBorder(focused
                                ? BorderFactory.createLineBorder(ACCENT)
                                : BorderFactory.createEmptyBorder(1, 1, 1, 1),
                        BorderFactory.createEmptyBorder(0, 11, 0, 11)));
                return this;
            }
        });
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable owner, Object value, boolean selected,
                                                           boolean focused, int row, int column) {
                super.getTableCellRendererComponent(owner, value, selected, focused, row, column);
                setFont(BODY_FONT.deriveFont(Font.BOLD));
                setForeground(MUTED);
                setBackground(BACKGROUND);
                setHorizontalAlignment(SwingConstants.LEFT);
                for (int rightAlignedColumn : rightAlignedColumns) {
                    if (owner.convertColumnIndexToModel(column) == rightAlignedColumn) {
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                }
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                        BorderFactory.createEmptyBorder(0, 12, 0, 12)));
                return this;
            }
        });
        table.getTableHeader().setFont(BODY_FONT.deriveFont(Font.BOLD));
        table.getTableHeader().setForeground(MUTED);
        table.getTableHeader().setBackground(BACKGROUND);
        table.getTableHeader().setPreferredSize(new Dimension(0, ROW_HEIGHT));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(SURFACE);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getVerticalScrollBar().setUI(new PixelScrollBarUi());
        scrollPane.getHorizontalScrollBar().setUI(new PixelScrollBarUi());
        scrollPane.getVerticalScrollBar().setUnitIncrement(ROW_HEIGHT);
        return scrollPane;
    }
}

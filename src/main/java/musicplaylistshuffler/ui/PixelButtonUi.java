package musicplaylistshuffler.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.*;

/** Flat stepped buttons with explicit hover, pressed, selected and keyboard-focus states. */
public final class PixelButtonUi extends BasicButtonUI {
    /** UIManager factory for standard Swing dialogs as well as application buttons. */
    public static ComponentUI createUI(JComponent component) {
        return new PixelButtonUi();
    }

    @Override
    protected void installDefaults(AbstractButton button) {
        super.installDefaults(button);
        button.setFont(UiStyle.BODY_FONT);
        button.setForeground(UiStyle.TEXT);
        button.setBackground(UiStyle.SURFACE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        button.setOpaque(false);
        button.setRolloverEnabled(true);
    }

    private boolean highlighted(AbstractButton button) {
        return button.isSelected() || Boolean.TRUE.equals(button.getClientProperty("pixel.primary"));
    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        AbstractButton button = (AbstractButton) component;
        Graphics g = graphics.create();
        int width = component.getWidth();
        int height = component.getHeight();
        Color fill = highlighted(button) ? UiStyle.ACCENT : UiStyle.SURFACE;
        if (!button.isEnabled()) {
            fill = UiStyle.SURFACE;
        } else if (button.getModel().isPressed()) {
            fill = UiStyle.MUTED;
        } else if (button.getModel().isRollover() && !highlighted(button)) {
            fill = UiStyle.SELECTION;
        }
        g.setColor(fill);
        g.fillRect(2, 0, width - 4, height);
        g.fillRect(0, 2, width, height - 4);
        g.setColor(button.isEnabled() && highlighted(button) ? UiStyle.ACCENT : UiStyle.BORDER);
        g.drawLine(2, 0, width - 3, 0);
        g.drawLine(2, height - 1, width - 3, height - 1);
        g.drawLine(0, 2, 0, height - 3);
        g.drawLine(width - 1, 2, width - 1, height - 3);
        g.dispose();
        super.paint(graphics, component);
    }

    @Override
    protected void paintText(Graphics graphics, AbstractButton button, Rectangle rectangle, String text) {
        graphics.setFont(button.getFont());
        graphics.setColor(!button.isEnabled() ? UiStyle.MUTED
                : highlighted(button) ? UiStyle.BACKGROUND : button.getForeground());
        BasicGraphicsUtils.drawStringUnderlineCharAt(graphics, text, button.getDisplayedMnemonicIndex(),
                rectangle.x, rectangle.y + graphics.getFontMetrics().getAscent());
    }

    @Override
    protected void paintFocus(Graphics graphics, AbstractButton button, Rectangle view,
                              Rectangle text, Rectangle icon) {
        graphics.setColor(highlighted(button) ? UiStyle.BACKGROUND : UiStyle.ACCENT);
        BasicGraphicsUtils.drawDashedRect(graphics, 4, 4, button.getWidth() - 8, button.getHeight() - 8);
    }
}

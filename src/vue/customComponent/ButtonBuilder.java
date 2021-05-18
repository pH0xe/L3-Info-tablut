package vue.customComponent;

import vue.utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class ButtonBuilder {
    private final JButton button;

    private boolean isPressed;
    private boolean isHovered;

    public ButtonBuilder() {
        button = new JButton();
        isPressed = false;
        isHovered = false;
        // button.setBorderPainted(false);
        button.setFocusPainted(false);
    }

    public ButtonBuilder setBackground(Color color) {
        button.setBackground(color);
        return this;
    }

    public ButtonBuilder setForeground(Color color) {
        button.setForeground(color);
        return this;
    }

    public ButtonBuilder setText(String text) {
        button.setText(text);
        return this;
    }

    public ButtonBuilder setName(String name) {
        button.setName(name);
        return this;
    }

    public JButton toJButton() {
        button.setFont(Constants.BOLD_FONT);
        button.setUI(new ButtonStyle());
        return button;
    }


    private static class ButtonStyle extends BasicButtonUI {
        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            AbstractButton btn = (AbstractButton) c;
            btn.setOpaque(false);
            btn.setBorder(new EmptyBorder(5,15,5,15));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton btn = (AbstractButton) c;
            int decalageY = getDecalage(btn);

            int w = c.getSize().width;
            int h = c.getSize().height;

            int roundSize = 20;

            Graphics2D drawable = (Graphics2D) g;
            drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(c.getBackground().darker());
            g.fillRoundRect(0, decalageY, w, h - decalageY, roundSize, roundSize);

            Color couleurPrincipal = btn.getModel().isPressed() ? c.getBackground().darker() : c.getBackground();
            g.setColor(couleurPrincipal);
            g.fillRoundRect(0, decalageY, w, h + decalageY - 5, roundSize, roundSize);

            super.paint(g, c);
        }

        private int getDecalage(AbstractButton btn) {
            if (btn.getModel().isRollover()) return 2;
            if (btn.getModel().isPressed()) return 2;
            return 0;
        }
    }
}

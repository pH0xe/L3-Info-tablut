package vue.customComponent;

import vue.utils.Constants;
import vue.utils.Images;

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

    public ButtonBuilder setIcon(Image img) {
        button.setIcon(new ImageIcon(img));
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

            g.setColor(c.getBackground());
            g.fillRoundRect(0, decalageY, w, h + decalageY - 5, roundSize, roundSize);

            ImageIcon ic = (ImageIcon)btn.getIcon();
            if (ic != null) {
                Image img = ic.getImage();
                img = img.getScaledInstance((int) (c.getHeight()*0.4), (int) (c.getHeight()*0.4), Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            }
            super.paint(g, c);
        }

        private int getDecalage(AbstractButton btn) {
            if (btn.getModel().isRollover()) return 2;
            return 0;
        }

        @Override
        protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
            iconRect.setLocation(10, (int) iconRect.getY());
            super.paintIcon(g, c, iconRect);
        }
    }
}

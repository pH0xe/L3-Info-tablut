package vue.customComponent;

import vue.utils.Constants;
import vue.utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ButtonBuilder {
    private final JButton button;

    public ButtonBuilder() {
        button = new JButton();
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

    public JButton toFlatJButton() {
        button.setFont(Constants.BOLD_FONT);
        button.setUI(new ButtonFlatStyle());
        return button;
    }

    public JButton toIconJButton() {
        button.setFont(Constants.BOLD_FONT);
        button.setUI(new ButtonIconStyle());
        return button;
    }


    private static class ButtonStyle extends MetalButtonUI {
        private Image img;

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

            if (btn.isEnabled()) {
                g.setColor(c.getBackground().darker());
                g.fillRoundRect(0, decalageY, w, h - decalageY, roundSize, roundSize);
            }

            Color bgColor = btn.isEnabled() ? c.getBackground() : Color.LIGHT_GRAY;
            g.setColor(bgColor);
            g.fillRoundRect(0, decalageY, w, h + decalageY - 5, roundSize, roundSize);

            ImageIcon ic = (ImageIcon)btn.getIcon();
            if (ic != null) {
                img = ic.getImage();
                btn.setIcon(null);
            }
            if (img != null) {
                int iconSize = (int) (c.getHeight() * 0.4);
                int y = (c.getHeight() / 2) - (iconSize/2);
                g.drawImage(img, 10, y, iconSize, iconSize, null);
            }
            super.paint(g, c);
        }

        private int getDecalage(AbstractButton btn) {
            if (!btn.isEnabled()) return 2;
            if (btn.getModel().isRollover()) return 2;
            return 0;
        }

        @Override
        protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
            iconRect.setLocation(10, (int) iconRect.getY());
            super.paintIcon(g, c, iconRect);
        }

        @Override
        protected Color getDisabledTextColor() {
            return Color.BLACK;
        }
    }

    private static class ButtonFlatStyle extends BasicButtonUI {
        private Image img;

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

            Graphics2D drawable = (Graphics2D) g;
            drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(c.getBackground().darker());
            g.fillRect(0, decalageY, w, h - decalageY);

            g.setColor(c.getBackground());
            g.fillRect(0, decalageY, w, h + decalageY - 5);

            ImageIcon ic = (ImageIcon)btn.getIcon();
            if (ic != null) {
                img = ic.getImage();
                btn.setIcon(null);
            }
            if (img != null) {
                int iconSize = (int) (c.getHeight() * 0.4);
                int y = (c.getHeight() / 2) - (iconSize/2);
                g.drawImage(img, 10, y, iconSize, iconSize, null);
            }
            super.paint(g, c);
        }

        private int getDecalage(AbstractButton btn) {
            if (btn.getModel().isRollover()) return 2;
            return 0;
        }

        @Override
        protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {

        }
    }

    private static class ButtonIconStyle extends BasicButtonUI {
        private Image img;

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

            Graphics2D drawable = (Graphics2D) g;
            drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(c.getBackground().darker());
            g.fillRect(0, decalageY, w, h - decalageY);

            g.setColor(c.getBackground());
            g.fillRect(0, decalageY, w, h + decalageY - 5);

            ImageIcon ic = (ImageIcon)btn.getIcon();
            if (ic != null) {
                img = ic.getImage();
                btn.setIcon(null);
            }
            if (img != null) {
                int iconSize = (int) (c.getHeight() * 0.4);
                int x = (c.getWidth() / 2) - (iconSize/2);
                int y = (c.getHeight() / 2) - (iconSize/2);
                g.drawImage(img, x, y, iconSize, iconSize, null);
            }
            super.paint(g, c);
        }

        private int getDecalage(AbstractButton btn) {
            if (btn.getModel().isRollover()) return 2;
            return 0;
        }

        @Override
        protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {

        }
    }
}

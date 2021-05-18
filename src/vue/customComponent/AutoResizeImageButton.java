package vue.customComponent;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AutoResizeImageButton extends JButton implements MouseListener {
    protected Image background;
    protected Image hoverBackground;
    protected Image displayImage;

    public AutoResizeImageButton(final Image background, final Image hoverBackground) {
        this.background = background;
        this.hoverBackground = hoverBackground;
        this.displayImage = background;
        Border emptyBorder = BorderFactory.createEmptyBorder();
        setBorder(emptyBorder);
        setOpaque(false);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        // Only draw the background image
        g.drawImage(displayImage, 0, 0, getWidth(), getHeight(), null);
    }


    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        displayImage = hoverBackground;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        displayImage = background;
    }
}

package vue.buttons;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AutoResizePreserveRatioImageButton extends JButton implements MouseListener {
    private final Image image, imageHover;
    private Image displayImage;
    private final boolean isHorizontal;
    private final double aspectRatio;

    public AutoResizePreserveRatioImageButton(final Image image, final Image imageHover) {
        this.image = image;
        this.imageHover = imageHover;
        aspectRatio = (double) image.getWidth(null) / (double) image.getHeight(null);
        isHorizontal = aspectRatio > 1.0;
        this.displayImage = image;

        Border emptyBorder = BorderFactory.createEmptyBorder();
        setBorder(emptyBorder);
        addMouseListener(this);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final double screenRatio = (double) getWidth() / (double) getHeight();
        final boolean isScreenHorizontal = screenRatio > 1.0;

        int x = 0, y = 0;
        double width, height;

        // If image need to fill the width
        // Both image & container are horizontal but the image is "thicker" than the container
        // Both are vertical but the container is thicker than the image
        if ((isScreenHorizontal && isHorizontal && (screenRatio < aspectRatio))
                || (!isScreenHorizontal && !isHorizontal && screenRatio < aspectRatio)) {

            // Compute width & height with respect to aspect ratio
            width = getWidth();
            height = width / aspectRatio;

            // Center Y
            y = getCenteredY(height);

        } else {
            height = getHeight();
            width = height * aspectRatio;

            x = getCenteredX(width);
        }

        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(displayImage, x, y, (int) width, (int) height, null);
    }

    private int getCenteredX(final double imageWidth) {
        return (getWidth() - (int) imageWidth) / 2;
    }

    private int getCenteredY(final double imageHeight) {
        return (getHeight() - (int) imageHeight) / 2;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        displayImage = imageHover;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        displayImage = image;
    }
}

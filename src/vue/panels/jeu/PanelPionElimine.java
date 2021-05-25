package vue.panels.jeu;

import vue.utils.Constants;
import vue.utils.Images;

import javax.swing.*;
import java.awt.*;

public class PanelPionElimine extends JPanel {

    private int pionN, pionB;

    public PanelPionElimine() {
        pionN = 0;
        pionB = 0;
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(Constants.BOLD_FONT);

        int size = getWidth() / 7;
        int fontOffset = (int) (size*0.8);
        int totalSize = (int) (size + size*1.5);

        int x = 10;
        int y = (getHeight() - totalSize) /2;


        g2.setFont(Constants.BOLD_FONT.deriveFont(25f));
        g2.drawImage(Images.PION_BLANC, x, y, size, size, null);
        g2.drawString("x" + pionB, x + size + 10, y + fontOffset);

        y += size * 1.5;
        g2.drawImage(Images.PION_NOIR, x, y, size, size, null);
        g2.drawString("x" + pionN, x + size + 10, y + fontOffset);
    }

    public void setPionB(int pionB) {
        this.pionB = pionB;
        repaint();
    }

    public void incrementPionB(){
        pionB += 1;
        repaint();
    }

    public void incrementPionN(){
        pionN += 1;
        repaint();
    }

    public void setPionN(int pionN) {
        this.pionN = pionN;
        repaint();
    }
}

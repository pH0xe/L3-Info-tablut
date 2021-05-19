package vue.panels;

import controleur.CollecteurEvenements;
import modele.*;
import modele.Point;
import vue.utils.Constants;
import vue.utils.Images;

import javax.swing.*;
import java.awt.*;

public class PanelPlateau extends JPanel {
    private final Jeu jeu;
    private final CollecteurEvenements controleur;
    private int initX, initY, sizeCase;

    public PanelPlateau(CollecteurEvenements controleur, Jeu jeu) {
        this.controleur = controleur;
        this.jeu = jeu;
        setOpaque(true);
        setBackground(Color.RED);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int size = Math.min(getWidth(), getHeight());
        sizeCase = size / 9;

        initX = (getWidth() - size) / 2;
        initY = (getHeight() - size) / 2;

        g2.setColor(Constants.BOARD_STROKE);
        g2.setStroke(new BasicStroke(5));
        int y = initY;
        for (int i = 0; i < 9; i++) {
            int x = initX;
            for (int j = 0; j < 9; j++) {
                g2.drawImage(Images.CASE_PLATEAU, x, y, sizeCase, sizeCase, null);
                g2.drawRect(x, y, sizeCase, sizeCase);
                x += sizeCase;
            }
            y += sizeCase;
        }


        java.util.List<Pion> blancs = jeu.getPlateau().getBlancs();
        java.util.List<Pion> noirs = jeu.getPlateau().getNoirs();

        boolean tourBlanc = jeu.joueurCourant().getType() == TypeJoueur.BLANC;

        for (Pion pion : blancs) {
            if (pion.getType() == TypePion.ROI)
                drawPion(g2,pion.getPosition().getL(),pion.getPosition().getC(), Images.PION_ROI, tourBlanc);
            else
                drawPion(g2,pion.getPosition().getL(),pion.getPosition().getC(), Images.PION_BLANC, tourBlanc);
        }

        for (Pion pion : noirs) {
            drawPion(g2,pion.getPosition().getL(),pion.getPosition().getC(), Images.PION_NOIR, !tourBlanc);
        }
    }

    private void drawPion(Graphics2D g2, int l, int c, Image img, boolean estClickable) {
        int sizePion = sizeCase/2;
        int offset = (sizeCase - sizePion) / 2;
        int x = initX + (sizeCase * c) + offset;
        int y = initY + (sizeCase * l) + offset;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(img, x, y, sizePion, sizePion, null);

        if (estClickable) {
            g2.setStroke(new BasicStroke(4));
            int strokeSize = (int) ((BasicStroke) g2.getStroke()).getLineWidth();
            x -= strokeSize/3;
            y -= strokeSize/3;
            sizePion += strokeSize/2;
            g2.setColor(Constants.HALO_PION);
            g2.drawOval(x,y, sizePion, sizePion);
        }
    }


    public Point getCoord(int x, int y) {
        int taillePlateau = sizeCase * 9;
        if (x >= initX + taillePlateau || x <= initX || y >= initY + taillePlateau || y <= initY)
            return new Point(-1, -1);

        int c = (x - initX) / sizeCase;
        int l = (y - initY) / sizeCase;
        return new Point(l,c);
    }
}

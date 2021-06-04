package vue.panels.jeu;

import controleur.CollecteurEvenements;
import modele.*;
import modele.Joueur.Couleur;
import modele.util.Coup;
import modele.util.Point;
import modele.pion.Pion;
import modele.pion.TypePion;
import vue.adapters.mouseAdapters.PlateauAdapteur;
import vue.utils.Constants;
import vue.utils.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.List;

public class PanelPlateau extends JPanel {
    private Jeu jeu;
    private final CollecteurEvenements controleur;
    private int initX, initY, sizeCase;
    private boolean isHover = false;
    private Point hoverCoord;
    private boolean drawLastMove = false;

    public PanelPlateau(CollecteurEvenements controleur, Jeu jeu) {
        this.controleur = controleur;
        this.jeu = jeu;
        setOpaque(false);
        addMouseListener(new PlateauAdapteur(controleur, this));
        addMouseMotionListener(new PlateauAdapteur(controleur, this));
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
                Image img;
                if (i == 4 && j == 4)
                    img = Images.TRONE;
                else
                    img = Images.CASE_PLATEAU;
                g2.drawImage(img, x, y, sizeCase, sizeCase, null);
                g2.drawRect(x, y, sizeCase, sizeCase);
                x += sizeCase;
            }
            y += sizeCase;
        }

        if (jeu != null)
            miseEnPlaceJeu(g2);
    }

    private void miseEnPlaceJeu(Graphics2D g2) {
        java.util.List<Pion> blancs = jeu.getPlateau().getBlancs();
        java.util.List<Pion> noirs = jeu.getPlateau().getNoirs();

        boolean tourBlanc = jeu.joueurCourant().getCouleur() == Couleur.BLANC;

        for (Pion pion : blancs) {
            boolean clickable = tourBlanc && jeu.getPionClickable().contains(pion);
            if (pion.estPris()) continue;
            if (pion.getType() == TypePion.ROI)
                drawPion(g2,pion.getPosition().getL(),pion.getPosition().getC(), Images.PION_ROI, clickable);
            else
                drawPion(g2,pion.getPosition().getL(),pion.getPosition().getC(), Images.PION_BLANC, clickable);
        }

        for (Pion pion : noirs) {
            boolean clickable = !tourBlanc && jeu.getPionClickable().contains(pion);
            if (pion.estPris()) continue;
            drawPion(g2,pion.getPosition().getL(),pion.getPosition().getC(), Images.PION_NOIR, clickable);
        }

        if (drawLastMove && !jeu.getCoupsPrecedent().isEmpty())
            drawArrow(g2, jeu.getCoupsPrecedent().peek());
    }

    private void drawPion(Graphics2D g2, int l, int c, Image img, boolean estClickable) {
        int sizePion = sizeCase/2;
        int offset = (sizeCase - sizePion) / 2;
        int x = getX(c) + offset;
        int y = getY(l) + offset;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(img, x, y, sizePion, sizePion, null);

        g2.setStroke(new BasicStroke(4));
        int strokeSize = (int) ((BasicStroke) g2.getStroke()).getLineWidth();
        Pion pionSelect = jeu.getSelectionne();
        if (controleur.estTourIA()) return;
        if (estClickable && pionSelect == null) {
            x -= strokeSize/3;
            y -= strokeSize/3;
            sizePion += strokeSize/2;
            g2.setColor(Constants.HALO_PION);
            g2.drawOval(x,y, sizePion, sizePion);
        } else if (pionSelect != null && pionSelect.getPosition().equals(new Point(l, c))) {
            x -= strokeSize/3;
            y -= strokeSize/3;
            sizePion += strokeSize/2;
            g2.setColor(Constants.HALO_PION);
            g2.drawOval(x,y, sizePion, sizePion);

            drawSelectable(g2);
        }
    }

    private void drawSelectable(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        List<Point> clickable = jeu.getClickable();

        int sizePoint = sizeCase/4;
        int offset = (sizeCase - sizePoint) / 2;

        for (Point point : clickable) {
            int x = initX + (sizeCase * point.getC()) + offset;
            int y = initY + (sizeCase * point.getL()) + offset;

            g2.setColor(Constants.BOARD_PREVIEW);
            g2.fillOval(x,y, sizePoint, sizePoint);

            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.BLACK);
            g2.drawOval(x, y, sizePoint, sizePoint);
            if (isHover && point.equals(hoverCoord)) {
                g2.setColor(new Color(0,255,0,75));
                g2.fillRect(x - offset, y-offset, sizeCase, sizeCase);
                g2.setColor(Constants.BOARD_PREVIEW);
            }
        }


    }

    private void drawArrow(Graphics2D g, Coup coup) {
        Point debut = coup.getOrigine();
        Point fin = coup.getDestination();
        g.setColor(new Color(255, 0, 0, 128));
        int direction = getX(debut) - getX(fin);
        if (direction == 0){
            if (getY(fin) > getY(debut))
                drawUpToDownArrow(g, debut, fin);
            else
                drawDownToUpArrow(g, debut, fin);
        } else {
            if (getX(fin) > getX(debut))
                drawLeftToRightArrow(g, debut, fin);
            else
                drawRightToLeftArrow(g, debut, fin);
        }
    }

    private void drawLeftToRightArrow(Graphics2D g, Point coordDebut, Point coordFin) {
        int endX = getX(coordFin) + sizeCase/2;
        int endY = getY(coordFin) + sizeCase/2;
        int startX = getX(coordDebut) + sizeCase/2;
        int startY = getY(coordDebut) + sizeCase/2;

        GeneralPath path = new GeneralPath();
        path.moveTo(endX, endY);
        path.lineTo(endX - sizeCase/2, endY + sizeCase/3);
        path.lineTo(endX - sizeCase/2, endY - sizeCase/3);
        path.closePath();
        g.fill(path);

        int rectX = startX;
        int rectY = startY - sizeCase/8;
        int rectH = sizeCase/4;
        int rectW = (endX - sizeCase/2 +1) - rectX;
        g.fillRect(rectX, rectY, rectW, rectH);
    }

    private void drawRightToLeftArrow(Graphics2D g, Point coordDebut, Point coordFin) {
        int endX = getX(coordFin) + sizeCase/2;
        int endY = getY(coordFin) + sizeCase/2;
        int startX = getX(coordDebut) + sizeCase/2;
        int startY = getY(coordDebut) + sizeCase/2;

        GeneralPath path = new GeneralPath();
        path.moveTo(endX, endY);
        path.lineTo(endX + sizeCase/2, endY + sizeCase/3);
        path.lineTo(endX + sizeCase/2, endY - sizeCase/3);
        path.closePath();
        g.fill(path);

        int rectX = endX + sizeCase/2 - 1;
        int rectY = startY - sizeCase/8;
        int rectH = sizeCase/4;
        int rectW = startX - rectX;
        g.fillRect(rectX, rectY, rectW, rectH);
    }

    private void drawDownToUpArrow(Graphics2D g, Point coordDebut, Point coordFin) {
        int endX = getX(coordFin) + sizeCase/2;
        int endY = getY(coordFin) + sizeCase/2;
        int startX = getX(coordDebut) + sizeCase/2;
        int startY = getY(coordDebut) + sizeCase/2;

        GeneralPath path = new GeneralPath();
        path.moveTo(endX, endY);
        path.lineTo(endX + sizeCase/3, endY + sizeCase/2);
        path.lineTo(endX - sizeCase/3, endY + sizeCase/2);
        path.closePath();
        g.fill(path);

        int rectX = startX - sizeCase/8;
        int rectY = endY + sizeCase/2 - 1;
        int rectH = startY - rectY;
        int rectW = sizeCase/4;
        g.fillRect(rectX, rectY, rectW, rectH);
    }

    private void drawUpToDownArrow(Graphics2D g, Point coordDebut, Point coordFin) {
        int endX = getX(coordFin) + sizeCase/2;
        int endY = getY(coordFin) + sizeCase/2;
        int startX = getX(coordDebut) + sizeCase/2;
        int startY = getY(coordDebut) + sizeCase/2;

        GeneralPath path = new GeneralPath();
        path.moveTo(endX, endY);
        path.lineTo(endX + sizeCase/3, endY - sizeCase/2);
        path.lineTo(endX - sizeCase/3, endY - sizeCase/2);
        path.closePath();
        g.fill(path);

        int rectX = startX - sizeCase/8;
        int rectY = startY;
        int rectH = (endY - sizeCase/2 + 1) - rectY;
        int rectW = sizeCase/4;
        g.fillRect(rectX, rectY, rectW, rectH);
    }

    public Point getCoord(int x, int y) {
        int taillePlateau = sizeCase * 9;
        if (x >= initX + taillePlateau || x <= initX || y >= initY + taillePlateau || y <= initY)
            return null;

        int c = (x - initX) / sizeCase;
        int l = (y - initY) / sizeCase;
        return new Point(l,c);
    }

    public int getX(Point p) {
        return getX(p.getC());
    }

    public int getX(int c) {
        return initX + (sizeCase * c);
    }

    public int getY(Point p) {
        return getY(p.getL());
    }

    public int getY(int l) {
        return initY + (sizeCase * l);
    }

    public void addJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public void setHover(boolean b) {
        isHover = b;
    }

    public void setHoverCoord(Point coord) {
        hoverCoord = coord;
    }

    public void toggleLastMove(boolean show) {
        drawLastMove = show;
    }
}

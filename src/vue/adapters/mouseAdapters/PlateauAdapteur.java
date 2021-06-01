package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;
import modele.util.Point;
import vue.panels.jeu.PanelPlateau;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlateauAdapteur extends MouseAdapter {
    private final CollecteurEvenements controleur;
    private final PanelPlateau panel;

    public PlateauAdapteur(CollecteurEvenements controleur, PanelPlateau panel) {
        this.controleur = controleur;
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point point = panel.getCoord(e.getX(), e.getY());
        if (point != null)
            controleur.cliquePlateau(point);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        panel.setHover(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        panel.setHover(true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        panel.setHoverCoord(panel.getCoord(e.getX(), e.getY()));
        panel.repaint();
    }
}

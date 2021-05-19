package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;
import modele.Point;
import vue.panels.PanelPlateau;
import vue.utils.Labels;

import javax.swing.*;
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
}

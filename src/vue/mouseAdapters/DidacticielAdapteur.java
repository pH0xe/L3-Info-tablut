package vue.mouseAdapters;

import controleur.CollecteurEvenements;
import vue.panels.Didacticiel.PanelPlateauTuto;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DidacticielAdapteur extends MouseAdapter{
    private final CollecteurEvenements controleur;
    private PanelPlateauTuto panel;

    public DidacticielAdapteur(CollecteurEvenements cotroleur, PanelPlateauTuto pt){
        this.controleur = cotroleur;
        panel = pt;
    }

    @Override
    public void mousePressed(MouseEvent e){
        controleur.clicSourisTuto(panel.getCoord(e.getX(), e.getY()).getL(), panel.getCoord(e.getX(), e.getY()).getC());
    }
}

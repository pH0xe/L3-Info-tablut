package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnnulerTutoAdapteur extends MouseAdapter{
    private final CollecteurEvenements controleur;

    public AnnulerTutoAdapteur(CollecteurEvenements cotroleur){
        this.controleur = cotroleur;
    }

    @Override
    public void mousePressed(MouseEvent e){ controleur.clicAnnulerTuto(); }
}

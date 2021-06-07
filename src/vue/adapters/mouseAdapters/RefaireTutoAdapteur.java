package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RefaireTutoAdapteur extends MouseAdapter{
    private final CollecteurEvenements controleur;

    public RefaireTutoAdapteur(CollecteurEvenements cotroleur){
        this.controleur = cotroleur;
    }

    @Override
    public void mousePressed(MouseEvent e){
        controleur.clicRefaireTuto();
    }
}

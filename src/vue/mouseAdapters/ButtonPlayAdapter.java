package vue.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonPlayAdapter extends MouseAdapter {
    private final CollecteurEvenements controleur;

    public ButtonPlayAdapter(CollecteurEvenements controleur) {
        this.controleur = controleur;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Configuration.instance().logger().severe("Non implémenté");
    }
}

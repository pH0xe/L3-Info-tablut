package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MeilleursAdapteur extends MouseAdapter {
    private final CollecteurEvenements controleur;

    public MeilleursAdapteur(CollecteurEvenements controleur) {
        this.controleur = controleur;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton source = (JButton) e.getSource();
        switch (source.getText()) {
            case (Labels.BTN_RETOUR):
                Configuration.instance().logger().info("[Retour]");
                controleur.fermerMeilleursJoueurs();
                break;
            default:
                Configuration.instance().logger().severe("Commande inconnue : " + source.getName());
                break;

        }

    }
}

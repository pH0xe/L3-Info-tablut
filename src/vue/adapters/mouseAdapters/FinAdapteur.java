package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FinAdapteur extends MouseAdapter {
    private final CollecteurEvenements controleur;

    public FinAdapteur(CollecteurEvenements controleur) {
        this.controleur = controleur;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton source = (JButton) e.getSource();
        switch (source.getText()) {
            case (Labels.BTN_REJOUER):
                Configuration.instance().logger().info("[Rejouer]");
                controleur.rejouer();
                break;
            case (Labels.JEU_ACCUEIL):
                Configuration.instance().logger().warning("[ACCUEIL]");
                controleur.retourAccueil();
                break;
            default:
                Configuration.instance().logger().severe("Commande inconnue : " + source.getName());
                break;

        }

    }
}

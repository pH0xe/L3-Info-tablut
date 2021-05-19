package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;
import modele.Jeu;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JeuInfoAdapteur extends MouseAdapter {
    private final CollecteurEvenements controleur;

    public JeuInfoAdapteur(CollecteurEvenements controleur) {
        this.controleur = controleur;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton source = (JButton) e.getSource();

        switch (source.getText()) {
            case Labels.ACCUEIL_OPTIO :
                Configuration.instance().logger().warning("[Option] Non implémenté");
                break;
            case Labels.JEU_REFAIRE:
                Configuration.instance().logger().warning("[Refaire] Non implémenté");
                break;
            case Labels.JEU_ANNULER:
                Configuration.instance().logger().warning("[Annuler] Non implémenté");
                break;
            default:
                Configuration.instance().logger().severe("[" + e.getSource() + "] Commande inconnue");
                break;
        }
    }
}

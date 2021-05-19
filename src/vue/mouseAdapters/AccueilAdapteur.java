package vue.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AccueilAdapteur extends MouseAdapter {
    private final CollecteurEvenements controleur;

    public AccueilAdapteur(CollecteurEvenements controleur) {
        this.controleur = controleur;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton source = (JButton) e.getSource();
        switch (source.getText()) {
            case (Labels.ACCUEIL_JOUER):
                Configuration.instance().logger().warning("[Jouer] Non implémenté");
                break;
            case (Labels.ACCUEIL_DIDAC):
                Configuration.instance().logger().warning("[Didacticiel] Non implémenté");
                break;
            case (Labels.ACCUEIL_SAUVE):
                Configuration.instance().logger().warning("[Sauvegarde] Non implémenté");
                break;
            case (Labels.ACCUEIL_MEJOU):
                Configuration.instance().logger().warning("[Meilleurs joueur] Non implémenté");
                break;
            case (Labels.ACCUEIL_OPTIO):
                Configuration.instance().logger().warning("[Options] Non implémenté");
                break;
            case (Labels.ACCUEIL_QUITT):
                Configuration.instance().logger().warning("[Quitter] Non implémenté");
                break;
            default:
                Configuration.instance().logger().severe("Commande inconnue : " + source.getName());
                break;

        }

    }
}

package vue.adapters.mouseAdapters;

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
                Configuration.instance().logger().info("[Jouer]");
                controleur.demarrerJeu();
                break;
            case (Labels.ACCUEIL_DIDAC):
                Configuration.instance().logger().info("[Didacticiel]");
                controleur.ouvrirDidacticiel();
                break;
            case (Labels.ACCUEIL_SAUVE):
                Configuration.instance().logger().info("[Sauvegarde]");
                controleur.ouvrirSauvegarde();
                break;
            case (Labels.ACCUEIL_MEJOU):
                Configuration.instance().logger().info("[Meilleurs joueur]");
                controleur.ouvrirMeilleursJoueurs();
                break;
            case (Labels.ACCUEIL_OPTIO):
                Configuration.instance().logger().info("[Options]");
                controleur.ouvrirOption();
                break;
            case (Labels.ACCUEIL_QUITT):
                Configuration.instance().logger().info("[quitter]");
                System.exit(0);
                break;
            default:
                Configuration.instance().logger().severe("Commande inconnue : " + source.getName());
                break;

        }

    }
}

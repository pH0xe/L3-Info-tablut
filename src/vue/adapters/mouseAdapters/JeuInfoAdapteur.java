package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;
import modele.Jeu;
import vue.panels.jeu.PanelInfoJeu;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JeuInfoAdapteur extends MouseAdapter {
    private final CollecteurEvenements controleur;
    private final PanelInfoJeu panel;

    public JeuInfoAdapteur(CollecteurEvenements controleur, PanelInfoJeu panel) {
        this.controleur = controleur;
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton source = (JButton) e.getSource();

        switch (source.getText()) {
            case Labels.ACCUEIL_OPTIO :
                Configuration.instance().logger().warning("[Option]");
                controleur.ouvrirOptionJeu();
                break;
            case Labels.JEU_REFAIRE:
                if (panel.refaireActif()) {
                    Configuration.instance().logger().warning("[Refaire]");
                    controleur.refaireCoup();
                }
                break;
            case Labels.JEU_ANNULER:
                if (panel.annulerActif()) {
                    Configuration.instance().logger().info("[Annuler]");
                    controleur.annulerCoup();
                }
                break;
            default:
                Configuration.instance().logger().severe("[" + e.getSource() + "] Commande inconnue");
                break;
        }
    }
}

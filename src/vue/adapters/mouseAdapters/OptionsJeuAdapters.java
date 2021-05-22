package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;
import vue.panels.jeu.DialogOptionJeu;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OptionsJeuAdapters extends MouseAdapter {
    private CollecteurEvenements controleur;
    private DialogOptionJeu panel;

    public OptionsJeuAdapters(CollecteurEvenements controleur, DialogOptionJeu panel) {
        this.controleur = controleur;
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton source = (JButton) e.getSource();
        switch (source.getText()) {
            case Labels.JEU_ABANDON:
                Configuration.instance().logger().info(("[" + Labels.JEU_ABANDON + "]"));
                controleur.abandonnerPartie();
                break;
            case Labels.JEU_ACCUEIL:
                Configuration.instance().logger().info(("[" + Labels.JEU_ACCUEIL + "]"));
                controleur.retourAccueil();
                break;
            case Labels.BTN_FERMER:
                Configuration.instance().logger().info(("[" + Labels.BTN_FERMER + "]"));
                controleur.fermerOptionJeu(panel.getIABlanc(), panel.getIANoir());
                break;
            default:
                Configuration.instance().logger().severe(("[" + source.getText() + "] non implémenté"));
        }
    }
}

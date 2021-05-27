package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;
import vue.panels.PanelOption;
import vue.utils.Names;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OptionsAdapteur extends MouseAdapter {
    private final CollecteurEvenements controleur;
    private PanelOption panel;

    public OptionsAdapteur(CollecteurEvenements controleur, PanelOption panel) {
        this.controleur = controleur;
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton source = (JButton) e.getSource();
        switch (source.getName()) {
            case (Names.OPTIONS_MUSIC):
                Configuration.instance().logger().warning("[Musique] Non implémenté");
                break;
            case (Names.OPTIONS_SOUND):
                Configuration.instance().logger().warning("[Son] Non implémenté");
                break;
            case (Names.BTN_RETOUR):
                Configuration.instance().logger().info("[Retours]");
                controleur.fermerOption(panel.getNomJoueurBlanc(), panel.getNomJoueurNoir(), panel.getTypeJB(), panel.getTypeJN());
                break;
            default:
                Configuration.instance().logger().severe("Commande inconnue : " + source.getName());
                break;

        }

    }
}

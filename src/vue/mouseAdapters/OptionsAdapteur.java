package vue.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;
import vue.utils.Labels;
import vue.utils.Names;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OptionsAdapteur extends MouseAdapter {
    private final CollecteurEvenements controleur;

    public OptionsAdapteur(CollecteurEvenements controleur) {
        this.controleur = controleur;
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
                Configuration.instance().logger().warning("[Retours] Non implémenté");
                break;
            default:
                Configuration.instance().logger().severe("Commande inconnue : " + source.getName());
                break;

        }

    }
}

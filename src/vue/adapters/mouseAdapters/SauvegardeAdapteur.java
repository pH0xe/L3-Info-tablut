package vue.adapters.mouseAdapters;

import controleur.CollecteurEvenements;
import global.Configuration;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SauvegardeAdapteur extends MouseAdapter {
    private final CollecteurEvenements controleur;
    private final int type;
    private final String filename;

    public static final int BACK = 0;
    public static final int DELETE = 1;
    public static final int LOAD = 2;


    public SauvegardeAdapteur(CollecteurEvenements controleur, int type, String filename) {
        this.controleur = controleur;
        this.type = type;
        this.filename = filename;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton source = (JButton) e.getSource();
        switch (type) {
            case BACK:
                Configuration.instance().logger().info("Retour Accueil");
                controleur.quitterSauvegarde();
                break;
            case DELETE:
                Configuration.instance().logger().info("Suppression : " + filename);
                controleur.supprimerSauvegarde(filename);
                break;
            case LOAD:
                Configuration.instance().logger().info("Chargement : " + filename);
                controleur.chargerSauvegarde(filename);
                break;
            default:
                Configuration.instance().logger().severe("Erreur commande inconnue");
                break;
        }
    }
}

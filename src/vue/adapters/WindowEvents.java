package vue.adapters;

import controleur.CollecteurEvenements;
import controleur.Controleur;
import global.Configuration;
import vue.dialog.DialogSaveQuit;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowEvents extends WindowAdapter {
    private CollecteurEvenements controleur;
    private JPanel panel;

    public WindowEvents(Controleur controleur, JPanel panelJeu) {
        this.controleur = controleur;
        this.panel = panelJeu;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Configuration.instance().logger().info("Click on window close button");
        if(panel.isDisplayable())
            controleur.afficherDialogSauv(DialogSaveQuit.QUIT_AFTER);
        else
            controleur.fermerApp();
    }
}

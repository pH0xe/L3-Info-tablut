package vue.adapters;

import controleur.CollecteurEvenements;
import controleur.Controleur;
import global.Configuration;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowEvents extends WindowAdapter {
    private CollecteurEvenements controleur;

    public WindowEvents(Controleur controleur) {
        this.controleur = controleur;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Configuration.instance().logger().info("Click on window close button");
        System.exit(0);
    }
}

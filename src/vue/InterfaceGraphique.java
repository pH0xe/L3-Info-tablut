package vue;

import controleur.Controleur;
import vue.panels.PanelAccueil;
import vue.panels.PanelJeu;
import vue.panels.PanelOption;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable {
    private Controleur controleur;

    private JFrame frame;
    private JPanel panelAccueil, panelOption, panelJeu;

    public InterfaceGraphique(Controleur controleur) {
        this.controleur = controleur;
    }

    public static void demarrer(Controleur controleur) {
        SwingUtilities.invokeLater(new InterfaceGraphique(controleur));
    }

    @Override
    public void run() {
        frame =  new JFrame("Tablut");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setMinimumSize(new Dimension(600,600));
        frame.setLocationRelativeTo(null);

        panelAccueil = new PanelAccueil(controleur);
        panelOption = new PanelOption(controleur);
        panelJeu = new PanelJeu(controleur, null);
        frame.add(panelJeu);

        frame.setVisible(true);
    }
}

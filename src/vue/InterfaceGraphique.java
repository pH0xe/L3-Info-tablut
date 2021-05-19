package vue;

import controleur.Controleur;
import modele.Jeu;
import modele.Joueur;
import modele.TypeJoueur;
import vue.panels.DialogOptionJeu;
import vue.panels.PanelAccueil;
import vue.panels.PanelJeu;
import vue.panels.PanelOption;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable {
    private Controleur controleur;

    private JFrame frame;
    private JPanel panelAccueil, panelOption, panelJeu;
    private JDialog dialogOptionJeu;

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


        dialogOptionJeu = new JDialog();
        dialogOptionJeu.add(new DialogOptionJeu(controleur));
        dialogOptionJeu.setSize(400,400);
        dialogOptionJeu.setMinimumSize(new Dimension(300,500));
        dialogOptionJeu.setLocationRelativeTo(frame);

        panelAccueil = new PanelAccueil(controleur);
        panelOption = new PanelOption(controleur);
        panelJeu = new PanelJeu(controleur, new Jeu(new Joueur("Julien", TypeJoueur.BLANC), new Joueur("L'autre", TypeJoueur.NOIR)));
        frame.add(panelJeu);

        frame.setVisible(true);
        dialogOptionJeu.setVisible(false);
    }
}

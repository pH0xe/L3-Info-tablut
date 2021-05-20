package vue;

import controleur.Controleur;
import modele.Jeu;
import structure.Observer;
import vue.adapters.WindowEvents;
import vue.panels.DialogOptionJeu;
import vue.panels.PanelAccueil;
import vue.panels.PanelJeu;
import vue.panels.PanelOption;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable, Observer {
    private Controleur controleur;

    private JFrame frame;
    private PanelOption panelOption;
    private PanelJeu panelJeu;
    private PanelAccueil panelAccueil;
    private JDialog dialogOptionJeu;
    private Jeu jeu;

    public InterfaceGraphique(Controleur controleur) {
        this.controleur = controleur;
        this.controleur.fixerInterface(this);

        dialogOptionJeu = new JDialog();
        panelAccueil = new PanelAccueil(controleur);
        panelOption = new PanelOption(controleur);
        panelJeu = new PanelJeu(controleur, null);
    }

    public static void demarrer(Controleur controleur) {
        SwingUtilities.invokeLater(new InterfaceGraphique(controleur));
    }

    @Override
    public void run() {
        frame =  new JFrame("Tablut");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowEvents(controleur));
        frame.setSize(600, 600);
        frame.setMinimumSize(new Dimension(600,600));
        frame.setLocationRelativeTo(null);

        initDialogOption();

        frame.add(panelAccueil);
        frame.setVisible(true);
    }

    private void initDialogOption() {
        dialogOptionJeu.add(new DialogOptionJeu(controleur));
        dialogOptionJeu.setSize(400,400);
        dialogOptionJeu.setMinimumSize(new Dimension(300,500));
        dialogOptionJeu.setLocationRelativeTo(frame);
        dialogOptionJeu.setVisible(false);
    }

    @Override
    public void update() {
        if (panelJeu != null) {
            panelJeu.update();
        }
    }

    public void fixerJeu(Jeu jeu) {
        this.jeu = jeu;
        this.jeu.addObserver(this);
        panelJeu.addJeu(jeu);
        frame.remove(panelAccueil);
        frame.add(panelJeu);
        update();
        frame.repaint();
        frame.setVisible(true);
    }

    public void ouvrirOption() {
        frame.remove(panelAccueil);
        frame.add(panelOption);
        frame.repaint();
        frame.setVisible(true);
    }

    public void fermerOption() {
        frame.remove(panelOption);
        frame.add(panelAccueil);
        frame.repaint();
        frame.setVisible(true);
    }
}

package vue;

import controleur.AnimationChangerEtat;
import controleur.Controleur;
import modele.*;
import modele.Joueur.*;
import vue.panels.*;
import vue.panels.Didacticiel.PanelDidacticiel;
import vue.panels.jeu.PanelJeu;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable {
    private Controleur controleur;

    private Jeu jeu;
    private JeuTuto jeuTuto;
    private JFrame frame;
    private JPanel panelAccueil, panelOption, panelJeu;
    private PanelDidacticiel panelDidacticiel;
    private JDialog dialogOptionJeu;
    private int wd = 710, ht = 600;

    public InterfaceGraphique(Controleur controleur) {
        this.controleur = controleur;
        jeu = new Jeu(new Joueur("Julien", Couleur.BLANC), new Joueur("L'autre", Couleur.NOIR));
    }

    public static void demarrer(Controleur controleur) {
        SwingUtilities.invokeLater(new InterfaceGraphique(controleur));
    }

    @Override
    public void run() {
        frame =  new JFrame("Tablut");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(wd, ht);
        frame.setMinimumSize(new Dimension(wd,ht));
//        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        jeuTuto = new JeuTuto(new Jeu(new Joueur("Jouer1", Couleur.BLANC), new Joueur("Jouer2", Couleur.NOIR)), 0);
        controleur.fixerInterface(this);
        controleur.fixerJeuTuto(jeuTuto);

        dialogOptionJeu = new JDialog();
        dialogOptionJeu.add(new DialogOptionJeu(controleur));
        dialogOptionJeu.setSize(400,400);
        dialogOptionJeu.setMinimumSize(new Dimension(300,500));
        dialogOptionJeu.setLocationRelativeTo(frame);

        panelAccueil = new PanelAccueil(controleur);
        panelOption = new PanelOption(controleur);
        panelJeu = new PanelJeu(controleur, jeu);

        panelDidacticiel = new PanelDidacticiel(controleur, jeuTuto);
        frame.add(panelDidacticiel);
        Timer timer = new Timer(500, new AnimationChangerEtat(controleur));
        controleur.fixerTimer(timer);

        frame.setVisible(true);
        dialogOptionJeu.setVisible(false);
    }

    public void update(){
        if(panelDidacticiel != null)
            panelDidacticiel.update();
    }

    public void addJeuTuto(JeuTuto j){
        jeuTuto = j;
        panelDidacticiel.addJeu(jeuTuto);
    }
}


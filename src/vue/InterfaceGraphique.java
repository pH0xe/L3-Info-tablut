package vue;

import controleur.Controleur;
import modele.*;
import vue.panels.Didacticiel.PanelDidacticiel;
import vue.panels.jeu.PanelJeu;
import modele.Jeu;
import modele.Joueur.Joueur;
import structure.Observer;
import vue.adapters.WindowEvents;
import vue.dialog.DialogFinJeu;
import vue.dialog.DialogOptionJeu;
import vue.dialog.DialogSaveQuit;
import vue.panels.PanelAccueil;
import vue.panels.bestPlayers.PanelMeilleursJoueurs;
import vue.panels.PanelOption;
import vue.panels.saves.PanelSauvegarde;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable, Observer {
    private final Controleur controleur;

    private Jeu jeu;
    private JeuTuto jeuTuto;
    private JFrame frame;
    private final PanelOption panelOption;
    private final PanelJeu panelJeu;
    private final PanelAccueil panelAccueil;
    private final PanelSauvegarde panelSauvegarde;
    private final JDialog dialogFinJeu;
    private final JDialog dialogOptionJeu;
    private final DialogOptionJeu panelOptionJeu;
    private final DialogFinJeu panelDialogFinJeu;
    private final DialogSaveQuit dialogSaveQuit;
    private final PanelMeilleursJoueurs panelMeilleurs;
    private PanelDidacticiel panelDidacticiel;

    public InterfaceGraphique(Controleur controleur) {
        this.controleur = controleur;
        this.controleur.fixerInterface(this);

        dialogOptionJeu = new JDialog();
        panelOptionJeu = new DialogOptionJeu(controleur);
        dialogFinJeu = new JDialog();
        panelAccueil = new PanelAccueil(controleur);
        panelOption = new PanelOption(controleur);
        panelJeu = new PanelJeu(controleur, null);
        panelSauvegarde = new PanelSauvegarde(controleur);
        dialogSaveQuit = new DialogSaveQuit(controleur);
        panelDialogFinJeu = new DialogFinJeu(controleur);
        panelMeilleurs = new PanelMeilleursJoueurs(controleur);
        panelDidacticiel = new PanelDidacticiel(controleur, null);

        controleur.fixerInterface(this);
    }

    public static void demarrer(Controleur controleur) {
        SwingUtilities.invokeLater(new InterfaceGraphique(controleur));
    }

    @Override
    public void run() {
        frame =  new JFrame("Tablut");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowEvents(controleur, panelJeu));
        frame.setSize(600, 600);
        frame.setMinimumSize(new Dimension(600,600));
        frame.setLocationRelativeTo(null);
        initDialogOption();
        initDialogFin();
        frame.add(panelAccueil);
        frame.setVisible(true);
    }

    private void initDialogOption() {
        dialogOptionJeu.add(panelOptionJeu);
        dialogOptionJeu.setSize(400,400);
        dialogOptionJeu.setMinimumSize(new Dimension(300,500));
        dialogOptionJeu.setLocationRelativeTo(frame);
        dialogOptionJeu.setVisible(false);
        dialogOptionJeu.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    }

    private void initDialogFin() {
        dialogFinJeu.add(panelDialogFinJeu);
        dialogFinJeu.setSize(400,400);
        dialogFinJeu.setMinimumSize(new Dimension(300,500));
        dialogFinJeu.setLocationRelativeTo(frame);
        dialogFinJeu.setVisible(false);
    }

    @Override
    public void update() {
        if (panelJeu.isDisplayable()) {
            panelJeu.update();
        }
        if (panelSauvegarde.isDisplayable()) {
            panelSauvegarde.update();
        }
        if(panelDidacticiel.isDisplayable())
            panelDidacticiel.update();
    }

    public void fixerJeu(Jeu jeu) {
        jeu.addObserver(this);
        panelJeu.addJeu(jeu);
        fermerPanels();
        frame.add(panelJeu);
        update();
        reloadFrame();
    }
    public void toggleLastMove(boolean show) {
        if (panelJeu != null) {
            panelJeu.toggleLastMove(show);
            update();
        }
    }

    public void ouvrirOption() {
        fermerPanels();
        frame.add(panelOption);
        reloadFrame();
    }

    public void fermerOption() {
        fermerPanels();
        frame.add(panelAccueil);
        reloadFrame();
    }

    public void ouvrirDialogOption() {
        this.panelOptionJeu.update();
        this.dialogOptionJeu.setLocationRelativeTo(frame);
        this.dialogOptionJeu.setVisible(true);
    }

    public void fermerDialogOption() {
        this.dialogOptionJeu.setVisible(false);
    }

    public void retourAccueil() {
        fermerPanels();
        frame.add(panelAccueil);
        reloadFrame();
    }

    public void afficherDialogSauvegarde(int afterAction) {
        dialogSaveQuit.showMessage(afterAction, panelJeu);
    }

    public void ouvrirSauvegarde() {
        fermerPanels();
        frame.add(panelSauvegarde);
        panelSauvegarde.update();
        reloadFrame();
    }

    public void quitterSauvegarde() {
        frame.remove(panelSauvegarde);
        frame.add(panelAccueil);
        frame.repaint();
        frame.setVisible(true);
    }

    public void ouvrirDialogFin(Joueur gagnant) {
        panelDialogFinJeu.fixerGagnant(gagnant);
        this.dialogFinJeu.setLocationRelativeTo(frame);
        this.dialogFinJeu.setVisible(true);
    }

    public void fermerDialogFin() {
        this.dialogFinJeu.setVisible(false);
    }

    private void fermerPanels() {
        if (panelOption.isDisplayable())
            frame.remove(panelOption);
        if (panelJeu.isDisplayable())
            frame.remove(panelJeu);
        if (panelAccueil.isDisplayable())
            frame.remove(panelAccueil);
        if (panelSauvegarde.isDisplayable())
            frame.remove(panelSauvegarde);
        if (panelDidacticiel.isDisplayable())
            frame.remove(panelDidacticiel);
        if (panelMeilleurs.isDisplayable())
            frame.remove(panelMeilleurs);
    }

    private void reloadFrame() {
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public void ouvrirMeilleursJoueurs() {
        fermerPanels();
        panelMeilleurs.update();
        frame.add(panelMeilleurs);
        reloadFrame();
    }

    public void fermerMeilleursJoueurs() {
        fermerPanels();
        frame.add(panelAccueil);
        reloadFrame();
    }

    public void ouvrirDidacticiel(){
        fermerPanels();
        frame.add(panelDidacticiel);
        reloadFrame();
    }

    public void fermerDidacticiel(){
        fermerPanels();
        frame.add(panelAccueil);
        reloadFrame();
    }

    public void addJeuTuto(JeuTuto j){
        jeuTuto = j;
        panelDidacticiel.addJeu(jeuTuto);
    }
}


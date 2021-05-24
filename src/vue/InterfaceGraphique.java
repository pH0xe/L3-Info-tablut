package vue;

import controleur.Controleur;
import modele.Jeu;
import structure.Observer;
import vue.adapters.WindowEvents;
import vue.dialog.DialogOptionJeu;
import vue.dialog.DialogSaveQuit;
import vue.panels.PanelAccueil;
import vue.panels.jeu.PanelJeu;
import vue.panels.PanelOption;
import vue.panels.saves.PanelSauvegarde;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable, Observer {
    private Controleur controleur;

    private JFrame frame;
    private PanelOption panelOption;
    private PanelJeu panelJeu;
    private PanelAccueil panelAccueil;
    private PanelSauvegarde panelSauvegarde;
    private JDialog dialogOptionJeu;
    private Jeu jeu;
    private DialogSaveQuit dialogSaveQuit;

    public InterfaceGraphique(Controleur controleur) {
        this.controleur = controleur;
        this.controleur.fixerInterface(this);

        dialogOptionJeu = new JDialog();
        panelAccueil = new PanelAccueil(controleur);
        panelOption = new PanelOption(controleur);
        panelJeu = new PanelJeu(controleur, null);
        panelSauvegarde = new PanelSauvegarde(controleur);
        dialogSaveQuit = new DialogSaveQuit(controleur);
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

        frame.add(panelAccueil);
        frame.setVisible(true);
    }

    private void initDialogOption() {
        dialogOptionJeu.add(new DialogOptionJeu(controleur));
        dialogOptionJeu.setSize(400,400);
        dialogOptionJeu.setMinimumSize(new Dimension(300,500));
        dialogOptionJeu.setLocationRelativeTo(frame);
        dialogOptionJeu.setVisible(false);
        dialogOptionJeu.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    }

    @Override
    public void update() {
        if (panelJeu.isDisplayable()) {
            panelJeu.update();
        }
        if (panelSauvegarde.isDisplayable()) {
            panelSauvegarde.update();
        }
    }

    public void fixerJeu(Jeu jeu) {
        this.jeu = jeu;
        this.jeu.addObserver(this);
        panelJeu.addJeu(jeu);
        if (panelAccueil.isDisplayable())
            frame.remove(panelAccueil);
        if (panelSauvegarde.isDisplayable())
            frame.remove(panelSauvegarde);
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

    public void ouvrirDialogOption() {
        this.dialogOptionJeu.setLocationRelativeTo(frame);
        this.dialogOptionJeu.setVisible(true);
    }

    public void fermerDialogOption() {
        this.dialogOptionJeu.setVisible(false);
    }

    public void retourAccueil(String source) {
        if (source.equalsIgnoreCase("jeu")) {
            frame.remove(panelJeu);
            frame.add(panelAccueil);
            frame.repaint();
            frame.setVisible(true);
        }
    }

    public void afficherDialogSauvegarde(int afterAction) {
        dialogSaveQuit.showMessage(afterAction);
    }

    public void ouvrirSauvegarde() {
        frame.remove(panelAccueil);
        frame.add(panelSauvegarde);
        panelSauvegarde.update();
        frame.repaint();
        frame.setVisible(true);
    }

    public void quitterSauvegarde() {
        frame.remove(panelSauvegarde);
        frame.add(panelAccueil);
        frame.repaint();
        frame.setVisible(true);
    }
}

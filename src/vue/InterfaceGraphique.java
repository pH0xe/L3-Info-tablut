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
    private final Controleur controleur;

    private JFrame frame;
    private final PanelOption panelOption;
    private final PanelJeu panelJeu;
    private final PanelAccueil panelAccueil;
    private final PanelSauvegarde panelSauvegarde;
    private final JDialog dialogOptionJeu;
    private final DialogSaveQuit dialogSaveQuit;

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
        jeu.addObserver(this);
        panelJeu.addJeu(jeu);
        fermerPanels();
        frame.add(panelJeu);
        update();
        reloadFrame();
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
        dialogSaveQuit.showMessage(afterAction);
    }

    public void ouvrirSauvegarde() {
        fermerPanels();
        panelSauvegarde.update();
        reloadFrame();
    }

    public void quitterSauvegarde() {
        frame.remove(panelSauvegarde);
        frame.add(panelAccueil);
        frame.repaint();
        frame.setVisible(true);
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
    }

    private void reloadFrame() {
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
}

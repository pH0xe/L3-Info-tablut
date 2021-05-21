package vue.panels;

import controleur.CollecteurEvenements;
import controleur.Controleur;
import modele.Jeu;
import modele.JeuTuto;
import vue.customComponent.ButtonBuilder;
import vue.mouseAdapters.AccueilAdapteur;
import vue.mouseAdapters.DidacticielAdapteur;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.*;

public class PanelDidacticiel extends JPanel {

    private final CollecteurEvenements controleur;
    JeuTuto jeu;
    PanelPlateauTuto panelPlateau;
    PanelInstructionTuto panelInstruction;
    PanelInfoTuto panelInfoTuto;

    public PanelDidacticiel(CollecteurEvenements c, JeuTuto j){
        controleur = c;
        jeu = j;
        setLayout(new GridBagLayout());
        setBackground(Constants.FRAME_BACKGROUND);

        ConstraintBuilder gbc = new ConstraintBuilder(0,0)
                .setWeightx(1)
                .setWeighty(0.9)
                .fillBoth();

        gbc.setWeighty(0.95);
        panelPlateau = new PanelPlateauTuto(controleur, jeu);
        panelPlateau.addMouseListener(new DidacticielAdapteur(controleur, panelPlateau));
        add(panelPlateau, gbc.toConstraints());

        gbc.setGridy(1).setWeightx(0.9).setWeighty(0.05).fillBoth();
        panelInstruction = new PanelInstructionTuto(controleur, jeu);
        add(panelInstruction, gbc.toConstraints());

        gbc.setGridy(0).setGridx(1).setWeightx(0.1).setWeighty(1).setGridHeight(2);
        panelInfoTuto = new PanelInfoTuto(controleur, jeu);
        add(panelInfoTuto, gbc.toConstraints());

    }

    public void update(){
        panelPlateau.repaint();
        panelInstruction.update();
        panelInfoTuto.update();
        repaint();
    }

    public void addJeu(JeuTuto j){
        jeu = j;
        panelPlateau.addJeu(jeu);
        panelInstruction.addJeu(jeu);
        panelInfoTuto.addJeu(jeu);
    }

}

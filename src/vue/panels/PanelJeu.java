package vue.panels;

import controleur.CollecteurEvenements;
import modele.Jeu;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;

import javax.swing.*;
import java.awt.*;

public class PanelJeu extends JPanel {

    private final CollecteurEvenements controleur;
    private Jeu jeu;
    private PanelPlateau panelPlateau;
    private PanelInfoJeu panelInfoJeu;

    public PanelJeu(CollecteurEvenements c, Jeu jeu) {
        controleur = c;
        this.jeu = jeu;
        setLayout(new GridBagLayout());
        setBackground(Constants.FRAME_BACKGROUND);

        ConstraintBuilder gbc = new ConstraintBuilder(0,0)
                .setWeightx(0.9)
                .setWeighty(1)
                .fillBoth();

        panelPlateau = new PanelPlateau(controleur, jeu);
        add(panelPlateau, gbc.toConstraints());

        gbc.setGridx(1).setWeightx(0.1);
        panelInfoJeu = new PanelInfoJeu(controleur, jeu);
        add(panelInfoJeu, gbc.toConstraints());
    }

    public void update() {
        panelInfoJeu.update();
        panelPlateau.repaint();
        repaint();
    }

    public void addJeu(Jeu jeu) {
        this.jeu = jeu;
        panelPlateau.addJeu(jeu);
        panelInfoJeu.addJeu(jeu);
    }
}

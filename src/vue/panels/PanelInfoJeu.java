package vue.panels;

import controleur.CollecteurEvenements;
import modele.Jeu;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class PanelInfoJeu extends JPanel implements MouseListener {
    private final Jeu jeu;
    private final CollecteurEvenements controleur;
    private JLabel joueurCourant;
    private JPanel pionElimi;

    public PanelInfoJeu(CollecteurEvenements controleur, Jeu jeu) {
        this.controleur = controleur;
        this.jeu = jeu;
        setOpaque(true);
        setBackground(Color.RED.darker());

        setLayout(new GridLayout(2,1));

        joueurCourant = new JLabel("Au tours de : Blanc", SwingConstants.CENTER);
        joueurCourant.setFont(Constants.BOLD_FONT);
        joueurCourant.setForeground(Constants.JEU_LABEL_FG);
        joueurCourant.setOpaque(true);
        joueurCourant.setBackground(Constants.JEU_LABEL_BG);
        ConstraintBuilder cb = new ConstraintBuilder(0,0).setIpady(40).setWeighty(0.2).setWeightx(1).fillBoth();
        add(joueurCourant);

        pionElimi = new PanelPionElimine();
        cb.setWeighty(0.5).setGridy(1);
        add(pionElimi);

        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Random r = new Random();
        if (r.nextBoolean()) {
            ((PanelPionElimine)pionElimi).incrementPionB();
        } else {
            ((PanelPionElimine)pionElimi).incrementPionN();
        }


        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

package vue.panels;

import controleur.CollecteurEvenements;
import modele.Jeu;
import vue.adapters.mouseAdapters.JeuInfoAdapteur;
import vue.customComponent.ButtonBuilder;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Images;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class PanelInfoJeu extends JPanel {
    private Jeu jeu;
    private final CollecteurEvenements controleur;
    private JLabel joueurCourant;
    private PanelPionElimine pionElimi;
    private JButton btnOption, btnRefaire, btnAnnuler;

    public PanelInfoJeu(CollecteurEvenements controleur, Jeu jeu) {
        this.controleur = controleur;
        this.jeu = jeu;
        setOpaque(true);
        setBackground(Color.gray);

        setLayout(new GridBagLayout());

        joueurCourant = new JLabel("Au tours de : Blanc", SwingConstants.CENTER);
        joueurCourant.setFont(Constants.BOLD_FONT);
        joueurCourant.setForeground(Constants.JEU_LABEL_FG);
        joueurCourant.setOpaque(true);
        joueurCourant.setBackground(Constants.JEU_LABEL_BG);
        ConstraintBuilder cb = new ConstraintBuilder(0,0).setIpady(40).setWeighty(0.2).setWeightx(1).fillBoth();
        add(joueurCourant, cb.toConstraints());

        pionElimi = new PanelPionElimine();
        cb.setWeighty(0.5).incrGridy();
        add(pionElimi,cb.toConstraints());

        btnOption = new ButtonBuilder().setText(Labels.ACCUEIL_OPTIO).setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND)
                .setIcon(Images.OPTION).toJButton();
        btnAnnuler = new ButtonBuilder().setText(Labels.JEU_ANNULER).setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND)
                .setIcon(Images.ANNULER_COUP).toJButton();
        btnRefaire = new ButtonBuilder().setText(Labels.JEU_REFAIRE).setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND)
                .setIcon(Images.REFAIRE_COUP).toJButton();

        cb.setWeighty(0).setIpady(30).setInset(0,10,10,10);
        cb.incrGridy();
        add(btnAnnuler, cb.toConstraints());

        cb.incrGridy();
        add(btnRefaire, cb.toConstraints());

        cb.incrGridy();
        add(btnOption, cb.toConstraints());

        initButtonAdapters();

        if (jeu != null)
            update();
    }

    private void initButtonAdapters() {
        btnRefaire.addMouseListener(new JeuInfoAdapteur(controleur));
        btnAnnuler.addMouseListener(new JeuInfoAdapteur(controleur));
        btnOption.addMouseListener(new JeuInfoAdapteur(controleur));
    }

    public void update() {
        joueurCourant.setText("Au tours de : " + jeu.joueurCourant().getNom() + " [" + jeu.joueurCourant().getType().toString().toLowerCase() + "]");
        pionElimi.setPionB(jeu.getPlateau().getBlancsElimine());
        pionElimi.setPionN(jeu.getPlateau().getNoirsElimine());
        pionElimi.repaint();
    }

    public void addJeu(Jeu jeu) {
        this.jeu = jeu;
    }
}

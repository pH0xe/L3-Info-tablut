package vue.panels.Didacticiel;

import controleur.CollecteurEvenements;
import modele.JeuTuto;
import vue.adapters.mouseAdapters.AccueilTutoAdapteur;
import vue.adapters.mouseAdapters.AnnulerTutoAdapteur;
import vue.customComponent.ButtonBuilder;
import vue.adapters.mouseAdapters.RefaireTutoAdapteur;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Images;
import vue.utils.Labels;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class PanelInfoTuto extends JPanel {
    private JeuTuto jeu;
    private final CollecteurEvenements controleur;
    private JTextPane joueurCourant;
    private PanelPionElimineTuto pionElimi;
    private JButton btnAcceuil, btnRefaire, btnAnnuler;
    private SimpleAttributeSet style;

    public PanelInfoTuto(CollecteurEvenements controleur, JeuTuto jeu) {
        this.controleur = controleur;
        this.jeu = jeu;
        setOpaque(true);
        setBackground(Color.gray);

        setLayout(new GridBagLayout());

        joueurCourant = new JTextPane();
        joueurCourant.setBackground(Constants.JEU_LABEL_BG);
        joueurCourant.setBorder(BorderFactory.createEmptyBorder(50,0,0,0));
        style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, Font.SANS_SERIF);
        StyleConstants.setBackground(style, Constants.JEU_LABEL_BG);
        StyleConstants.setForeground(style, Constants.JEU_LABEL_FG);
        StyleConstants.setFontSize(style, 18);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);

        ConstraintBuilder cb = new ConstraintBuilder(0,0).setIpady(40).setWeighty(0.2).setWeightx(1).fillBoth();
        add(joueurCourant, cb.toConstraints());

        pionElimi = new PanelPionElimineTuto();
        cb.setWeighty(0.5).incrGridy();
        add(pionElimi,cb.toConstraints());

        btnAcceuil = new ButtonBuilder().setText(Labels.JEU_ACCUEIL).setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND)
                .setIcon(Images.ACCUEIL).toJButton();
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
        add(btnAcceuil, cb.toConstraints());

        initButtonAdapters();

        if (jeu != null)
            update();
    }

    private void initButtonAdapters() {
        btnRefaire.addMouseListener(new RefaireTutoAdapteur(controleur));
        btnAnnuler.addMouseListener(new AnnulerTutoAdapteur(controleur));
        btnAcceuil.addMouseListener(new AccueilTutoAdapteur(controleur));
    }

    public void update() {
        StyledDocument textJoueur = joueurCourant.getStyledDocument();
        try {
            textJoueur.remove(0, textJoueur.getLength());
            textJoueur.insertString(textJoueur.getLength(), "Au tour de :\n", style);
            textJoueur.insertString(textJoueur.getLength(), jeu.getJeu().joueurCourant().getNom() + "\n", style);
            textJoueur.insertString(textJoueur.getLength(), "[" + jeu.getJeu().joueurCourant().getCouleur().toString().toLowerCase() + "]", style);
            textJoueur.setParagraphAttributes(0, textJoueur.getLength(), style, false);
        } catch (Exception ignored){}

        pionElimi.setPionB(jeu.getJeu().getPlateau().getBlancsElimine());
        pionElimi.setPionN(jeu.getJeu().getPlateau().getNoirsElimine());
        pionElimi.repaint();

        btnAnnuler.setEnabled(!jeu.getCoupsPrecedent().isEmpty());
        btnRefaire.setEnabled(false);
    }

    public void addJeu(JeuTuto jeu) {
        this.jeu = jeu;
    }

    public boolean annulerActif() {
        return btnAnnuler.isEnabled();
    }

    public boolean refaireActif() {
        return btnRefaire.isEnabled();
    }
}

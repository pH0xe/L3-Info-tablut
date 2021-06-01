package vue.panels.jeu;

import controleur.CollecteurEvenements;
import modele.Jeu;
import vue.adapters.mouseAdapters.JeuInfoAdapteur;
import vue.customComponent.ButtonBuilder;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Images;
import vue.utils.Labels;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class PanelInfoJeu extends JPanel {
    private Jeu jeu;
    private final CollecteurEvenements controleur;
    private JTextPane joueurCourant;
    private PanelPionElimine pionElimi;
    private JButton btnOption, btnRefaire, btnAnnuler;
    private SimpleAttributeSet style;

    public PanelInfoJeu(CollecteurEvenements controleur, Jeu jeu) {
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

        pionElimi = new PanelPionElimine();
        cb.setWeighty(0.5).incrGridy();
        add(pionElimi,cb.toConstraints());

        btnOption = new ButtonBuilder().setText(Labels.JEU_MENU).setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND)
                .setIcon(Images.MENU).toJButton();
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
        btnRefaire.addMouseListener(new JeuInfoAdapteur(controleur, this));
        btnAnnuler.addMouseListener(new JeuInfoAdapteur(controleur, this));
        btnOption.addMouseListener(new JeuInfoAdapteur(controleur, this));
    }

    public void update() {
        StyledDocument textJoueur = joueurCourant.getStyledDocument();
        try {
            textJoueur.remove(0, textJoueur.getLength());
            textJoueur.insertString(textJoueur.getLength(), "Au tour de :\n", style);
            textJoueur.insertString(textJoueur.getLength(), jeu.joueurCourant().getNom() + "\n", style);
            textJoueur.insertString(textJoueur.getLength(), "[" + jeu.joueurCourant().getCouleur().toString().toLowerCase() + "]", style);
            textJoueur.setParagraphAttributes(0, textJoueur.getLength(), style, false);
        } catch (Exception ignored){}

        pionElimi.setPionB(jeu.getPlateau().getBlancsElimine());
        pionElimi.setPionN(jeu.getPlateau().getNoirsElimine());
        pionElimi.repaint();

        btnAnnuler.setEnabled(!jeu.getCoupsPrecedent().isEmpty());
        btnRefaire.setEnabled(!jeu.getCoupsSuivant().isEmpty());
    }

    public void addJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public boolean annulerActif() {
        return btnAnnuler.isEnabled();
    }

    public boolean refaireActif() {
        return btnRefaire.isEnabled();
    }

    @Override
    public Dimension getPreferredSize() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        int size = (frame.getWidth() / 5);
        return new Dimension(size, size);
    }

}

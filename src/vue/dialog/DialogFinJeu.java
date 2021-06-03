package vue.dialog;

import controleur.CollecteurEvenements;
import modele.Joueur.Joueur;
import vue.adapters.mouseAdapters.FinAdapteur;
import vue.adapters.mouseAdapters.OptionsJeuAdapters;
import vue.customComponent.ButtonBuilder;
import vue.customComponent.CustomComboBox;
import vue.customComponent.formPanel;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Labels;
import vue.utils.Names;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class DialogFinJeu extends JPanel {
    private CollecteurEvenements controleur;
    private Joueur gagnant;
    private JButton btnRejouer, btnAccueil;
    private JTextPane panelGagnant;

    public DialogFinJeu(CollecteurEvenements c) {
        controleur = c;
        setLayout(new GridBagLayout());
        setBackground(Constants.FRAME_BACKGROUND);
        initGrid(this, 4);

        ConstraintBuilder gbc = new ConstraintBuilder(1,0)
                .setWeightx(1)
                .setWeighty(0.1)
                .setGridWidth(2)
                .fillBoth();

        JLabel title = initTitle();
        add(title, gbc.toConstraints());

        gbc.setWeighty(0.9).incrGridy();
        initVictoire();
        add(panelGagnant, gbc.toConstraints());

        gbc.setWeighty(0.1).incrGridy();
        JPanel panelBtn = initPanelButton();
        add(panelBtn, gbc.toConstraints());

        initAdapters();
    }

    private JLabel initTitle() {
        JLabel title = new JLabel("Victoire !");
        title.setFont(Constants.BOLD_FONT.deriveFont(30f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        return title;
    }

    private void initVictoire() {
        panelGagnant = new JTextPane();
        panelGagnant.setEditable(false);
        panelGagnant.setBackground(Constants.FRAME_BACKGROUND);
        panelGagnant.setBorder(BorderFactory.createEmptyBorder(50,0,0,0));
    }

    private void initGrid(JPanel panel, final int x) {
        for (int i = 0; i < x; i++) {
            JPanel p = new JPanel();
            p.setOpaque(false);
            panel.add(p, new ConstraintBuilder(i, 0).setWeighty(0).setWeightx(1).fillHorizontal().toConstraints());
        }
    }

    private JPanel initPanelButton() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        ConstraintBuilder btnConstraints = new ConstraintBuilder(0,0)
                .fillHorizontal()
                .setWeightx(1)
                .setInset(0,10,0,0)
                .setIpady(20);

        btnRejouer = new ButtonBuilder().setBackground(Constants.BUTTON_BACK_BACKGROUND).setForeground(Constants.BUTTON_BACK_FOREGROUND).setText(Labels.BTN_REJOUER).toJButton();
        btnAccueil = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText(Labels.JEU_ACCUEIL).toJButton();

        panel.add(btnAccueil, btnConstraints.toConstraints());
        btnConstraints.incrGridx();
        panel.add(btnRejouer, btnConstraints.toConstraints());

        return panel;
    }

    private void initAdapters() {
        btnRejouer.addMouseListener(new FinAdapteur(controleur));
        btnAccueil.addMouseListener(new FinAdapteur(controleur));
    }

    public void fixerGagnant(Joueur j) {
        gagnant = j;
        update();
        repaint();
        revalidate();
    }

    public void update() {
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, Font.SANS_SERIF);
        StyleConstants.setBackground(style, Constants.FRAME_BACKGROUND);
        StyleConstants.setForeground(style, Constants.JEU_LABEL_BG);
        StyleConstants.setFontSize(style, 18);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);

        StyledDocument textJoueur = panelGagnant.getStyledDocument();
        try {
            textJoueur.remove(0, textJoueur.getLength());
            textJoueur.insertString(textJoueur.getLength(), "Bravo !\n", style);
            textJoueur.insertString(textJoueur.getLength(), gagnant.getNom() + " gagne\n", style);
            textJoueur.insertString(textJoueur.getLength(), "[" + gagnant.getCouleur().toString().toLowerCase() + "]", style);
            textJoueur.setParagraphAttributes(0, textJoueur.getLength(), style, false);
        } catch (Exception ignored){}
    }
}

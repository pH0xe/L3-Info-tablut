package vue.dialog;

import controleur.CollecteurEvenements;
import controleur.TypeIA;
import vue.adapters.mouseAdapters.OptionsJeuAdapters;
import vue.customComponent.ButtonBuilder;
import vue.customComponent.CustomComboBox;
import vue.customComponent.formPanel;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Labels;
import vue.utils.Names;

import javax.swing.*;
import java.awt.*;

public class DialogOptionJeu extends JPanel {
    private CollecteurEvenements controleur;
    private JButton btnClose, btnAbandon, btnAccueil;
    private JComboBox joueurBlanc, joueurNoir;
    private final String[] comboboxOption = {"Humain", "IA Facile", "IA Moyenne", "IA Difficile"};

    public DialogOptionJeu(CollecteurEvenements c) {
        controleur = c;
        setLayout(new GridBagLayout());
        setBackground(Constants.FRAME_BACKGROUND);

        ConstraintBuilder gbc = new ConstraintBuilder(0,0)
                .setWeightx(1)
                .setWeighty(0.1)
                .fillBoth();

        JLabel title = initTitle();
        add(title, gbc.toConstraints());

        gbc.setWeighty(0.9).setGridy(1);
        JPanel panelButton = initPanelButton();
        add(panelButton, gbc.toConstraints());
        initButtonAdapters();
    }

    private JLabel initTitle() {
        JLabel title = new JLabel("Tablut - Options");
        title.setFont(Constants.BOLD_FONT.deriveFont(30f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        return title;
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
        initGrid(panel, 5);

        JPanel labelComponent;
        ConstraintBuilder btnConstraints = new ConstraintBuilder(1,0)
                .fillHorizontal()
                .setWeightx(1)
                .setInset(0,0,10,0)
                .setIpady(20)
                .setGridWidth(2)
                .setAnchor(GridBagConstraints.PAGE_START);

        joueurBlanc = new CustomComboBox(comboboxOption, Names.OPTIONS_COMBO_BLANC);
        joueurNoir = new CustomComboBox(comboboxOption, Names.OPTIONS_COMBO_NOIR);
        btnClose = new ButtonBuilder().setBackground(Constants.BUTTON_BACK_BACKGROUND).setForeground(Constants.BUTTON_BACK_FOREGROUND).setText(Labels.BTN_FERMER).toJButton();

        btnAbandon = new ButtonBuilder().setBackground(Constants.BUTTON_BACK_BACKGROUND).setForeground(Constants.BUTTON_BACK_FOREGROUND).setText(Labels.JEU_ABANDON).toJButton();
        btnAccueil = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText(Labels.JEU_ACCUEIL).toJButton();

        String[] labels = {"", Labels.OPTIONS_JOUBL, Labels.OPTIONS_JOUNO, "", ""};
        JComponent[] cps = {btnAccueil, joueurBlanc, joueurNoir,btnAbandon, btnClose};
        labelComponent = new formPanel(labels, cps);
        panel.add(labelComponent, btnConstraints.toConstraints());

        return panel;
    }

    public void initButtonAdapters() {
        btnAbandon.addMouseListener(new OptionsJeuAdapters(controleur, this));
        btnAccueil.addMouseListener(new OptionsJeuAdapters(controleur, this));
        btnClose.addMouseListener(new OptionsJeuAdapters(controleur, this));
    }

    public TypeIA getIA(JComboBox combo) {
        String type = combo.getSelectedItem().toString();
        if (type.equalsIgnoreCase("IA Facile"))
            return TypeIA.FACILE;
        if (type.equalsIgnoreCase("IA Moyenne"))
            return TypeIA.MOYENNE;
        if (type.equalsIgnoreCase("IA Difficile"))
            return TypeIA.DIFFICILE;
        return TypeIA.NONE;
    }

    public TypeIA getIANoir() {
        return getIA(joueurNoir);
    }

    public TypeIA getIABlanc() {
        return getIA(joueurBlanc);
    }

    public void update() {
        TypeIA b = controleur.getNiveauIABlanche();
        TypeIA n = controleur.getNiveauIANoir();

        setCombobox(joueurBlanc, b);
        setCombobox(joueurNoir, n);
    }

    private void setCombobox(JComboBox comboBox, TypeIA typeIA) {
        switch (typeIA) {
            case FACILE:
                comboBox.setSelectedItem(comboboxOption[1]);
                break;
            case MOYENNE:
                comboBox.setSelectedItem(comboboxOption[2]);
                break;
            case DIFFICILE:
                comboBox.setSelectedItem(comboboxOption[3]);
                break;
            default:
                comboBox.setSelectedItem(comboboxOption[0]);
                break;
        }
    }
}

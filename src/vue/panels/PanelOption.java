package vue.panels;

import controleur.CollecteurEvenements;
import vue.customComponent.ButtonBuilder;
import vue.customComponent.CustomComboBox;
import vue.customComponent.CustomTextField;
import vue.customComponent.formPanel;
import vue.mouseAdapters.OptionsAdapteur;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Labels;
import vue.utils.Names;

import javax.swing.*;
import java.awt.*;

public class PanelOption extends JPanel {

    private final CollecteurEvenements controleur;
    private JButton btnMusic, btnSon, btnBack;
    private JComboBox joueurBlanc, joueurNoir;
    private JTextField nomJBlanc, nomJNoir;
    private final String[] comboboxOption = {"Humain", "IA Facile", "IA Moyenne", "IA Difficile"};

    public PanelOption(CollecteurEvenements c) {
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

        initAdaptateur();
    }

    private JLabel initTitle() {
        JLabel title = new JLabel("Tablut - Options de jeu");
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

        btnMusic = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText("On").setName(Names.OPTIONS_MUSIC).toJButton();
        btnSon = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText("On").setName(Names.OPTIONS_SOUND).toJButton();
        joueurBlanc = new CustomComboBox(comboboxOption, Names.OPTIONS_COMBO_BLANC);
        joueurNoir = new CustomComboBox(comboboxOption, Names.OPTIONS_COMBO_NOIR);
        nomJBlanc = new CustomTextField(Labels.OPTIONS_FIEBL, Names.OPTIONS_TEXTFIELD_BLANC);
        nomJNoir = new CustomTextField(Labels.OPTIONS_FIENL, Names.OPTIONS_TEXTFIELD_NOIR);
        btnBack = new ButtonBuilder().setBackground(Constants.BUTTON_BACK_BACKGROUND).setForeground(Constants.BUTTON_BACK_FOREGROUND).setText(Labels.BTN_RETOUR).setName(Names.BTN_RETOUR).toJButton();

        String[] labels = {Labels.OPTIONS_MUSIC, Labels.OPTIONS_SOUND, Labels.OPTIONS_JOUBL, Labels.OPTIONS_JOUNO, Labels.OPTIONS_NOJBL,Labels.OPTIONS_NOJNO, ""};
        JComponent[] cps = {btnMusic, btnSon, joueurBlanc, joueurNoir, nomJBlanc, nomJNoir, btnBack};
        labelComponent = new formPanel(labels, cps);
        panel.add(labelComponent, btnConstraints.toConstraints());

        return panel;
    }

    private void initAdaptateur() {
        btnMusic.addMouseListener(new OptionsAdapteur(controleur));
        btnSon.addMouseListener(new OptionsAdapteur(controleur));
        btnBack.addMouseListener(new OptionsAdapteur(controleur));
    }
}

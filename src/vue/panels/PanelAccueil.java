package vue.panels;

import controleur.CollecteurEvenements;
import vue.customComponent.ButtonBuilder;
import vue.adapters.mouseAdapters.AccueilAdapteur;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.*;

public class PanelAccueil extends JPanel {

    private final CollecteurEvenements controleur;
    private JButton btnJouer, btnTuto, btnOptions, btnSauv, btnQuit, btnBest;

    public PanelAccueil(CollecteurEvenements c) {
        controleur = c;
        setLayout(new GridBagLayout());
        setBackground(Constants.FRAME_BACKGROUND);

        ConstraintBuilder gbc = new ConstraintBuilder(0,1)
                .setWeightx(1)
                .setWeighty(0.9)
                .fillBoth();

        JPanel panelButton = initPanelButton();
        add(panelButton, gbc.toConstraints());

        JLabel title = initTitle();
        gbc.setWeighty(0.1).setGridy(0);
        add(title, gbc.toConstraints());

        initButtonAdapter();
    }

    private void initButtonAdapter() {
        btnJouer.addMouseListener(new AccueilAdapteur(controleur));
        btnSauv.addMouseListener(new AccueilAdapteur(controleur));
        btnTuto.addMouseListener(new AccueilAdapteur(controleur));
        btnOptions.addMouseListener(new AccueilAdapteur(controleur));
        btnBest.addMouseListener(new AccueilAdapteur(controleur));
        btnQuit.addMouseListener(new AccueilAdapteur(controleur));
    }

    private JLabel initTitle() {
        JLabel title = new JLabel("Tablut");
        title.setFont(Constants.BOLD_FONT.deriveFont(60f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        return title;
    }

    private void initGrid(JPanel panel, int x, int y) {
        for (int i = 0; i < x; i++) {
            JPanel p = new JPanel();
            p.setOpaque(false);
            panel.add(p, new ConstraintBuilder(i, y).setWeighty(0).setWeightx(1).fillHorizontal().toConstraints());
        }
    }

    private JPanel initPanelButton() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        initGrid(panel, 3,5);

        ConstraintBuilder btnConstraints = new ConstraintBuilder(1,0)
                .fillHorizontal()
                .setWeightx(1)
                .setInset(0,0,10,0)
                .setIpady(20)
                .setAnchor(GridBagConstraints.PAGE_START);

        btnJouer = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText(Labels.ACCUEIL_JOUER).toJButton();
        panel.add(btnJouer, btnConstraints.toConstraints());

        btnConstraints.setGridy(1);
        btnSauv = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText(Labels.ACCUEIL_SAUVE).toJButton();
        panel.add(btnSauv, btnConstraints.toConstraints());

        btnConstraints.setGridy(2);
        btnTuto = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText(Labels.ACCUEIL_DIDAC).toJButton();
        panel.add(btnTuto, btnConstraints.toConstraints());

        btnConstraints.setGridy(3);
        btnOptions = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText(Labels.ACCUEIL_OPTIO).toJButton();
        panel.add(btnOptions, btnConstraints.toConstraints());

        btnConstraints.setGridy(4);
        btnBest = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText(Labels.ACCUEIL_MEJOU).toJButton();
        panel.add(btnBest, btnConstraints.toConstraints());

        btnConstraints.setGridy(5);
        btnQuit = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText(Labels.ACCUEIL_QUITT).toJButton();
        panel.add(btnQuit, btnConstraints.toConstraints());

        return panel;
    }
}

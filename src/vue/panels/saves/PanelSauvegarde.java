package vue.panels.saves;

import controleur.CollecteurEvenements;
import global.Configuration;
import vue.adapters.mouseAdapters.SauvegardeAdapteur;
import vue.customComponent.ButtonBuilder;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelSauvegarde extends JPanel {

    private final CollecteurEvenements controleur;
    private JPanel scrollPanel;

    public PanelSauvegarde(CollecteurEvenements c) {
        controleur = c;
        setLayout(new GridBagLayout());
        setBackground(Constants.FRAME_BACKGROUND);

        ConstraintBuilder gbc = new ConstraintBuilder(1,0)
                .setWeightx(1)
                .setWeighty(0.1)
                .fillBoth();

        JLabel title = initTitle();
        add(title, gbc.toConstraints());

        scrollPanel = new JPanel();
        gbc.setWeighty(0.9).setGridy(1);
        JScrollPane sp = new JScrollPane(scrollPanel);
        sp.setBorder(BorderFactory.createEmptyBorder());
        int initSpeed = sp.getVerticalScrollBar().getUnitIncrement();
        sp.getVerticalScrollBar().setUnitIncrement(initSpeed * 4);
        add(sp, gbc.toConstraints());
        initSaves();

        initBack();
    }

    private void initSaves() {
        scrollPanel.setLayout(new GridBagLayout());
        scrollPanel.setBackground(Constants.FRAME_BACKGROUND);
        update();
    }

    private void initGrid(JPanel panel) {
        ConstraintBuilder bc = new ConstraintBuilder(0,0).setWeightx(1).fillHorizontal();
        for (int i = 0; i < 3; i++) {
            bc.setGridx(i);
            JPanel p = new JPanel();
            p.setOpaque(false);
            panel.add(p, bc.toConstraints());
        }
    }

    private void initBack() {
        ConstraintBuilder gbc = new ConstraintBuilder(1,2)
                .setWeightx(1)
                .setWeighty(0.1)
                .fillHorizontal()
                .setInset(10,0,10,0)
                .setIpady(20);

        initGrid(this);

        JButton btnBack = new ButtonBuilder()
                .setBackground(Constants.BUTTON_BACK_BACKGROUND)
                .setForeground(Constants.BUTTON_BACK_FOREGROUND)
                .setText(Labels.BTN_RETOUR)
                .toJButton();
        add(btnBack, gbc.toConstraints());
        btnBack.addMouseListener(new SauvegardeAdapteur(controleur, SauvegardeAdapteur.BACK, null));
    }

    private JLabel initTitle() {
        JLabel title = new JLabel("Tablut - Sauvegardes");
        title.setFont(Constants.BOLD_FONT.deriveFont(30f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        return title;
    }

    public void update() {
        scrollPanel.removeAll();
        ConstraintBuilder bc = new ConstraintBuilder(1,0).setWeightx(1).fillHorizontal().setIpady(20);
        initGrid(scrollPanel);
        List<String> saves = Configuration.listeFichierSave();
        if (saves == null) return;
        for (String filename : saves) {
            scrollPanel.add(new SavesButton(controleur, filename), bc.toConstraints());
            bc.incrGridy();
        }
        scrollPanel.repaint();
        scrollPanel.revalidate();
    }
}

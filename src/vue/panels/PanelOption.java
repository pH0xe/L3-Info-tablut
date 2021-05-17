package vue.panels;

import controleur.CollecteurEvenements;
import vue.buttons.ButtonBuilder;
import vue.mouseAdapters.*;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;

import javax.swing.*;
import java.awt.*;

public class PanelOption extends JPanel {

    private final CollecteurEvenements controleur;
    private JButton btnMusic, btnSon;

    public PanelOption(CollecteurEvenements c) {
        controleur = c;
        setLayout(new GridBagLayout());
        setBackground(Constants.BACKGROUND_COLOR);
        JLayeredPane lp = new JLayeredPane();

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

    }

    private JLabel initTitle() {
        JLabel title = new JLabel("Tablut");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
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

        btnMusic = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText("On").toJButton();
        panel.add(btnMusic, btnConstraints.toConstraints());

        btnConstraints.setGridy(1);
        btnSon = new ButtonBuilder().setBackground(Constants.BUTTON_BACKGROUND).setForeground(Constants.BUTTON_FOREGROUND).setText("On").toJButton();
        panel.add(btnSon, btnConstraints.toConstraints());

        return panel;
    }
}

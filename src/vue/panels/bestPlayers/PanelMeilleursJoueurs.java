package vue.panels.bestPlayers;

import controleur.CollecteurEvenements;
import global.BestScoresUtils;
import global.Configuration;
import vue.adapters.mouseAdapters.MeilleursAdapteur;
import vue.adapters.mouseAdapters.SauvegardeAdapteur;
import vue.customComponent.ButtonBuilder;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Labels;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PanelMeilleursJoueurs extends JPanel {

    private final CollecteurEvenements controleur;
    private JPanel scrollPanel;

    public PanelMeilleursJoueurs(CollecteurEvenements c) {
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
        gbc.setWeighty(0.9).incrGridy();
        JScrollPane sp = new JScrollPane(scrollPanel);
        sp.setColumnHeaderView(initLabels());
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setBackground(Color.LIGHT_GRAY);

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
        btnBack.addMouseListener(new MeilleursAdapteur(controleur));
    }

    private JLabel initTitle() {
        JLabel title = new JLabel("Tablut - Meilleurs joueurs");
        title.setFont(Constants.BOLD_FONT.deriveFont(30f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        return title;
    }

    public void update() {
        scrollPanel.removeAll();
        ConstraintBuilder bc = new ConstraintBuilder(0,0).setWeightx(1).fillHorizontal().setIpady(20);
        Map<String, Integer> bestP = BestScoresUtils.instance().getScores();
        bestP = sortScores(bestP);
        if (bestP.isEmpty()) return;
        int i = 1;

        bc.incrGridy();
        for (Map.Entry<String, Integer> score : bestP.entrySet()) {
            scrollPanel.add(new ScoreEntry(score.getKey(), score.getValue(), i), bc.toConstraints());
            bc.incrGridy();
            i++;
        }
        scrollPanel.repaint();
        scrollPanel.revalidate();
    }

    private JPanel initLabels() {
        JPanel panelLabel = new JPanel();
        panelLabel.setOpaque(true);
        panelLabel.setBackground(Color.LIGHT_GRAY);
        panelLabel.setLayout(new GridBagLayout());
        ConstraintBuilder bc = new ConstraintBuilder(0,0).setWeightx(1).setWeighty(1).fillBoth().setInset(5,0,5,10);

        JLabel labelName = new JLabel("Nom", SwingConstants.CENTER);
        panelLabel.add(labelName, bc.toConstraints());

        JLabel labelScore = new JLabel("Nombre de victoires", SwingConstants.TRAILING);
        panelLabel.add(labelScore, bc.toConstraints());

        JLabel labelRang = new JLabel("Rang", SwingConstants.LEADING);
        panelLabel.add(labelRang, bc.toConstraints());

        return panelLabel;
    }

    private Map<String,Integer> sortScores(Map<String,Integer> map) {
        List<Map.Entry<String,Integer>> l = new ArrayList<>(map.entrySet());
        l.sort(Map.Entry.comparingByValue());
        Collections.reverse(l);

        Map<String,Integer> res = new LinkedHashMap<>();
        for (Map.Entry<String,Integer> entry : l) {
            res.put(entry.getKey(), entry.getValue());
        }

        return res;
    }
}

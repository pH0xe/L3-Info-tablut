package vue.panels.bestPlayers;

import controleur.CollecteurEvenements;
import global.DateUtils;
import vue.adapters.mouseAdapters.SauvegardeAdapteur;
import vue.customComponent.ButtonBuilder;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Images;

import javax.swing.*;
import java.awt.*;

public class ScoreEntry extends JPanel {
    private final int rank;

    public ScoreEntry(String name, int score, int rank) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        ConstraintBuilder bc = new ConstraintBuilder(0,0).setWeightx(1).setWeighty(1).fillBoth().setInset(5,0,5,10);

        JLabel labelName = new JLabel(name, SwingConstants.CENTER);
        add(labelName, bc.toConstraints());

        JLabel labelScore = new JLabel(String.valueOf(score), SwingConstants.TRAILING);
        add(labelScore, bc.toConstraints());
        this.rank = rank;
        if (rank == 1)
            setBorder(BorderFactory.createMatteBorder(1,0,1,0, Color.BLACK));
        else
            setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));

        if (rank > 3) {
            JLabel labelRank = new JLabel(String.valueOf(rank), SwingConstants.LEADING);
            //labelRank.setBorder(BorderFactory.createEmptyBorder(0,100,0,0));
            add(labelRank, bc.setInset(0,20,0,0).toConstraints());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (rank == 0) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int imgSize = getHeight() / 2;
        int x = 10;
        int y = (getHeight()/2) - (imgSize/2);
        if (rank == 1)
            g2.drawImage(Images.FIRST, x, y, imgSize, imgSize, null);
        else if (rank == 2)
            g2.drawImage(Images.SECOND, x, y, imgSize, imgSize, null);
        else if (rank == 3)
            g2.drawImage(Images.THIRD, x, y, imgSize, imgSize, null);
        super.paintComponent(g);
    }
}

package vue.panels;

import controleur.CollecteurEvenements;
import modele.JeuTuto;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class PanelInstructionTuto extends JPanel {
    private JeuTuto jeu;
    private final CollecteurEvenements controleur;
//    private JTextArea instruction;
    private JTextPane instruction;
//    private JLabel instruction;

    public PanelInstructionTuto(CollecteurEvenements controleur, JeuTuto jeu) {
        this.jeu = jeu;
        this.controleur = controleur;
        jeu.setEtat(0);

        setLayout(new GridBagLayout());
//        setOpaque(false);
        setBackground(Constants.INSTR_LABEL_BG);

//        instruction = new JLabel("Les Blancs Commencent", SwingConstants.CENTER);
//        instruction = new JTextArea("Les Blancs Commencent");
        instruction = new JTextPane();
        instruction.setText("Les Blancs Commencent");
//        instruction.setLineWrap(true);

        StyledDocument doc = instruction.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        instruction.setEditable(false);
        instruction.setFont(Constants.BOLD_FONT);
        instruction.setForeground(Constants.JEU_LABEL_BG);
        instruction.setOpaque(true);
        instruction.setBackground(Constants.INSTR_LABEL_BG);
//        instruction.setBackground(Color.red);
        ConstraintBuilder cb = new ConstraintBuilder(0, 1).setInset(20,10,0,10).setWeightx(0.9).setWeighty(0.1).fillBoth();
        add(instruction, cb.toConstraints());

        update();
    }

    public void update() {
        switch (jeu.getEtat()) {
            case 0 -> instruction.setText("Les Blancs commencent. Cliquer sur le pion indique pour deplacer.");
            default -> System.out.println("Etat invalide.");
        }
    }

    public void addJeu(JeuTuto j){ jeu = j; }

}

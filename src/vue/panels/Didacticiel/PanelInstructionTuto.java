package vue.panels.Didacticiel;

import controleur.CollecteurEvenements;
import modele.JeuTuto;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class PanelInstructionTuto extends JPanel {
    private JeuTuto jeu;
    private final CollecteurEvenements controleur;
    private JTextPane instruction;

    public PanelInstructionTuto(CollecteurEvenements controleur, JeuTuto jeu) {
        this.jeu = jeu;
        this.controleur = controleur;

        setLayout(new GridBagLayout());
        setBackground(Constants.INSTR_LABEL_BG);

        instruction = new JTextPane();
        instruction.setText("Bienvenue dans le tutoriel. Le Jouer Blanc commence.");

        StyledDocument doc = instruction.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);


        instruction.setEditable(false);
        instruction.setFont(Constants.BOLD_FONT);
        instruction.setForeground(Constants.JEU_LABEL_BG);
        instruction.setOpaque(true);
        instruction.setBackground(Constants.INSTR_LABEL_BG);
        ConstraintBuilder cb = new ConstraintBuilder(0, 1).setInset(10,10,0,10).setWeightx(0.8).setWeighty(0.1).fillBoth();
        add(instruction, cb.toConstraints());

    }

    public void update() {
        if (jeu.getEtat() == 0)
            instruction.setText("Bienvenue dans le tutoriel. Le Jouer Blanc commence.");
        else {
            if (jeu.getEtatDeplace() == 0)
                instruction.setText(jeu.getJeu().joueurCourant().getNom() + " : Cliques sur la case indiquée pour choisir le pion.");
            else if (jeu.getEtatDeplace() == 1)
                instruction.setText(jeu.getJeu().joueurCourant().getNom() + " : Cliques sur la case indiquée pour deplacer le pion.");
            else {
                if (jeu.getEtat() == 3)
                    instruction.setText(jeu.getJeu().getJoueurSuivant().getNom() + " a capturé un pion " + jeu.getJeu().joueurCourant().getCouleur().toString().toLowerCase() + ". Un pion est eliminé s'il est entouré par 2 pions adversaires sur la ligne ou la colonne et plusieurs elimination sont possible.");
                else if (jeu.getEtat() == 4)
                    instruction.setText(jeu.getJeu().getJoueurSuivant().getNom() + " a capturer un pion " + jeu.getJeu().joueurCourant().getCouleur().toString().toLowerCase() + ". Un pion est aussi eliminé quand il se retrouve entre un pion adversaire et la case centrale ou la trone.");
                else if (jeu.getEtat() == 8)
                    instruction.setText(jeu.getJeu().getJoueurSuivant().getNom() + " a gagné. Le roi est entouré par 4 pions noirs et il est capturé.");
                else if (jeu.getEtat() == 10)
                    instruction.setText(jeu.getJeu().getJoueurSuivant().getNom() + " a gagné. Comme les poins, quand le roi est entouré par 3 pions noirs et la case centrale ou la trone, le roi est capturé.");
                else if (jeu.getEtat() == 11)
                    instruction.setText(jeu.getJeu().getJoueurSuivant().getNom() + " a gagné. Le roi a réussit à s'échapper en se mettant sur une case de bord. Tu as terminé le tutoriel. Bonne chance pour la prochaine partie.");
                else
                    instruction.setText("Le tour de " + jeu.getJeu().joueurCourant().getNom() + ".");
            }
            instruction.setText(instruction.getText() + "\n [Cliques pour continuer]");
        }
    }

    public void addJeu(JeuTuto j){ jeu = j; }

}

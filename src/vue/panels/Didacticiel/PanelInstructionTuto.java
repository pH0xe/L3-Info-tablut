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
        instruction.setText("Bienvenue dans le tutoriel.\n\tLe joueur blanc commence.\n\t[Cliquez pour Continuer]");

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
            instruction.setText("Bienvenue dans le tutoriel.\n\tLe joueur blanc commence.\n\t[Cliquez pour Continuer]");
        else {
            if (jeu.getEtatDeplace() == 0)
                instruction.setText(jeu.getJeu().joueurCourant().getNom() + " : Cliquez sur la case indiquée pour choisir le pion.");
            else if (jeu.getEtatDeplace() == 1)
                instruction.setText(jeu.getJeu().joueurCourant().getNom() + " : Cliquez sur la case indiquée pour déplacer le pion.");
            else {
                if (jeu.getEtat() == 3)
                    instruction.setText(jeu.getJeu().getJoueurSuivant().getNom() + " a capturé un pion " + jeu.getJeu().joueurCourant().getCouleur().toString().toLowerCase() + ". Un pion est éliminé s'il est entouré par 2 pions adverses sur la ligne ou la colonne. Un même coup permet plusieurs captures.");
                else if (jeu.getEtat() == 4)
                    instruction.setText(jeu.getJeu().getJoueurSuivant().getNom() + " a capturé un pion " + jeu.getJeu().joueurCourant().getCouleur().toString().toLowerCase() + ". Un pion est aussi éliminé quand il se retrouve entre un pion adverse et la case centrale (appelée le trône).");
                else if (jeu.getEtat() == 8)
                    instruction.setText(jeu.getJeu().getJoueurSuivant().getNom() + " a gagné. Le roi est entouré par 4 pions noirs et il est capturé.");
                else if (jeu.getEtat() == 10)
                    instruction.setText(jeu.getJeu().getJoueurSuivant().getNom() + " a gagné. Comme les pions, quand le roi est entouré par 3 pions noirs et le trône, le roi est capturé.");
                else if (jeu.getEtat() == 11)
                    instruction.setText(jeu.getJeu().getJoueurSuivant().getNom() + " a gagné. Le roi a réussi à s'échapper en se mettant sur une case du bord. Vous avez terminé le tutoriel. Bonne chance pour la prochaine partie. :)");
                else
                    instruction.setText("Au tour du " + jeu.getJeu().joueurCourant().getNom() + ".");
            }
            instruction.setText(instruction.getText() + "\n [Cliquez pour continuer]");
        }
    }

    public void addJeu(JeuTuto j){ jeu = j; }

}

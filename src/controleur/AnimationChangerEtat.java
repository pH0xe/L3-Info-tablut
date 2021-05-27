package controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

public class AnimationChangerEtat implements ActionListener {
    CollecteurEvenements controleur;

    public AnimationChangerEtat(CollecteurEvenements controleur){
        this.controleur = controleur;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controleur.animationChangerEtat();
        controleur.stopTimer();
    }
}

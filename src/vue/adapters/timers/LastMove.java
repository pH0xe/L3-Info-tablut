package vue.adapters.timers;

import controleur.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LastMove implements ActionListener {
    private final CollecteurEvenements controleur;

    public LastMove(CollecteurEvenements controleur) {
        this.controleur = controleur;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controleur.drawLastMove(false);
    }
}

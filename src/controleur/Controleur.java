package controleur;

import modele.Jeu;
import vue.InterfaceGraphique;

public class Controleur implements CollecteurEvenements {
    private Jeu j;
    private InterfaceGraphique it;

    public Controleur(){

    }

    @Override
    public void fixerInterface(InterfaceGraphique interfaceGraphique) {
        it = interfaceGraphique;
    }

    @Override
    public void fixerJeu(Jeu jeu) {
        j = jeu;
    }

    @Override
    public void demarrerJeu(int l, int c, String player1, String player2) {

    }
}

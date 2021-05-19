package controleur;

import modele.Jeu;
import vue.InterfaceGraphique;

public interface CollecteurEvenements {
    void fixerInterface(InterfaceGraphique it);
    void fixerJeu(Jeu j);
    void demarrerJeu();
}

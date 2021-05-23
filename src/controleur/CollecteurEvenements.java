package controleur;

import modele.util.Point;
import vue.InterfaceGraphique;

public interface CollecteurEvenements {
    void fixerInterface(InterfaceGraphique it);
    void demarrerJeu();
    void ouvrirOption();
    void fermerOption(String nomJoueurBlanc, String nomJoueurNoir, TypeIA typeJB, TypeIA typeJN);
    void cliquePlateau(Point point);
}

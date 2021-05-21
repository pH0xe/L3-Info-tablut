package controleur;

import modele.Jeu;
import modele.JeuTuto;
import vue.InterfaceGraphique;

public interface CollecteurEvenements {
    void clicSourisTuto(int l, int c);
    void fixerInterface(InterfaceGraphique it);
    void fixerJeu(Jeu j);
    void fixerJeuTuto(JeuTuto jeu);
    void demarrerJeu();
}

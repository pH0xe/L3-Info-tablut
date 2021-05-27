package controleur;

import modele.Jeu;
import modele.JeuTuto;
import vue.InterfaceGraphique;

import javax.swing.*;

public interface CollecteurEvenements {
    void clicSourisTuto(int l, int c);
    void animationChangerEtat();
    void clicRefaireTuto();
    void loadPlateauTuto(String filename);
    void stopTimer();
    void fixerInterface(InterfaceGraphique it);
    void fixerJeu(Jeu j);
    void fixerJeuTuto(JeuTuto jeu);
    void fixerTimer(Timer tm);
    void demarrerJeu();
}

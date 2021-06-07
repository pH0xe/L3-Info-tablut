package controleur;

import modele.util.Point;
import modele.Jeu;
import modele.JeuTuto;
import vue.InterfaceGraphique;

import javax.swing.*;

public interface CollecteurEvenements {
    // Fonctionnement Tour de jeu
    void demarrerJeu();
    void cliquePlateau(Point point);
    void joueIA1();
    void joueIA2();
    void refaireCoup();
    void annulerCoup();
    void abandonnerPartie();
    void rejouer();
    void drawLastMove(boolean show);

    // Options
    void ouvrirOption();
    void fermerOption(String nomJoueurBlanc, String nomJoueurNoir, TypeIA typeJB, TypeIA typeJN);

    // Options en jeu
    void ouvrirOptionJeu();
    void fermerOptionJeu(TypeIA typeIAB, TypeIA typeIAN);

    // Dialogue Sauvegarde
    void afficherDialogSauv(int afterAction);
    void sauvegarderAccueil();
    void sauvegarderQuitter();

    // Sauvegarde
    void ouvrirSauvegarde();
    void quitterSauvegarde();
    void supprimerSauvegarde(String filename);
    void chargerSauvegarde(String filename);

    // Scores
    void ouvrirMeilleursJoueurs();
    void fermerMeilleursJoueurs();

    // Utilitaires
    void retourAccueil();
    void fermerApp();
    void fixerInterface(InterfaceGraphique it);

    // IA
    boolean estTourIA();
    TypeIA getNiveauIANoir();
    TypeIA getNiveauIABlanche();

    // Tutoriel
    void fixerJeuTuto(JeuTuto jeu);
    void clicSourisTuto(int l, int c);
    void clicRefaireTuto();
    void clicAnnulerTuto();
    void ouvrirDidacticiel();
    void fermerDidacticiel();
    void retourAccueilTuto();
}

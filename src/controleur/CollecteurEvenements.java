package controleur;

import modele.util.Point;
import vue.InterfaceGraphique;

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
}

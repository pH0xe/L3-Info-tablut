package controleur;

import modele.util.Point;
import vue.InterfaceGraphique;

public interface CollecteurEvenements {
    void fixerInterface(InterfaceGraphique it);
    void demarrerJeu();
    void ouvrirOption();
    void fermerOption(String nomJoueurBlanc, String nomJoueurNoir, TypeIA typeJB, TypeIA typeJN);
    void cliquePlateau(Point point);
    void ouvrirOptionJeu();
    void refaireCoup();
    void annulerCoup();
    void fermerOptionJeu(TypeIA typeIAB, TypeIA typeIAN);
    void abandonnerPartie();
    void retourAccueil();

    void sauvegarderAccueil();
    void sauvegarderQuitter();
    void fermerApp();

    void afficherDialogSauv(int afterAction);
    void ouvrirSauvegarde();

    void quitterSauvegarde();
    void supprimerSauvegarde(String filename);
    void chargerSauvegarde(String filename);
}

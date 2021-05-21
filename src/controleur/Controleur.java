package controleur;

import global.Configuration;
import modele.*;
import modele.Joueur.Couleur;
import modele.Joueur.Joueur;
import modele.util.Point;
import vue.InterfaceGraphique;

public class Controleur implements CollecteurEvenements {
    private Jeu jeu;
    private InterfaceGraphique interfaceGraphique;
    private Joueur joueurBlanc, joueurNoir;
    private boolean estPause;

    public Controleur(){
        joueurBlanc = new Joueur("Joueur blanc", Couleur.BLANC);
        joueurNoir = new Joueur("Joueur noir", Couleur.NOIR);
        jeu = new Jeu(joueurBlanc, joueurNoir);
        estPause = false;
    }

    @Override
    public void fixerInterface(InterfaceGraphique interfaceGraphique) {
        this.interfaceGraphique = interfaceGraphique;
    }

    @Override
    public void demarrerJeu() {
        interfaceGraphique.fixerJeu(jeu);
    }

    @Override
    public void ouvrirOption() {
        interfaceGraphique.ouvrirOption();
    }

    @Override
    public void fermerOption(String nomJoueurBlanc, String nomJoueurNoir, TypeIA typeJB, TypeIA typeJN) {
        joueurBlanc.setNom(nomJoueurBlanc);
        joueurNoir.setNom(nomJoueurNoir);
        // TODO ajouter les IA;
        interfaceGraphique.fermerOption();
    }

    @Override
    public void cliquePlateau(Point point) {
        if (estPause) return;
        if (jeu.verifierCoup(point)) {
            if (jeu.roiSorti())
                Configuration.instance().logger().info("ROI SORTI");
                // TODO dialog de fin
            return;
        }
        jeu.verifierPion(point);
    }

    @Override
    public void ouvrirOptionJeu() {
        // TODO on stop les deux IA et on empeche de cliquer sur le plateau
        estPause = true;
        interfaceGraphique.ouvrirDialogOption();
    }

    @Override
    public void refaireCoup() {
        jeu.refaireCoup();
        // TODO tempo IA
    }

    @Override
    public void annulerCoup() {
        jeu.annulerCoup();
        // TODO tempo IA
    }

    @Override
    public void fermerOptionJeu(TypeIA typeIAB, TypeIA typeIAN) {

    }

    @Override
    public void abandonnerPartie() {

    }

    @Override
    public void retourAccueil() {

    }
}

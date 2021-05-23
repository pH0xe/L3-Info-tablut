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

    public Controleur(){
        joueurBlanc = new Joueur("Joueur blanc", Couleur.BLANC);
        joueurNoir = new Joueur("Joueur noir", Couleur.NOIR);
        jeu = new Jeu(joueurBlanc, joueurNoir);
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
        // TODO supprimer IA si necessaire
        interfaceGraphique.fermerOption();
    }

    @Override
    public void cliquePlateau(Point point) {
        if (jeu.verifierCoup(point)) {
            if (jeu.roiSorti()){
                Configuration.instance().logger().info("ROI SORTI");
                // TODO dialog de fin
            }
            return;
        }
        jeu.verifierPion(point);
    }

    @Override
    public void ouvrirOptionJeu() {
        // TODO on stop les deux IA
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
        // TODO changer les IA / les suppr
        // TODO changer le nom si necessaire
        // TODO relancer les IA
        interfaceGraphique.fermerDialogOption();
    }

    @Override
    public void abandonnerPartie() {
        // TODO enregistrer VICTOIRE
        // TODO afficher la dialog de fin
        // TODO arreter les IA;
        interfaceGraphique.fermerDialogOption();
    }

    @Override
    public void retourAccueil() {
        // TODO arreter les IA
        interfaceGraphique.fermerDialogOption();
        interfaceGraphique.retourAccueil("jeu");
        jeu = new Jeu(joueurBlanc, joueurNoir);
    }
}

package controleur;

import modele.*;
import vue.InterfaceGraphique;

public class Controleur implements CollecteurEvenements {
    private Jeu jeu;
    private InterfaceGraphique interfaceGraphique;
    private Joueur joueurBlanc, joueurNoir;

    public Controleur(){
        joueurBlanc = new Joueur("Joueur blanc", TypeJoueur.BLANC);
        joueurNoir = new Joueur("Joueur noir", TypeJoueur.NOIR);
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
        interfaceGraphique.fermerOption();
    }

    @Override
    public void cliquePlateau(Point point) {
        if (jeu.verifierCoup(point)) return;
        jeu.verifierPion(point);
    }
}

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
}

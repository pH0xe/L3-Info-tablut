package controleur;

import controleur.IA.IA;
import controleur.IA.IADifficile;
import controleur.IA.IAFacile;
import global.Configuration;
import modele.*;
import vue.InterfaceGraphique;
import vue.adapters.AdaptateurIA;

import javax.swing.*;


public class Controleur implements CollecteurEvenements {
    private Jeu jeu;
    private InterfaceGraphique interfaceGraphique;
    private Joueur joueurBlanc, joueurNoir;
    private IA iaBlanc, iaNoir;
    private Timer tIAB, tIAN;

    public Controleur(){
        joueurBlanc = new Joueur("Joueur blanc", Couleur.BLANC);
        joueurNoir = new Joueur("Joueur noir", Couleur.NOIR);
        jeu = new Jeu(joueurBlanc, joueurNoir);
        tIAB = new Timer(2000, new AdaptateurIA(this, 1));
        tIAN = new Timer(2000, new AdaptateurIA(this, 2));
    }

    @Override
    public void fixerInterface(InterfaceGraphique interfaceGraphique) {
        this.interfaceGraphique = interfaceGraphique;
    }

    @Override
    public void demarrerJeu() {
        interfaceGraphique.fixerJeu(jeu);
        if(iaBlanc!=null){
            tIAB.start();
        }
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
        iaBlanc = definirIa(typeJB);
        iaNoir = definirIa(typeJN);
        interfaceGraphique.fermerOption();
    }

    @Override
    public void cliquePlateau(Point point) {
        if(jeu.joueurCourant().getCouleur()==Couleur.BLANC && iaBlanc != null)
            return;
        if(jeu.joueurCourant().getCouleur()==Couleur.NOIR && iaNoir != null)
            return;
        if (jeu.verifierCoup(point)) {
           verifFin();
           return;
        }
        jeu.verifierPion(point);
    }

    public IA definirIa(TypeIA type){
        switch (type){
            case FACILE:
                return new IAFacile();
            case MOYENNE:
                return new IAFacile();
            case DIFFICILE:
                return new IADifficile();
            default:
                return null;
        }
    }

    @Override
    public void joueIA1() {
        tIAB.stop();
        Coup c = iaBlanc.iaJoue(jeu);
        jeu.joueCoup(c);
        //jeu.getPlateau().affichePlateau();
        System.out.println();
        verifFin();
    }

    @Override
    public void joueIA2() {
        tIAN.stop();
        Coup c = iaNoir.iaJoue(jeu);
        jeu.joueCoup(c);
        verifFin();
    }

    public void verifFin(){
        if (jeu.roiSorti())
            System.out.println("rois sorti");
        else if (jeu.roiCapture())
            System.out.println("roi pris");
            // TODO dialog de fin
        else {
            if(jeu.joueurCourant().getCouleur()==Couleur.BLANC && iaBlanc != null)
                tIAB.start();
            if(jeu.joueurCourant().getCouleur()==Couleur.NOIR && iaNoir != null)
                tIAN.start();
        }
    }
}

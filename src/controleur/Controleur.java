package controleur;

import controleur.IA.IA;
import controleur.IA.IADifficile;
import controleur.IA.IAFacile;
import global.BestScoresUtils;
import global.Configuration;
import global.reader.BoardReaderBinary;
import global.writer.BoardWriterBinary;
import modele.*;
import modele.Joueur.Couleur;
import modele.Joueur.Joueur;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Coup;
import modele.util.Point;
import vue.InterfaceGraphique;
import vue.adapters.AdaptateurIA;

import javax.swing.*;


import java.io.File;

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
        lancerTimerIA();
    }

    @Override
    public void ouvrirOption() {
        interfaceGraphique.ouvrirOption();
    }

    @Override
    public void fermerOption(String nomJoueurBlanc, String nomJoueurNoir, TypeIA typeJB, TypeIA typeJN) {
        joueurBlanc.setNom(nomJoueurBlanc);
        joueurNoir.setNom(nomJoueurNoir);
        iaBlanc = definirIa(typeJB);
        iaNoir = definirIa(typeJN);
        interfaceGraphique.fermerOption();
    }

    @Override
    public void cliquePlateau(Point point) {
        if (jeu.estFini()) return;
        if (estTourIA()) return;
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
        if (jeu.roiSorti()) {
            interfaceGraphique.ouvrirDialogFin(jeu.getJoueurBlanc());
            BestScoresUtils.instance().addVictory(jeu.getJoueurBlanc().getNom());
        } else if (jeu.roiCapture()) {
            interfaceGraphique.ouvrirDialogFin(jeu.getJoueurNoir());
            BestScoresUtils.instance().addVictory(jeu.getJoueurNoir().getNom());
        } else {
            lancerTimerIA();
        }
    }

    private void lancerTimerIA() {
        if(jeu.joueurCourant().getCouleur()==Couleur.BLANC && iaBlanc != null)
            tIAB.start();
        if(jeu.joueurCourant().getCouleur()==Couleur.NOIR && iaNoir != null)
            tIAN.start();
    }

    private void stoperIA() {
        tIAB.stop();
        tIAN.stop();
    }

    @Override
    public void ouvrirOptionJeu() {
        stoperIA();
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
        iaBlanc = definirIa(typeIAB);
        iaNoir = definirIa(typeIAN);
        interfaceGraphique.fermerDialogOption();
        lancerTimerIA();
    }

    @Override
    public void abandonnerPartie() {
        stoperIA();
        interfaceGraphique.fermerDialogOption();
        interfaceGraphique.ouvrirDialogFin(jeu.getJoueurSuivant());
        BestScoresUtils.instance().addVictory(jeu.getJoueurSuivant().getNom());
        jeu.setEstFini(true);
    }

    @Override
    public void retourAccueil() {
        stoperIA();
        interfaceGraphique.fermerDialogOption();
        interfaceGraphique.fermerDialogFin();
        interfaceGraphique.retourAccueil();
        jeu = new Jeu(joueurBlanc, joueurNoir);
    }

    @Override
    public void sauvegarderAccueil() {
        BoardWriterBinary bw = new BoardWriterBinary();
        try {
            bw.ecrireJeu(jeu);
        } catch (Exception ignored) {}
        retourAccueil();
    }

    @Override
    public void sauvegarderQuitter() {
        BoardWriterBinary bw = new BoardWriterBinary();
        try {
            bw.ecrireJeu(jeu);
        } catch (Exception ignored) {}
        fermerApp();
    }

    @Override
    public void fermerApp() {
        System.exit(0);
    }

    @Override
    public void afficherDialogSauv(int afterAction) {
        interfaceGraphique.afficherDialogSauvegarde(afterAction);
    }

    @Override
    public void ouvrirSauvegarde() {
        interfaceGraphique.ouvrirSauvegarde();
    }

    @Override
    public void quitterSauvegarde() {
        interfaceGraphique.quitterSauvegarde();
    }

    @Override
    public void supprimerSauvegarde(String filename) {
        File file = new File("saves" + File.separator + filename);
        if (file.delete())
            interfaceGraphique.update();
    }

    @Override
    public void chargerSauvegarde(String filename) {
        BoardReaderBinary br = new BoardReaderBinary("data" + File.separator + "saves" + File.separator + filename);
        br.lirePlateau();
        jeu = new Jeu(br);
        joueurBlanc = br.getJoueurBlanc();
        joueurNoir = br.getJoueurNoir();
        demarrerJeu();
    }

    @Override
    public void rejouer() {
        interfaceGraphique.fermerDialogFin();
        jeu = new Jeu(joueurBlanc, joueurNoir);
        demarrerJeu();
    }

    @Override
    public boolean estTourIA() {
        return (jeu.joueurCourant().getCouleur()==Couleur.BLANC && iaBlanc != null) || (jeu.joueurCourant().getCouleur()==Couleur.NOIR && iaNoir != null);
    }

    @Override
    public void ouvrirMeilleursJoueurs() {
        interfaceGraphique.ouvrirMeilleursJoueurs();
    }

    @Override
    public void fermerMeilleursJoueurs() {
        interfaceGraphique.fermerMeilleursJoueurs();
    }
}

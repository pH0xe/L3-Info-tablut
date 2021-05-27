package controleur;

import global.Configuration;
import global.reader.BoardReaderText;
import modele.Jeu;
import modele.JeuTuto;
import controleur.IA.IA;
import controleur.IA.IADifficile;
import controleur.IA.IAFacile;
import global.BestScoresUtils;
import global.reader.BoardReaderBinary;
import global.writer.BoardWriterBinary;
import modele.Joueur.Couleur;
import modele.Joueur.Joueur;
import modele.util.Coup;
import modele.util.Point;
import vue.InterfaceGraphique;
import vue.adapters.AdaptateurIA;

import javax.swing.*;


import java.io.File;

import java.io.InputStream;

public class Controleur implements CollecteurEvenements {
    private Jeu jeu;
    private JeuTuto jt;
    private InterfaceGraphique interfaceGraphique;
    private Joueur joueurBlanc, joueurNoir;
    private IA iaBlanc, iaNoir;
    private Timer tIAB, tIAN;
    Timer timer = new Timer(10,null);

    public Controleur(){
        joueurBlanc = new Joueur("Joueur blanc", Couleur.BLANC);
        joueurNoir = new Joueur("Joueur noir", Couleur.NOIR);
        jeu = new Jeu(joueurBlanc, joueurNoir);
        tIAB = new Timer(2000, new AdaptateurIA(this, 1));
        tIAN = new Timer(2000, new AdaptateurIA(this, 2));
    }

    @Override
    public void clicSourisTuto(int l, int c) {
        switch (jt.getEtat()){
            case 0:
                jt.setHighlightCase(4,3);
                break;
            case 1:
                jt.traiteDeplacement(l,c,2,3);
                break;
            case 2:
                jt.traiteDeplacement(l,c,3,3);
                break;
            case 3:
                jt.traiteDeplacement(l,c,4,3);
                break;
            case 4:
                jt.traiteDeplacement(l,c,4,2);
                break;
            case 5:
                jt.traiteDeplacement(l,c,6,0);
                break;
            case 6:
                jt.traiteDeplacement(l,c,6,1);
                break;
            case 7:
                jt.traiteDeplacement(l,c,5,5);
                break;
            case 8:
                jt.traiteDeplacement(l,c,5,2);
                break;
            case 9:
                jt.traiteDeplacement(l,c, 5,7);
                break;
            case 10:
                jt.traiteDeplacement(l,c, 5,3);
                break;
            case 11:
                jt.traiteDeplacement(l,c, 2,8);
                break;
            case 12:
                jt.traiteDeplacement(l,c, 4,2);
                break;
            case 13:
                jt.traiteDeplacement(l,c, 0,8);
                break;
        }
        if( jt.getEtat() == 0)
            jt.setEtat(1);
        if(jt.getEtat() == 5 && jt.getEtatDeplace() == 0)
            loadPlateauTuto("TutoBoard2.txt");
        if(jt.getEtat() == 9 && jt.getEtatDeplace() == 0)
            loadPlateauTuto("TutoBoard3.txt");
        if(jt.getEtat() == 11 && jt.getEtatDeplace() == 0)
            loadPlateauTuto("TutoBoard4.txt");
        interfaceGraphique.update();
        if(jt.getEtat() == 14)
            clicRefaireTuto();
        if(jt.getEtatDeplace() == 2 && !(jt.getUnanimateEtat().contains(jt.getEtat())))
                timer.start();
    }

    @Override
    public void animationChangerEtat() {
        switch (jt.getEtat()){
            case 1:
                jt.setHighlightCase(3,0);
                break;
            case 2:
                jt.setHighlightCase(4,2);
                break;
            case 5:
                jt.setHighlightCase(7,1);
                break;
            case 6:
                jt.setHighlightCase(6,5);
                break;
            case 7:
                jt.setHighlightCase(6,2);
                break;
            case 9:
                jt.setHighlightCase(5,0);
                break;
            case 11:
                jt.setHighlightCase(4,1);
                break;
            case 12:
                jt.setHighlightCase(2,8);
                break;
        }
        jt.setEtatDeplace(0);
        interfaceGraphique.update();
        jt.setEtat(jt.getEtat()+1);
    }

    @Override
    public void stopTimer(){ timer.stop(); }

    @Override
    public void clicRefaireTuto() {
        jt = new JeuTuto(new Jeu(new Joueur("Jouer1", Couleur.BLANC), new Joueur("Jouer2", Couleur.NOIR)), 0);
        interfaceGraphique.addJeuTuto(jt);
        interfaceGraphique.update();
    }

    @Override
    public void loadPlateauTuto(String filename) {
        InputStream in = Configuration.charger("tutorials" + File.separator + filename);
        BoardReaderText br = new BoardReaderText(in);
        br.lirePlateau();
        //jt.getJeu().getPlateau().setPlateau(br);
        interfaceGraphique.update();
        Configuration.instance().logger().info("Loaded board for tutorial : " + filename);
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
    public void fixerJeuTuto(JeuTuto jeu){ jt = jeu;}

    @Override
    public void fixerTimer(Timer tm) { timer = tm; }

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
        stoperIA();
        jeu.refaireCoup();
        lancerTimerIA();
    }

    @Override
    public void annulerCoup() {
        stoperIA();
        jeu.annulerCoup();
        lancerTimerIA();
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
        File file = new File("data" + File.separator + "saves" + File.separator + filename);
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

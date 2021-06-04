package controleur;

import global.Configuration;
import global.reader.BoardReaderText;
import modele.Jeu;
import modele.JeuTuto;
import controleur.IA.IA;
import controleur.IA.IADifficile;
import controleur.IA.IAFacile;
import controleur.IA.IAMoyenne;
import global.BestScoresUtils;
import global.Configuration;
import global.reader.BoardReaderBinary;
import global.reader.BoardReaderText;
import global.writer.BoardWriterBinary;
import modele.Joueur.Couleur;
import modele.Joueur.Joueur;
import modele.Plateau;
import modele.util.Coup;
import modele.util.Point;
import vue.InterfaceGraphique;
import vue.adapters.AdaptateurIA;
import vue.adapters.timers.LastMove;

import javax.swing.*;
import java.io.File;

import java.io.InputStream;

public class Controleur implements CollecteurEvenements {
    private Jeu jeu;
    private JeuTuto jt;
    private InterfaceGraphique interfaceGraphique;
    private Joueur joueurBlanc, joueurNoir;
    private IA iaBlanc, iaNoir;
    private Timer tIAB, tIAN, rechercheIA, tLastMove;

    public Controleur(){
        joueurBlanc = new Joueur("Joueur blanc", Couleur.BLANC);
        joueurNoir = new Joueur("Joueur noir", Couleur.NOIR);
        jeu = new Jeu(joueurBlanc, joueurNoir);
        jt = new JeuTuto(new Jeu(new Joueur("Joueur Blanc", Couleur.BLANC), new Joueur("Joueur Noir", Couleur.NOIR)), 0);
        tIAB = new Timer(2000, new AdaptateurIA(this, 1));
        tIAN = new Timer(2000, new AdaptateurIA(this, 2));
        tLastMove = new Timer(2000, new LastMove(this));
        tLastMove.start();
    }

    ////////////////////////////////////////////////
    // Fonctionnement tour de jeu
    ////////////////////////////////////////////////
    /**
     * fixe un jeu a une interface puis lance les IAs
     */
    @Override
    public void demarrerJeu() {
        interfaceGraphique.fixerJeu(jeu);
        lancerTimerIA();
    }

    /**
     * Vérifie l'état du jeu, si la partie est en cours selectionne un pion ou joue un coup.
     * Si joue un coup vérifie si la partie est terminé.
     * @param point les cordonnées ligne et colonne de l'endroit cliqué
     */
    @Override
    public void cliquePlateau(Point point) {
        if (jeu.estFini()) return;
        if (estTourIA()) return;
        if (jeu.verifierCoup(point)) {
            verifFin();
            drawLastMove(false);
            return;
        }
        jeu.verifierPion(point);
    }

    /**
     * Joue un coup pour l'IA 1 (Joueur Blanc)
     */
    @Override
    public void joueIA1() {
        joueIA(iaBlanc);
    }

    /**
     * Joue un coup pour l'IA 2 (Joueur Noir)
     */
    @Override
    public void joueIA2() {
        joueIA(iaNoir);
    }

    /**
     * Arrete les timer des IA.
     * Récupére le coup que l'ia passé en parametre doit jouer.
     * Joue le coup.
     * Vérifie la fin de la partie.
     * @param ia l'IA devant jouer un coup
     */
    private void joueIA(IA ia) {
        stoperIA();
        Coup c = ia.iaJoue(jeu);
        jeu.joueCoup(c);
        verifFin();
        tLastMove.restart();
        drawLastMove(true);
    }

    /**
     * Stop les IAs.
     * Demande au jeu de refaire le coup suivant. Puis relance la bonne IA.
     */
    @Override
    public void refaireCoup() {
        stoperIA();
        jeu.refaireCoup();
        if (!jeu.estFini())
            lancerTimerIA();
    }

    /**
     * Stop les IAs. Demande au jeu d'annuler le dernier coup. Puis relance la bonne IA.
     */
    @Override
    public void annulerCoup() {
        stoperIA();
        jeu.annulerCoup();
        if (!jeu.estFini())
            lancerTimerIA();
    }

    /**
     * Stop les IAs. Ferme la dialogue d'abandon, ouvre la dialogue de gagnant puis enregistre la victoire de l'adversaire. Indique au jeu que la partie est terminer pour bloquer les cliques plateau
     */
    @Override
    public void abandonnerPartie() {
        stoperIA();
        interfaceGraphique.fermerDialogOption();
        interfaceGraphique.ouvrirDialogFin(jeu.getJoueurSuivant());
        BestScoresUtils.instance().addVictory(jeu.getJoueurSuivant().getNom());
        jeu.setEstFini(true);
    }

    /**
     * Une fois la partie terminé (sur la dialogue de fin). Permet de rejouer une partie sans repasser par l'accueil. Ferme la dialogue de fin puis créé un nouveau jeu avec les meme joueurs puis demarre la partie.
     */
    @Override
    public void rejouer() {
        interfaceGraphique.fermerDialogFin();
        jeu = new Jeu(joueurBlanc, joueurNoir);
        demarrerJeu();
    }

    /**
     * Après un coup joué vérifie si le roi est sortie ou capturé. Si c'est le cas ouvre la dialogue de fin et enregistre la victoire. Sinon la partie continue, on relance les Timer IA si nécessaire.
     */
    public void verifFin(){
        if (jeu.roiSorti()) {
            jeu.setEstFini(true);
            interfaceGraphique.ouvrirDialogFin(jeu.getJoueurBlanc());
            BestScoresUtils.instance().addVictory(jeu.getJoueurBlanc().getNom());
        } else if (jeu.roiCapture()) {
            jeu.setEstFini(true);
            interfaceGraphique.ouvrirDialogFin(jeu.getJoueurNoir());
            BestScoresUtils.instance().addVictory(jeu.getJoueurNoir().getNom());
        } else {
            lancerTimerIA();
        }
    }

    /**
     * Affiche le dernier coup joué
     */
    @Override
    public void drawLastMove(boolean show) {
        interfaceGraphique.toggleLastMove(show);
    }

    ////////////////////////////////////////////////
    // Options
    ////////////////////////////////////////////////

    /**
     * Ouvre le menu d'option de l'accueil
     */
    @Override
    public void ouvrirOption() {
        interfaceGraphique.ouvrirOption();
    }

    /**
     * Ferme le menu d'option et reviens a l'accueil
     * @param nomJoueurBlanc Le nom du joueurs Blanc si il s'agit d'un humain ou la difficulté de l'IA si il s'agit d'une IA
     * @param nomJoueurNoir Le nom du joueur Noir si il s'agit d'un humain ou la difficulté de l'IA si il s'agit d'une IA
     * @param typeJB Le Type de IA Blanche (Humain, Facile, Difficile ou Moyenne)
     * @param typeJN Le Type de IA Noir (Humain, Facile, Difficile ou Moyenne)
     */
    @Override
    public void fermerOption(String nomJoueurBlanc, String nomJoueurNoir, TypeIA typeJB, TypeIA typeJN) {
        joueurBlanc.setNom(nomJoueurBlanc);
        joueurNoir.setNom(nomJoueurNoir);
        iaBlanc = definirIa(typeJB);
        iaNoir = definirIa(typeJN);
        interfaceGraphique.fermerOption();
    }

    ////////////////////////////////////////////////
    // Options en jeu
    ////////////////////////////////////////////////

    /**
     * Met le jeu en Pause, Bloque les IA et empeche d'interragir avec le jeu. Ouvre la dialogue d'option en jeu (Accueil, abandon ou changement d'IA)
     */
    @Override
    public void ouvrirOptionJeu() {
        stoperIA();
        interfaceGraphique.ouvrirDialogOption();
    }

    /**
     * Ferme la dialogue d'option, relance les IA, Met a jours la difficulté des IA et réautorise l'interraction avec le plateau.
     * @param typeIAB Le nouveau type de L'IA Blanche
     * @param typeIAN Le nouveau type de l'IA Noir
     */
    @Override
    public void fermerOptionJeu(TypeIA typeIAB, TypeIA typeIAN) {
        iaBlanc = definirIa(typeIAB);
        iaNoir = definirIa(typeIAN);
        interfaceGraphique.fermerDialogOption();
        if (!jeu.estFini())
            lancerTimerIA();
    }

    ////////////////////////////////////////////////
    // Dialogue de sauvegarde
    ////////////////////////////////////////////////

    /**
     * Affiche une dialogue pour demander si la partie doit etre enregistré avant une action sensible (Fermeture de l'app ou retour a l'accueil)
     * @param afterAction Action devant etre fait après la sauvegarde (ou non) - retour a l'accueil ou femeture de l'application
     */
    @Override
    public void afficherDialogSauv(int afterAction) {
        interfaceGraphique.afficherDialogSauvegarde(afterAction);
    }

    /**
     * Sauvegarde le jeu dans le répertoire data/saves dans un fichier binaire .dat a la date et heure actuel. puis retour à l'accueil.
     */
    @Override
    public void sauvegarderAccueil() {
        BoardWriterBinary bw = new BoardWriterBinary();
        try {
            bw.ecrireJeu(jeu);
        } catch (Exception ignored) {}
        retourAccueil();
    }

    /**
     * Sauvegarde le jeu dans le répertoire data/saves dans un fichier binaire .dat a la date et heure actuel. puis fermeture de l'app.
     */
    @Override
    public void sauvegarderQuitter() {
        BoardWriterBinary bw = new BoardWriterBinary();
        try {
            bw.ecrireJeu(jeu);
        } catch (Exception ignored) {}
        fermerApp();
    }

    ////////////////////////////////////////////////
    // Sauvegardes
    ////////////////////////////////////////////////

    /**
     * Ouvre la fenetre pour lister les sauvegardes
     */
    @Override
    public void ouvrirSauvegarde() {
        interfaceGraphique.ouvrirSauvegarde();
    }

    /**
     * Ferme la fenetre ou sont listé les sauvegardes et retourne à l'accueil
     */
    @Override
    public void quitterSauvegarde() {
        interfaceGraphique.quitterSauvegarde();
    }

    /**
     * Supprime la sauvegarde data/saves/&lt;filename&gt;.dat
     * @param filename le nom du fichier a supprimer
     */
    @Override
    public void supprimerSauvegarde(String filename) {
        File file = new File("data" + File.separator + "saves" + File.separator + filename);
        if (file.delete())
            interfaceGraphique.update();
    }

    /**
     * Charge la sauvegarde data/saves/&lt;filename&gt;.dat pour la jouer. Récupére les données dans le fichier puis lance un jeu avec ces données.
     * @param filename le nom du fichier a charger
     */
    @Override
    public void chargerSauvegarde(String filename) {
        BoardReaderBinary br = new BoardReaderBinary("data" + File.separator + "saves" + File.separator + filename);
        br.lirePlateau();
        jeu = new Jeu(br);
        joueurBlanc = br.getJoueurBlanc();
        joueurNoir = br.getJoueurNoir();
        demarrerJeu();
    }

    ////////////////////////////////////////////////
    // Scores
    ////////////////////////////////////////////////

    /**
     * Ouvre la fenetre des scores. Liste les meilleurs joueurs (Le plus de victoire).
     */
    @Override
    public void ouvrirMeilleursJoueurs() {
        interfaceGraphique.ouvrirMeilleursJoueurs();
    }

    /**
     * Ferme la fenetre de score et retourne a l'accueil
     */
    @Override
    public void fermerMeilleursJoueurs() {
        interfaceGraphique.fermerMeilleursJoueurs();
    }

    ////////////////////////////////////////////////
    // Utilitaire
    ////////////////////////////////////////////////

    /**
     * Stop les IAs, ferme les différentes dialogue, puis retourne a l'accueil. <br>
     * Créé un nouveau jeu
     */
    @Override
    public void retourAccueil() {
        stoperIA();
        interfaceGraphique.fermerDialogOption();
        interfaceGraphique.fermerDialogFin();
        interfaceGraphique.retourAccueil();
        jeu = new Jeu(joueurBlanc, joueurNoir);
    }

    /**
     * Arrete l'application
     */
    @Override
    public void fermerApp() {
        System.exit(0);
    }

    /**
     * Fixe une interface graphique a un controleur
     * @param interfaceGraphique l'interface graphique sur lequel le controleur doit travailler
     */
    @Override
    public void fixerInterface(InterfaceGraphique interfaceGraphique) {
        this.interfaceGraphique = interfaceGraphique;
    }

    ////////////////////////////////////////////////
    // IA
    ////////////////////////////////////////////////

    /**
     * Créé une IA en fonction du type passé en parametre
     * @param type Le type/ difficulté de l'IA a créé.
     * @return une nouvelle IA (peut etre <code>null</code> si il s'agit d'un humain)
     * @nu
     */
    public IA definirIa(TypeIA type){
        switch (type){
            case FACILE:
                return new IAFacile();
            case MOYENNE:
                return new IAMoyenne();
            case DIFFICILE:
                return new IADifficile();
            default:
                return null;
        }
    }

    /**
     * Vérifie si il s'agit du tour d'une IA. Si c'est le cas lance le Timer concerné.
     */
    private void lancerTimerIA() {
        if(jeu.joueurCourant().getCouleur()==Couleur.BLANC && iaBlanc != null)
            tIAB.start();
        if(jeu.joueurCourant().getCouleur()==Couleur.NOIR && iaNoir != null)
            tIAN.start();
    }

    /**
     * Stop le timer des 2 IA
     */
    private void stoperIA() {
        tIAB.stop();
        tIAN.stop();
    }

    /**
     * Indique si Il s'agit du tour d'une IA
     * @return <code>true</code> si c'est le tour d'une IA, <code>false</code> sinon
     */
    @Override
    public boolean estTourIA() {
        return (jeu.joueurCourant().getCouleur()==Couleur.BLANC && iaBlanc != null) || (jeu.joueurCourant().getCouleur()==Couleur.NOIR && iaNoir != null);
    }

    /**
     * Permet d'obtenir le niveau actuel de l'ia blanche
     * @return TypeIA le niveau de l'ia blanche
     */
    @Override
    public TypeIA getNiveauIABlanche() {
        return getNiveauIA(iaBlanc);
    }

    /**
     * Permet d'obtenir le niveau actuel de l'ia noir
     * @return TypeIA le niveau de l'ia noir
     */
    @Override
    public TypeIA getNiveauIANoir() {
        return getNiveauIA(iaNoir);
    }

    /**
     * Permet d'obtenir le niveau actuel de l'ia passé en parametre
     * @param ia dont il faut détérminer le niveau
     * @return TypeIA le niveau de l'ia passé en paramétre
     */
    private TypeIA getNiveauIA(IA ia) {
        if (ia instanceof IAFacile) {
            return TypeIA.FACILE;
        } else if (ia instanceof  IADifficile) {
            return TypeIA.DIFFICILE;
        }
        return TypeIA.NONE;
    }

    ////////////////////////////////////////////////
    // Tutoriel
    ////////////////////////////////////////////////
    @Override
    public void ouvrirDidacticiel() {
        interfaceGraphique.addJeuTuto(jt);
        interfaceGraphique.ouvrirDidacticiel();
    }

    @Override
    public void fermerDidacticiel() {
        interfaceGraphique.fermerDidacticiel();
    }

    @Override
    public void retourAccueilTuto(){
        fixerJeuTuto(new JeuTuto(new Jeu(new Joueur("Joueur Blanc", Couleur.BLANC), new Joueur("Joueur Noir", Couleur.NOIR)), 0));
        fermerDidacticiel();
    }

    @Override
    public void fixerJeuTuto(JeuTuto jeu){
        jt = jeu;
        interfaceGraphique.addJeuTuto(jeu);
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
        }
        if( jt.getEtat() == 0)
            jt.setEtat(1);
        if(jt.getEtat() == 5 && jt.getEtatDeplace() == 0)
            loadPlateauTuto("TutoBoard2.txt");
        if(jt.getEtat() == 9 && jt.getEtatDeplace() == 0)
            loadPlateauTuto("TutoBoard3.txt");
        if(jt.getEtat() == 11 && jt.getEtatDeplace() == 0)
            loadPlateauTuto("TutoBoard4.txt");
        if(jt.getEtat() == 12)
            fixerJeuTuto(new JeuTuto(new Jeu(new Joueur("Joueur Blanc", Couleur.BLANC), new Joueur("Joueur Noir", Couleur.NOIR)), 0));
        interfaceGraphique.update();
    }

    @Override
    public void clicRefaireTuto() {
        jt = new JeuTuto(new Jeu(new Joueur("Joueur1", Couleur.BLANC), new Joueur("Joueur2", Couleur.NOIR)), 0);
        interfaceGraphique.addJeuTuto(jt);
        interfaceGraphique.update();
    }

    @Override
    public void clicAnnulerTuto() {
        jt.annulerCoupTuto();
        interfaceGraphique.update();
    }

    public void loadPlateauTuto(String filename) {
        InputStream in = Configuration.charger("tutorials" + File.separator + filename);
        BoardReaderText br = new BoardReaderText(in);
        br.lirePlateau();
        jt.getJeu().addPlateau(new Plateau(br));
        interfaceGraphique.update();
        Configuration.instance().logger().info("Loaded board for tutorial : " + filename);
        System.out.println("Loaded board for tutorial : " + filename);
    }
}

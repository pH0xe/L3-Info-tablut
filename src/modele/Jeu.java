package modele;

import global.Configuration;
import global.Operateur;
import global.reader.BoardReaderBinary;
import modele.Joueur.Couleur;
import modele.Joueur.Joueur;
import modele.pion.EtatPion;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Coup;
import modele.util.Point;
import structure.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Jeu extends Observable {
    private Joueur joueurCourant;
    private final Joueur j1;
    private final Joueur j2;
    private Plateau pt;
    private final Stack<Coup> coupsPrecedent, coupsSuivant;
    private Pion pionSelect;
    private boolean estFini = false;

    ////////////////////////////////////////////////
    // Constructeurs
    ////////////////////////////////////////////////

    /**
     * Créé un nouveau jeu avec les 2 joueurs passé en paramétre
     * @param j1 Le joueur blanc, celui qui commence
     * @param j2 Le joueur noir, le deuxième a joué
     */
    public Jeu(Joueur j1, Joueur j2){
        this.j1 = j1;
        this.j2 = j2;
        this.joueurCourant = j1;
        pt = new Plateau();
        coupsPrecedent = new Stack<>();
        coupsSuivant = new Stack<>();
    }

    /**
     * Créé un jeu a partir d'une sauvegarde. Rétablt tout les attribut de classe comme dans la sauvegarde;
     * @param br Le lecteur de sauvegarde. Connait toute les valeurs de la classe.
     */
    public Jeu(BoardReaderBinary br) {
        coupsPrecedent = br.getCoupsPrecedent();
        coupsSuivant = br.getCoupsSuivant();
        j2 = br.getJoueurNoir();
        j1 = br.getJoueurBlanc();
        joueurCourant = br.getJoueurCourant();
        pt = new Plateau(br);
    }

    ////////////////////////////////////////////////
    // Gestion des tours
    ////////////////////////////////////////////////

    /**
     * Retourne le joueur dont c'est le tour
     * @return le joueur dont c'est le tour
     */
    public Joueur joueurCourant(){
        return joueurCourant;
    }

    /**
     * Passe le tour au joueur suivant.
     */
    public void joueurSuivant(){
        joueurCourant = getJoueurSuivant();
    }

    /**
     * @return le joueur suivant
     */
    public Joueur getJoueurSuivant(){
        if(joueurCourant.equals(j1)){
            return j2;
        }else{
            return j1;
        }
    }

    /**
     * @return les pions du joueur courant
     */
    public List<Pion> getPionsCourant() {
        if (joueurCourant().getCouleur() == Couleur.BLANC) {
            return pt.getBlancs();
        } else {
            return pt.getNoirs();
        }
    }

    /**
     * Met a jour le pion actuellement selectionné. Utile dans la construction en 2 etapes du coup pour un joueur.
     * @param point les coordonnées de la case cliquée
     */
    public void setSelectionner(Point point) {
        Pion tmp = pt.getPion(point);
        if (tmp.getCouleur() != joueurCourant.getCouleur()) pionSelect = null;
        if (tmp != pionSelect)
            pionSelect = tmp;
        else
            pionSelect = null;
        update();
    }

    /**
     * @return le pion actuellement selectionné.
     */
    public Pion getSelectionne() {
        return pionSelect;
    }

    ////////////////////////////////////////////////
    // Gestion des coups
    ////////////////////////////////////////////////

    /**
     * Joue le coup passé en paramétre si cela est possible. <br>
     * Vérifie la capture des pions après le déplacement. Enregistre les pions capturé dans le coup pour l'historique <br>
     * Efface la liste des coup suivant et met a jour la liste des anciens coup. <br>
     * Passe au joueur suivant. <br>
     * Met a jour l'interface graphique
     * @param c le coup a jouer
     */
    public void joueCoup(Coup c){
        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            coupsPrecedent.push(c);
            coupsSuivant.clear();
            c.setCaptures(pionCapture(pion));
            joueurSuivant();
            update();
        } else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getL() + "," + pion.getPosition().getC() + ") -> " + destination.getL() + "," + destination.getC());
    }

    /**
     * Utilisé par l'IA joue un coup sur une copie du jeu <br>
     * Joue le coup passé en paramétre si cela est possible. <br>
     * Vérifie la capture des pions après le déplacement. Enregistre les pions capturé dans le coup pour l'annulation lors du MinMax <br>
     * Passe au joueur suivant. <br>
     * @param c le coup a jouer
     * @return le Jeu après le coup
     */
    public Jeu joueCoupDuplique(Coup c){
        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            c.setCaptures(this.pionCapture(pion));
            this.joueurSuivant();
        }
        else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getL() + "," + pion.getPosition().getC() + ") -> " + destination.getL() + "," + destination.getC());
        return this;
    }

    /**
     * Annule une liste de coup. Réactive et remet dans le jeu tout les pions eliminé par cette liste de coup. <br>
     * Déplace le dernier pions vers ca position d'origine destL, destc
     * @param c la liste de coup a annuler
     * @param destL La ligne d'origine du pions deplacé dans cette liste
     * @param destC La colonne d'origine du pion déplacé dans cette liste
     */
    public void annulerCoup(List<Coup> c, int destL, int destC){
        Coup dernier = c.get(c.size()-1);
        List<Pion> captures = dernier.getCaptures();

        if(!captures.isEmpty()) {
            for (Pion p : captures) {
                if(p != null) {
                    p.changerEtat(EtatPion.ACTIF);
                    pt.getPions().add(p);
                }
            }
        }
        this.getPlateau().deplacerPion(pt.getPion(dernier.getPion()), destL, destC);
        joueurSuivant();
        c.remove(c.size()-1);
    }

    /**
     * Annule le dernier coup dans la liste des coups précédent. <br>
     * Réactive tous les pions éliminé par ce coup. <br>
     * Passe au joueur suivant. <br>
     * Rajoute dans la liste des coups suivant le coup <br>
     * Met a jour l'interface graphique.
     */
    public void annulerCoup() {
        Coup c = coupsPrecedent.pop();
        for (Pion pion : c.getCaptures()) {
            if (pion == null) continue;
            pion.changerEtat(EtatPion.ACTIF);
            pt.getPions().add(pion);
        }

        Point dest = c.getOrigine();

        Pion p = pt.getPion(c.getDestination());
        pt.deplacerPion(p, dest.getL(), dest.getC());
        joueurSuivant();
        coupsSuivant.push(c);
        update();
    }

    /**
     * Refait le dernier coup qui a été annulé.
     */
    public void refaireCoup() {
        Coup c = coupsSuivant.pop();

        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            coupsPrecedent.push(c);
            update();
        } else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getC() + "," + pion.getPosition().getL() + ") -> " + destination.getL() + "," + destination.getC());
    }

    ////////////////////////////////////////////////
    // Etat du jeu
    ////////////////////////////////////////////////

    /**
     * Vérifie la position du roi et indique si celui si est sortie ou non.
     * @return true si le rois est sortie, false sinon
     */
    public boolean roiSorti() {
        Point roiPos = pt.getRoi().getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        return (roiC == 0 || roiL == 0 || roiC == 8 || roiL == 8);
    }

    /**
     * Vérifie si le roi a été capturé ou non.
     * @return true si le roi est capturé, false sinon
     */
    public boolean roiCapture() {
        Point roiPos = pt.getRoi().getPosition();
        int c = roiPos.getC();
        int l = roiPos.getL();
        return (checkRoi(l+1, c)
                && checkRoi(l-1, c)
                && checkRoi(l, c+1)
                && checkRoi(l, c-1));

    }

    /**
     * Test Si la case (l,c) est un pion noir ou le trone.
     * @param l la ligne de la case testé
     * @param c la colonne de la case testé
     * @return true si la case est un trone ou un pion noir, false sinon
     */
    private boolean checkRoi(int l, int c) {
        return (l == 4 && c == 4) || pt.estCaseDeType(l, c, TypePion.NOIR);
    }

    /**
     * Indique l'etat du jeu, fini ou non
     * @return true si le jeu est fini, false sinon
     */
    public boolean estFini() {
        return estFini || roiCapture() || roiSorti();
    }

    /**
     * force la fin d'une partie dans le cas d'un abandon.
     * @param b la valeur que estFini doit prendre.
     */
    public void setEstFini(boolean b) {
        this.estFini = b;
    }

    ////////////////////////////////////////////////
    // Capture du Pion
    ////////////////////////////////////////////////
    /**
     * Vérifie la présence de pion a capturer autours du pion passé en parametre. <br>
     * Vérifie dans les quatre direction. <br>
     * Si un pion est capturer il est supprimer de la liste des pion actif est ajouté au un list qui sera retourné par la fonction.
     * @param pion le pion autouirs du quel il faut vérifier.
     * @return la liste des pions qui ont été capturé.
     */
    public List<Pion> pionCapture(Pion pion){
        Point posPion = pion.getPosition();
        int pionC = posPion.getC();
        int pionL = posPion.getL();
        List<Pion> captures = new ArrayList<>();

        Operateur[][] ops = {
                {Operateur.SUB,Operateur.NOTHING},
                {Operateur.ADD,Operateur.NOTHING},
                {Operateur.NOTHING,Operateur.SUB},
                {Operateur.NOTHING,Operateur.ADD}
        };

        for (Operateur[] op : ops) {
            if (checkPion(pionL, pionC, op[0], op[1], pion.getCouleur())) {
                Pion p = pt.capturerPion(new Point(op[0].faire(pionL, 1), op[1].faire(pionC, 1)));
                if (p != null)
                    captures.add(p);
            }
        }
        return captures;
    }

    /**
     * Utilisé par {@link #pionCapture(Pion) pionCapture(pion) } pour tester si le pion a coté de la case d'origine passé en paramétre doit etre capturé ou non. <br>
     * Le pion a 1 case doit etre de la couleur opossé a celle passé en paramètre. <br>
     * Le pion a 2 case de l'origine doit etre de la meme couleur. <br>
     * @param l la ligne d'origine du test
     * @param c la colonne d'origine du test
     * @param opL opération de décalage qui doit etre appliqué a la ligne d'origine. Permet de tester les 4 direction.
     * @param opC opération de décalage qui doit etre appliqué a la colonne d'origine. Permet de tester les 4 direction.
     * @param couleur couleur du pion en (l,c)
     * @return true si le pion doit etre capturé, false sinon.
     */
    private boolean checkPion(int l, int c, Operateur opL, Operateur opC, Couleur couleur) {
        if (opL.faire(l,2) > 8 || opL.faire(l,2) < 0) return false;
        if (opC.faire(c,2) > 8 || opC.faire(c,2) < 0) return false;

        return pt.estCaseDeCouleur(opL.faire(l,1), opC.faire(c,1), couleur.getOppose())
                && (pt.estCaseDeCouleur(opL.faire(l,2), opC.faire(c,2), couleur)
                || ((l == 4 && c == 4 )|| (opL.faire(l,2) == 4 && opC.faire(c,2) == 4)));
    }

    /**
     * Utilisé par {@link controleur.Controleur#cliquePlateau(Point) cliquePlateau(point)} permet de verifier la validité d'un clique. <br>
     * Verifie la première étape du clique d'un joueur, la selection du pion a bouger.
     * @param point le point ou ce situe le pion selectionné.
     */
    public void verifierPion(Point point) {
        if (deMemeCouleur(point) && estClickable(point))
            setSelectionner(point);

    }

    /**
     * Utilisé par {@link #verifierPion(Point)} vérifie que le pion au coordonné passé en paramétre fait partie des pion séléctionnable du joueur.
     * @param point le coordonnées du pion a vérifier
     * @return true si le pion fait partie des pion qui peuvent etre bougé dans le tour courant, false sinon
     */
    private boolean estClickable(Point point) {
        Pion p = pt.getPion(point);
        return getPionClickable().contains(p);
    }

    /**
     * Utilisé par {@link #verifierPion(Point)} vérifie si le pion fait parti des pion du joueur dont c'est le tour.
     * @param point les coordonné du pion a vérifier.
     * @return true si le pion appartient au joueur courant, false sinon
     */
    private boolean deMemeCouleur(Point point) {
        TypePion tmp = pt.getTypePion(point);
        if (tmp == null) return false;
        return tmp.getCouleur() ==  joueurCourant.getCouleur();
    }

    /**
     * Utilisé par {@link controleur.Controleur#cliquePlateau(Point) cliquePlateau(point)} permet de verifier la validité d'un clique. <br>
     * Verifie la seconde étape du clique d'un joueur, le déplacement du pion sélectionné. <br>
     * Joue un coup si le clique est valide sinon ne fait rien.
     * @param point les coordonnées du pion à vérifier
     * @return true si un coup a été joué, false sinon.
     */
    public boolean verifierCoup(Point point) {
        if (pionSelect == null) return false;

        List<Point> accessible = pt.getCasesAccessibles(pionSelect);
        if (accessible.contains(point)) {
            Coup coup = new Coup(pionSelect, point, pionSelect.getPosition().getL(), pionSelect.getPosition().getC());
            joueCoup(coup);
            pionSelect = null;
            return true;
        }
        return false;
    }

    ////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////
    public List<Point> getClickable() {
        return pt.getCasesAccessibles(pionSelect);
    }

    public List<Pion> getPionClickable() {
        if (estFini()) return new ArrayList<>();
        List<Pion> pions = getPionsCourant();
        return pions.stream().filter(pion -> !pt.getCasesAccessibles(pion).isEmpty()).collect(Collectors.toList());
    }

    public Plateau getPlateau() {
        return pt;
    }

    public List<Coup> getListeCoups(){
        List<Coup> C = new ArrayList<>();
        List<Pion> jouables = this.getPionsCourant();
        for (Pion pi: jouables) {
            List<Point> accessibles = pt.getCasesAccessibles(pi);
            for (Point pt: accessibles) {
                C.add(new Coup(pi, pt, pi.getPosition().getL(), pi.getPosition().getC()));
            }
        }
        return C;
    }

    public Joueur getJoueurBlanc() {
        return j1;
    }

    public Joueur getJoueurNoir() {
        return j2;
    }

    public Stack<Coup> getCoupsSuivant() {
        return coupsSuivant;
    }

    public Stack<Coup> getCoupsPrecedent() {
        return coupsPrecedent;
    }
}

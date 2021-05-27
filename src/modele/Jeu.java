package modele;

import global.Configuration;
import global.reader.BoardReader;
import global.reader.BoardReaderBinary;
import global.reader.BoardReaderText;
import modele.Joueur.*;
import modele.pion.*;
import modele.util.*;
import structure.Observable;

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

    public Jeu(Joueur j1, Joueur j2){
        this.j1 = j1;
        this.j2 = j2;
        this.joueurCourant = j1; // j1 = blancs
        pt = new Plateau();
        coupsPrecedent = new Stack<>();
        coupsSuivant = new Stack<>();
    }

    public Joueur joueurCourant(){
        return joueurCourant;
    }

    public void joueurSuivant(){
        joueurCourant = getJoueurSuivant();
    }

    public Joueur getJoueurSuivant(){
        if(joueurCourant == j1){
            return j2;
        }else{
            return j1;
        }
    }

    public List<Pion> getPionCourant() {
        if (joueurCourant().getCouleur() == Couleur.BLANC) {
            return pt.getBlancs();
        } else {
            return pt.getNoirs();
        }
    }

    public void joueCoup(Coup c){
        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            coupsPrecedent.push(c);
            coupsSuivant.clear();
            if(roiSorti())
                System.out.println(joueurCourant + " a gagné.");
            else if(roiCapture())
                System.out.println(getJoueurSuivant() + " a gagné");
            else
                pionCapture(pion);
            joueurSuivant();
            update();
        } else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getC() + "," + pion.getPosition().getL() + ") -> " + destination.getL() + "," + destination.getC());
    }

    public boolean roiSorti() {
        Point roiPos = pt.getRoi().getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        return (roiC == 0 || roiL == 0 || roiC == 8 || roiL == 8);
    }

    public boolean roiCapture() {
        Point roiPos = pt.getRoi().getPosition();
        int c = roiPos.getC();
        int l = roiPos.getL();
        return (checkRoi(l+1, c)
                && checkRoi(l-1, c)
                && checkRoi(l, c+1)
                && checkRoi(l, c-1));

    }

    private boolean checkRoi(int l, int c) {
        return (l == 4 && c == 4) || pt.estCaseDeType(l, c, TypePion.NOIR);
    }

//    pionCapture updated : Capture de pion par rapport a la trone
    public void pionCapture(Pion pion){
        Point posPion = null;
        int pionC = pion.getPosition().getC();
        int pionL = pion.getPosition().getL();

        if(pionL-2 >= 0 && pt.estCaseDeCouleur(pionL-1, pionC, pion.getCouleur().getOppose()) && (pt.estCaseDeCouleur(pionL-2, pionC, pion.getCouleur()) || pt.estTrone(new Point(pionL-2, pionC))))
            posPion = new Point(pionL-1, pionC);

        if (pionL+2 <= 8 && pt.estCaseDeCouleur(pionL+1, pionC, pion.getCouleur().getOppose()) && (pt.estCaseDeCouleur(pionL+2, pionC, pion.getCouleur()) || pt.estTrone(new Point(pionL+2, pionC))))
            posPion = new Point(pionL+1, pionC);

        if (pionC-2 >= 0 && pt.estCaseDeCouleur(pionL, pionC-1, pion.getCouleur().getOppose()) && (pt.estCaseDeCouleur(pionL, pionC-2, pion.getCouleur()) || pt.estTrone(new Point(pionL, pionC-2))))
            posPion = new Point(pionL, pionC-1);

        if ((pionC+2 <= 8 && pt.estCaseDeCouleur(pionL, pionC+1, pion.getCouleur().getOppose()) && (pt.estCaseDeCouleur(pionL, pionC+2, pion.getCouleur()) || pt.estTrone(new Point(pionL, pionC+2)))))
            posPion = new Point(pionL, pionC+1);

        if(posPion != null && !(posPion.getL() == pt.getRoi().getPosition().getL() && posPion.getC() == pt.getRoi().getPosition().getC())) {
            System.out.println(posPion.toString());
            System.out.println(pt.getRoi().toString());
            pt.capturerPion(posPion, pion);
        }
    }

    public Plateau getPlateau() {
        return pt;
    }

    public void annulerCoup() {
        // TODO annuler le coup dans jeu
        Coup c = coupsPrecedent.pop();
        coupsSuivant.push(c);
    }

    public void refaireCoup() {
        Coup c = coupsSuivant.pop();

        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            coupsPrecedent.push(c);
        } else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getC() + "," + pion.getPosition().getL() + ") -> " + destination.getL() + "," + destination.getC());
    }

    public void setSelectionner(Point point) {
        Pion tmp = pt.trouverPion(point, joueurCourant.getCouleur());
        if (tmp != pionSelect)
            pionSelect = tmp;
        else
            pionSelect = null;
        update();
    }

    public Pion getSelectionne() {
        return pionSelect;
    }

    public List<Point> getClickable() {
        return pt.getCasesAccessibles(pionSelect);
    }

    public void verifierPion(Point point) {
        if (deMemeCouleur(point) && estClickable(point))
            setSelectionner(point);

    }

    private boolean estClickable(Point point) {
        Pion p = pt.trouverPion(point, joueurCourant.getCouleur());
        return getPionClickable().contains(p);
    }

    private boolean deMemeCouleur(Point point) {
        TypePion tmp = pt.getTypePion(point);
        if (tmp == null) return false;
        return tmp.getCouleur() ==  joueurCourant.getCouleur();
    }

    public boolean verifierCoup(Point point) {
        if (pionSelect == null) return false;

        List<Point> accessible = pt.getCasesAccessibles(pionSelect);
        if (accessible.contains(point)) {
            Coup coup = new Coup(pionSelect, point);
            joueCoup(coup);
            coupsPrecedent.add(coup);
            pionSelect = null;
            return true;
        }
        return false;
    }

    public List<Pion> getPionClickable() {
        List<Pion> pions = getPionCourant();
        return pions.stream().filter(pion -> !pt.getCasesAccessibles(pion).isEmpty()).collect(Collectors.toList());
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
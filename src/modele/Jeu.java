package modele;

import global.Configuration;
import global.Operateur;
import global.reader.BoardReaderBinary;
import modele.Joueur.Couleur;
import modele.Joueur.Joueur;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Coup;
import modele.util.Point;
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
        this.joueurCourant = j1;
        pt = new Plateau();
        coupsPrecedent = new Stack<>();
        coupsSuivant = new Stack<>();
    }

    public Jeu(BoardReaderBinary br) {
        coupsPrecedent = br.getCoupsPrecedent();
        coupsSuivant = br.getCoupsSuivant();
        j2 = br.getJoueurNoir();
        j1 = br.getJoueurBlanc();
        joueurCourant = br.getJoueurCourant();
        pt = new Plateau(br);
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

    public void pionCapture(Pion pion){
        Point posPion = pion.getPosition();
        int pionC = posPion.getC();
        int pionL = posPion.getL();

        Operateur[][] ops = {
                {Operateur.SUB,Operateur.NOTHING},
                {Operateur.ADD,Operateur.NOTHING},
                {Operateur.NOTHING,Operateur.SUB},
                {Operateur.NOTHING,Operateur.ADD}
        };

        for (Operateur[] op : ops) {
            if(checkPion(pionL, pionC, op[0], op[1], pion.getCouleur()))
                pt.capturerPion(new Point(op[0].faire(pionL,1), op[1].faire(pionC,1)), pion);
        }
    }

    private boolean checkPion(int l, int c, Operateur opL, Operateur opC, Couleur couleur) {
        if (opL.faire(l,2) > 8 || opL.faire(l,2) < 0) return false;
        if (opC.faire(c,2) > 8 || opC.faire(c,2) < 0) return false;

        return pt.estCaseDeCouleur(opL.faire(l,1), opC.faire(c,1), couleur.getOppose())
                && pt.estCaseDeCouleur(opL.faire(l,2), opC.faire(c,2), couleur);
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
        Pion tmp = pt.getPion(point);
        if (tmp.getCouleur() != joueurCourant.getCouleur()) pionSelect = null;
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
        Pion p = pt.getPion(point);
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

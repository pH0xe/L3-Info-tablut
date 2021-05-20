package modele;

import global.Configuration;
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
    private final Stack<Coup> coupsPrecedant, coupsSuivant;
    private Pion pionSelect;

    public Jeu(Joueur j1, Joueur j2){
        this.j1 = j1;
        this.j2 = j2;
        this.joueurCourant = j1; // j1 = blancs
        pt = new Plateau();
        coupsPrecedant = new Stack<>();
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
            coupsPrecedant.push(c);
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
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        if(roiC == 3 && roiL == 4)
            return (pt.estCaseDeType(roiPos.getL(), roiPos.getC()-1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()-1, roiPos.getC(), TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()+1, roiPos.getC(), TypePion.NOIR));
        if(roiC == 5 && roiL == 4)
            return (pt.estCaseDeType(roiPos.getL(), roiPos.getC()+1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()-1, roiPos.getC(), TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()+1, roiPos.getC(), TypePion.NOIR));
        if(roiC == 4 && roiL == 3)
            return (pt.estCaseDeType(roiPos.getL(), roiPos.getC()-1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL(), roiPos.getC()+1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()-1, roiPos.getC(), TypePion.NOIR));
        if(roiC == 4 && roiL == 5)
            return (pt.estCaseDeType(roiPos.getL(), roiPos.getC()+1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL(), roiPos.getC()-1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()+1, roiPos.getC(), TypePion.NOIR));
        try {
            return (pt.estCaseDeType(roiPos.getL()+1, roiPos.getC(), TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()-1, roiPos.getC(), TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL(), roiPos.getC()+1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL(), roiPos.getC()-1, TypePion.NOIR));
        } catch (Exception e){
            return false;
        }
    }

    public void pionCapture(Pion pion){
        Point posPion = pion.getPosition();
        int pionC = posPion.getC();
        int pionL = posPion.getL();
        // TODO methode pas bonne
        /*
        if(pion.getType().getCouleur() == Couleur.BLANC){
            if(pt.estCaseDeCouleur(pionL, pionC - 1, Couleur.NOIR) && pt.estCaseDeCouleur(pionL, pionC+1, Couleur.NOIR))
                return true;

            if(pt.estCaseDeCouleur(pionL-1, pionC, Couleur.NOIR) && pt.estCaseDeCouleur(pionL+1, pionC, Couleur.NOIR))
                return true;
        } else if(pion.getType().getCouleur() == Couleur.NOIR){
            if(pt.estCaseDeCouleur(pionL, pionC - 1, Couleur.BLANC) && pt.estCaseDeCouleur(pionL, pionC+1, Couleur.BLANC))
                return true;

            if(pt.estCaseDeCouleur(pionL-1, pionC, Couleur.BLANC) && pt.estCaseDeCouleur(pionL+1, pionC, Couleur.BLANC))
                return true;
        }
        return false;
         */
        if(pionL-2 >= 0 && pt.estCaseDeCouleur(pionL-1, pionC, pion.getCouleur().getOppose()) && pt.estCaseDeCouleur(pionL-2, pionC, pion.getCouleur()))
            pt.capturerPion(new Point(pionL-1, pionC), pion);

        if (pionL+2 <= 8 && pt.estCaseDeCouleur(pionL+1, pionC, pion.getCouleur().getOppose()) && pt.estCaseDeCouleur(pionL+2, pionC, pion.getCouleur()))
            pt.capturerPion(new Point(pionL+1, pionC), pion);

        if (pionC-2 >= 0 && pt.estCaseDeCouleur(pionL, pionC-1, pion.getCouleur().getOppose()) && pt.estCaseDeCouleur(pionL, pionC-2, pion.getCouleur()))
            pt.capturerPion(new Point(pionL, pionC-1), pion);

        if ((pionC+2 <= 8 && pt.estCaseDeCouleur(pionL, pionC+1, pion.getCouleur().getOppose()) && pt.estCaseDeCouleur(pionL, pionC+2, pion.getCouleur())))
            pt.capturerPion(new Point(pionL, pionC+1), pion);

    }

    public Plateau getPlateau() {
        return pt;
    }

    public void annulerCoup() {
        // TODO annuler le coup dans jeu
        Coup c = coupsPrecedant.pop();
        coupsSuivant.push(c);
    }

    public void refaireCoup() {
        Coup c = coupsSuivant.pop();

        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            coupsPrecedant.push(c);
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
            coupsPrecedant.add(coup);
            pionSelect = null;
            return true;
        }
        return false;
    }

    public List<Pion> getPionClickable() {
        List<Pion> pions = getPionCourant();
        return pions.stream().filter(pion -> !pt.getCasesAccessibles(pion).isEmpty()).collect(Collectors.toList());
    }
}

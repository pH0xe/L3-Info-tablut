package modele;

import global.Configuration;
import structure.Observable;

import java.util.List;
import java.util.Stack;

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
        if(joueurCourant == j1){
            joueurCourant = j2;
        }else{
            joueurCourant = j1;
        }
    }

    public List<Pion> getPionCourant() {
        if (joueurCourant().getType() == TypeJoueur.BLANC) {
            return pt.getBlancs();
        } else {
            return pt.getNoirs();
        }
    }

    public void joueCoup(Coup c){
        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            System.out.println("deplacement");
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            coupsPrecedant.push(c);
            coupsSuivant.clear();
            joueurSuivant();
            update();
        } else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getC() + "," + pion.getPosition().getL() + ") -> " + destination.getL() + "," + destination.getC());
    }

    public boolean roiSorti() {
        Point roiPos = pt.getRoi().getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        return ((roiC == 0 && roiL == 0) || (roiC == 0 && roiL == 8) || (roiC == 8 && roiL == 0) || (roiC == 8 && roiL == 8));
    }

    public boolean roiCapture() {
        Point roiPos = pt.getRoi().getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        if(roiC == 3 && roiL == 4)
            return (pt.getCases()[roiPos.getC() - 1][roiPos.getL()] == pt.NOIR && pt.getCases()[roiPos.getC()][roiPos.getL() + 1] == pt.NOIR && pt.getCases()[roiPos.getC()][roiPos.getL() - 1] == pt.NOIR);
        if(roiC == 5 && roiL == 4)
            return (pt.getCases()[roiPos.getC() + 1][roiPos.getL()] == pt.NOIR && pt.getCases()[roiPos.getC()][roiPos.getL() + 1] == pt.NOIR && pt.getCases()[roiPos.getC()][roiPos.getL() - 1] == pt.NOIR);
        if(roiC == 4 && roiL == 3)
            return (pt.getCases()[roiPos.getC() + 1][roiPos.getL()] == pt.NOIR && pt.getCases()[roiPos.getC() - 1][roiPos.getL()] == pt.NOIR && pt.getCases()[roiPos.getC()][roiPos.getL() - 1] == pt.NOIR);
        if(roiC == 4 && roiL == 5)
            return (pt.getCases()[roiPos.getC() + 1][roiPos.getL()] == pt.NOIR && pt.getCases()[roiPos.getC() - 1][roiPos.getL()] == pt.NOIR && pt.getCases()[roiPos.getC()][roiPos.getL() + 1] == pt.NOIR);
        try {
            return (pt.getCases()[roiPos.getC() + 1][roiPos.getL()] == pt.NOIR && pt.getCases()[roiPos.getC() - 1][roiPos.getL()] == pt.NOIR && pt.getCases()[roiPos.getC()][roiPos.getL() + 1] == pt.NOIR && pt.getCases()[roiPos.getC()][roiPos.getL() - 1] == pt.NOIR);
        } catch (Exception e){
            return false;
        }
    }

    public boolean pionCapture(Pion pion){
        Point posPion = pion.getPosition();
        int pionC = posPion.getC();
        int pionL = posPion.getL();
//
        if(pion.getType() == TypePion.BLANC){
            try {
                if(pt.getCases()[pionC - 1][pionL] == pt.NOIR && pt.getCases()[pionC + 1][pionL] == pt.NOIR)
                    return true;
            } catch (Exception e){
                return false;
            }
            try {
                if(pt.getCases()[pionC][pionL - 1] == pt.NOIR && pt.getCases()[pionC][pionL + 1] == pt.NOIR)
                    return true;
            } catch (Exception e){
                return false;
            }
        } else if(pion.getType() == TypePion.NOIR){
            try {
                if((pt.getCases()[pionC - 1][pionL] == pt.BLANC || pt.getCases()[pionC - 1][pionL] == pt.ROI) && (pt.getCases()[pionC + 1][pionL] == pt.BLANC || pt.getCases()[pionC + 1][pionL] == pt.ROI))
                    return true;
            } catch (Exception e){
                return false;
            }
            try {
                if((pt.getCases()[pionC][pionL - 1] == pt.BLANC || pt.getCases()[pionC][pionL - 1] == pt.ROI) && (pt.getCases()[pionC][pionL + 1] == pt.BLANC || pt.getCases()[pionC][pionL + 1] == pt.ROI))
                    return true;
            } catch (Exception e){
                return false;
            }
        }
        return false;
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
        Pion tmp = pt.trouverPion(point, joueurCourant);
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
        if (deMemeCouleur(point))
            setSelectionner(point);
    }

    private boolean deMemeCouleur(Point point) {
        return (pt.getPion(point) == TypePion.BLANC && joueurCourant.getType() == TypeJoueur.BLANC)
                || (pt.getPion(point) == TypePion.ROI && joueurCourant.getType() == TypeJoueur.BLANC)
                || (pt.getPion(point) == TypePion.NOIR && joueurCourant.getType() == TypeJoueur.NOIR);
    }

    public boolean verifierCoup(Point point) {
        if (pionSelect == null) return false;

        List<Point> accessible = pt.getCasesAccessibles(pionSelect);
        System.out.println(accessible.contains(point));
        if (accessible.contains(point)) {
            Coup coup = new Coup(pionSelect, point);
            joueCoup(coup);
            coupsPrecedant.add(coup);
            pionSelect = null;
            return true;
        }
        return false;
    }
}

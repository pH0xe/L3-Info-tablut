package modele;

import global.Configuration;

import java.util.List;

public class Jeu {
    private Joueur joueurCourant;
    private final Joueur j1;
    private final Joueur j2;
    Plateau pt;

    public Jeu(Joueur j1, Joueur j2){
        this.j1 = j1;
        this.j2 = j2;
        this.joueurCourant = j1; // j1 = blancs
        pt = new Plateau();
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
            return pt.blancs;
        } else {
            return pt.noirs;
        }
    }

    public void joueCoup(Coup c){
        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination.getL(), destination.getC()))
            pt.deplacerPion(pion, destination.getL(), destination.getC());
        else
//        if(!(pt.peutDeplacer(pion, destination.getL(), destination.getC()) && pt.deplacerPion(pion, destination.getL(), destination.getC()))){
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getC() + "," + pion.getPosition().getL() + ") -> " + destination.getL() + "," + destination.getC());
    }

    public boolean roiSorti() {
        Point roiPos = pt.roi.getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        return (roiC == 0 && roiL == 0 || roiC == 0 && roiL == 8 || roiC == 8 && roiL == 0 || roiC == 8 && roiL == 8);
    }

    public boolean roiCapture() {
        Point roiPos = pt.roi.getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        //if(roiC == 3 && roiL == 4 || roiC == 5 && roiL == 4 || roiC == 4 && roiL == 3 || roiC == 4 && roiL == 5)
        if(roiC == 3 && roiL == 4)
            return (pt.cases[roiPos.getC() - 1][roiPos.getL()] == pt.NOIR && pt.cases[roiPos.getC()][roiPos.getL() + 1] == pt.NOIR && pt.cases[roiPos.getC()][roiPos.getL() - 1] == pt.NOIR);
        if(roiC == 5 && roiL == 4)
            return (pt.cases[roiPos.getC() + 1][roiPos.getL()] == pt.NOIR && pt.cases[roiPos.getC()][roiPos.getL() + 1] == pt.NOIR && pt.cases[roiPos.getC()][roiPos.getL() - 1] == pt.NOIR);
        if(roiC == 4 && roiL == 3)
            return (pt.cases[roiPos.getC() + 1][roiPos.getL()] == pt.NOIR && pt.cases[roiPos.getC() - 1][roiPos.getL()] == pt.NOIR && pt.cases[roiPos.getC()][roiPos.getL() - 1] == pt.NOIR);
        if(roiC == 4 && roiL == 5)
            return (pt.cases[roiPos.getC() + 1][roiPos.getL()] == pt.NOIR && pt.cases[roiPos.getC() - 1][roiPos.getL()] == pt.NOIR && pt.cases[roiPos.getC()][roiPos.getL() + 1] == pt.NOIR);
        try {
            return (pt.cases[roiPos.getC() + 1][roiPos.getL()] == pt.NOIR && pt.cases[roiPos.getC() - 1][roiPos.getL()] == pt.NOIR && pt.cases[roiPos.getC()][roiPos.getL() + 1] == pt.NOIR && pt.cases[roiPos.getC()][roiPos.getL() - 1] == pt.NOIR);
        } catch (Exception e){
            return false;
        }
    }

    public boolean pionCapture(Pion pion){
        Point posPion = pion.getPosition();
        int pionC = posPion.getC();
        int pionL = posPion.getL();
//        try{
//            if(pion.getType() == TypePion.BLANC){
//                if((pt.cases[pionC - 1][pionL] == pt.NOIR && pt.cases[pionC + 1][pionL] == pt.NOIR) || (pt.cases[pionC][pionL - 1] == pt.NOIR && pt.cases[pionC][pionL + 1] == pt.NOIR))
//                    return true;
//            } else if(pion.getType() == TypePion.NOIR){
//                if(((pt.cases[pionC - 1][pionL] == pt.BLANC || pt.cases[pionC - 1][pionL] == pt.ROI) && (pt.cases[pionC + 1][pionL] == pt.BLANC || pt.cases[pionC + 1][pionL] == pt.ROI)) || ((pt.cases[pionC][pionL - 1] == pt.BLANC || pt.cases[pionC][pionL - 1] == pt.ROI) && (pt.cases[pionC][pionL + 1] == pt.BLANC || pt.cases[pionC][pionL + 1] == pt.ROI)))
//                    return true;
//            }
//        } catch (Exception e){
//            return false;
//        }
        if(pion.getType() == TypePion.BLANC){
            try {
                if(pt.cases[pionC - 1][pionL] == pt.NOIR && pt.cases[pionC + 1][pionL] == pt.NOIR)
                    return true;
            } catch (Exception e){
                return false;
            }
            try {
                if(pt.cases[pionC][pionL - 1] == pt.NOIR && pt.cases[pionC][pionL + 1] == pt.NOIR)
                    return true;
            } catch (Exception e){
                return false;
            }
        } else if(pion.getType() == TypePion.NOIR){
            try {
                if((pt.cases[pionC - 1][pionL] == pt.BLANC || pt.cases[pionC - 1][pionL] == pt.ROI) && (pt.cases[pionC + 1][pionL] == pt.BLANC || pt.cases[pionC + 1][pionL] == pt.ROI))
                    return true;
            } catch (Exception e){
                return false;
            }
            try {
                if((pt.cases[pionC][pionL - 1] == pt.BLANC || pt.cases[pionC][pionL - 1] == pt.ROI) && (pt.cases[pionC][pionL + 1] == pt.BLANC || pt.cases[pionC][pionL + 1] == pt.ROI))
                    return true;
            } catch (Exception e){
                return false;
            }
        }

        return false;
    }
}

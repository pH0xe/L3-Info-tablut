package modele;

import global.Configuration;

import java.util.List;

public class Jeu {
    private Joueur joueurCourant;
    private final Joueur j1;
    private final Joueur j2;
    private Plateau pt;

    public Jeu(Joueur j1, Joueur j2){
        this.j1 = j1;
        this.j2 = j2;
        this.joueurCourant = j1; // j1 = blancs
        pt = new Plateau();
    }

    public Jeu(Jeu j){
        this.j1 = j.j1;
        this.j2 = j.j2;
        this.joueurCourant = j.j1; // j1 = blancs
        this.pt = j.pt;
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
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getL() + "," + pion.getPosition().getC() + ") -> " + destination.getL() + "," + destination.getC());
        this.joueurSuivant();
    }

    public Jeu joueCoupDuplique(Coup c){
        Jeu jeu2 = new Jeu(this);
        Pion pion = c.getPion();
        Point destination = c.getDestination();
        Plateau plat = jeu2.getPlateau();
        if(plat.peutDeplacer(pion, destination.getL(), destination.getC()))
            plat.deplacerPion(pion, destination.getL(), destination.getC());
        else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getL() + "," + pion.getPosition().getC() + ") -> " + destination.getL() + "," + destination.getC());
        jeu2.joueurSuivant();
        return jeu2;
    }

    public void annulerCoup(List<Coup> c, int destL, int destC){
        Coup dernier = c.get(c.size()-1);
        //System.out.println("Dernier coup " + dernier);
        c.remove(c.size()-1);
        Point dest = new Point(destL, destC);
        Pion p = dernier.getPion();
        Coup cp = new Coup(p, dest);
        //System.out.println("Coup d'annulation: " + cp);
        this.joueCoup(cp);
    }

    public boolean roiSorti() {
        Point roiPos = pt.roi.getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        return ((roiC == 0 || roiL == 0 || roiL == 8 || roiC == 8));
    }

    public boolean roiCapture() {
        Point roiPos = pt.roi.getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        if(roiL == 3 && roiC == 4)
            return (pt.cases[roiPos.getL() - 1][roiPos.getC()] == pt.NOIR && pt.cases[roiPos.getL()][roiPos.getC() + 1] == pt.NOIR && pt.cases[roiPos.getL()][roiPos.getC() - 1] == pt.NOIR);
        if(roiL == 5 && roiC == 4)
            return (pt.cases[roiPos.getL() + 1][roiPos.getC()] == pt.NOIR && pt.cases[roiPos.getL()][roiPos.getC() + 1] == pt.NOIR && pt.cases[roiPos.getL()][roiPos.getC() - 1] == pt.NOIR);
        if(roiL == 4 && roiC == 3)
            return (pt.cases[roiPos.getL() + 1][roiPos.getC()] == pt.NOIR && pt.cases[roiPos.getL() - 1][roiPos.getC()] == pt.NOIR && pt.cases[roiPos.getL()][roiPos.getC() - 1] == pt.NOIR);
        if(roiL == 4 && roiC == 5)
            return (pt.cases[roiPos.getL() + 1][roiPos.getC()] == pt.NOIR && pt.cases[roiPos.getL() - 1][roiPos.getC()] == pt.NOIR && pt.cases[roiPos.getL()][roiPos.getC() + 1] == pt.NOIR);
        try {
            return (pt.cases[roiPos.getL() + 1][roiPos.getC()] == pt.NOIR && pt.cases[roiPos.getL() - 1][roiPos.getC()] == pt.NOIR && pt.cases[roiPos.getL()][roiPos.getC() + 1] == pt.NOIR && pt.cases[roiPos.getL()][roiPos.getC() - 1] == pt.NOIR);
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
                if(pt.cases[pionL - 1][pionC] == pt.NOIR && pt.cases[pionL + 1][pionC] == pt.NOIR)
                    return true;
            } catch (Exception e){
                return false;
            }
            try {
                if(pt.cases[pionL][pionC - 1] == pt.NOIR && pt.cases[pionL][pionC + 1] == pt.NOIR)
                    return true;
            } catch (Exception e){
                return false;
            }
        } else if(pion.getType() == TypePion.NOIR){
            try {
                if((pt.cases[pionL - 1][pionC] == pt.BLANC || pt.cases[pionL - 1][pionC] == pt.ROI) && (pt.cases[pionL + 1][pionC] == pt.BLANC || pt.cases[pionL + 1][pionC] == pt.ROI))
                    return true;
            } catch (Exception e){
                return false;
            }
            try {
                if((pt.cases[pionL][pionC - 1] == pt.BLANC || pt.cases[pionL][pionC - 1] == pt.ROI) && (pt.cases[pionL][pionC + 1] == pt.BLANC || pt.cases[pionL][pionC + 1] == pt.ROI))
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
}

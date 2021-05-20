package controleur.IA;

import global.Configuration;
import modele.*;

import java.util.*;

public class IADifficile extends IA{
    public final int MAX = 100000;
    public final int MIN = -100000;

    Map<ConfigJeu, List<Coup>> returnVal;

    public IADifficile(){
        returnVal = new HashMap<>();
    }

    public int heuristique(Jeu j){
        Plateau p = j.getPlateau();
        Pion roi = p.getRoi();
        if(j.roiSorti()){
            System.out.println("Roi Sorti");
            return MAX;
        }
        else if(j.roiCapture()){
            System.out.println("Roi Capture");
            return MIN;
        }
        int autourRoiNoir = 0;
        int heuristique = 0;

        int lRoi = roi.getPosition().getL();
        int cRoi = roi.getPosition().getC();


        if(( lRoi+1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi+1, cRoi, TypePion.NOIR)){
            autourRoiNoir++;
        }

        if(( lRoi-1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi-1, cRoi, TypePion.NOIR)){
            autourRoiNoir++;
        }

        if(( lRoi == 4 && cRoi+1 == 4) || p.estCaseDeType(lRoi, cRoi+1, TypePion.NOIR)){
            autourRoiNoir++;
        }

        if(( lRoi == 4 && cRoi-1 == 4) || p.estCaseDeType(lRoi, cRoi-1, TypePion.NOIR)){
            autourRoiNoir++;
        }

        heuristique -= 5*autourRoiNoir;

        /****FIN ENCERCLEMENT ***/

        heuristique += 8*p.getBlancs().size();
        heuristique -= 4*p.getNoirs().size();

        heuristique += 7 * j.getPlateau().getCasesAccessibles(roi).size();

        heuristique += 32 * j.getPlateau().getSortiesAccessibles();
        return heuristique;
    }

    //Configuration.instance.logger

    public int Minimax(Jeu j, Couleur tj, int profondeur, List<Coup> prec, int alpha, int beta){

        if(profondeur==0 || j.roiSorti() || j.roiCapture()){
            return heuristique(j);
        }
        Plateau p = j.getPlateau();
        List<Coup> C = new ArrayList<>();
        List<Pion> jouables = j.getPionsCourant();
        for (Pion pi: jouables) {
            List<Point> accessibles = p.getCasesAccessibles(pi);
            for (Point pt: accessibles) {
                C.add(new Coup(pi, pt));
            }
        }

        int val;
        if (tj.equals(Couleur.BLANC)){
            val = MIN;
        }
        else{
            val = MAX;
        }
        int borne;
        List<Coup> coups = new ArrayList<>();
        Coup meilleur = null;
        for (Coup cp: C) {
            prec.add(cp);
            int dL = cp.getPion().getPosition().getL();
            int dC = cp.getPion().getPosition().getC();
            if (tj.equals(Couleur.BLANC)){
                borne =  Minimax(j.joueCoupDuplique(cp),Couleur.NOIR, profondeur-1, prec, alpha, beta);
                if(borne > val){
                    val = borne;
                    meilleur = cp;
                    coups.clear();
                }else if(borne == val){
                    coups.add(cp);
                }
                alpha = max(alpha, val);
                if(alpha>=beta){
                    j.annulerCoup(prec, dL, dC);
                    break;
                }
            }
            else{
                borne =  Minimax(j.joueCoupDuplique(cp),Couleur.BLANC, profondeur-1, prec, alpha, beta);
                if(borne < val){
                    val = borne;
                    meilleur = cp;
                    coups.clear();
                }else if(borne == val){
                    coups.add(cp);

                }
                beta = min(beta, val);
                if (beta<=alpha){
                    j.annulerCoup(prec, dL, dC);
                    break;
                }
            }
            j.annulerCoup(prec, dL, dC);
        }
        if(meilleur == null){
            Configuration.instance().logger().severe("Pas de coups jouables");
        }else{
            coups.add(meilleur);
            ConfigJeu cj = new ConfigJeu(j.joueurCourant(), j.getPlateau());
            returnVal.put(cj,coups);
        }
        return val;
    }


    public Coup iaJoue(Jeu j){
        Minimax(j, j.joueurCourant().getCouleur(),4, new ArrayList<>(),MIN, MAX);
        Plateau p = j.getPlateau();
        Random r = new Random();
        ConfigJeu cj = new ConfigJeu(j.joueurCourant(), p);
        List<Coup> cps = returnVal.get(cj);
        return cps.get(r.nextInt(cps.size()));
    }

    public int max(int a, int b){
        if(a>b)
            return a;
        return b;
    }

    public int min(int a, int b){
        if(a<b)
            return a;
        return b;
    }
}

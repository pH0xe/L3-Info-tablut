package controleur.IA;

import global.Configuration;
import modele.*;

import java.util.*;

public class IADifficile extends IA{
    Map<ConfigJeu, List<Coup>> returnVal;

    public IADifficile(){
        returnVal = new HashMap<>();
    }

    public int heuristique(Jeu j){
        Plateau p = j.getPlateau();
        Pion roi = p.getRoi();
        //System.out.println("Roi : " + roi);
        if(j.roiSorti()){
            System.out.println("Roi Sorti");
            return 100000;
        }
        else if(j.roiCapture()){
            System.out.println("Roi Capture");
            return -100000;
        }
        int autourRoi = 0;
        int heuristique = 0;

        int lRoi = roi.getPosition().getL();
        int cRoi = roi.getPosition().getC();


        if(( lRoi+1 == 4 && cRoi == 4) || p.getCases()[lRoi+1][cRoi] == TypePion.NOIR){
            autourRoi++;
        }

        if(( lRoi-1 == 4 && cRoi == 4) || p.getCases()[lRoi-1][cRoi] == TypePion.NOIR){
            autourRoi++;
        }

        if(( lRoi == 4 && cRoi+1 == 4) || p.getCases()[lRoi][cRoi+1] == TypePion.NOIR){
            autourRoi++;
        }

        if(( lRoi == 4 && cRoi-1 == 4) || p.getCases()[lRoi][cRoi-1] == TypePion.NOIR){
            autourRoi++;
        }

        heuristique -= 5*autourRoi;

        /****FIN ENCERCLEMENT ***/

        heuristique += 8*p.getBlancs().size();
        heuristique -= 16*p.getNoirs().size();

        heuristique += 32 * j.getPlateau().getCasesAccessibles(roi).size();

        return heuristique;
          }

    //Configuration.instance.logger

    public int Minimax(Jeu j, Couleur tj, int profondeur, List<Coup> prec){

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
        Configuration.instance().logger().warning("Liste coups: ");
        for(Coup cp:C){
            if(cp.getPion().getType().equals(TypePion.ROI)){
                Configuration.instance().logger().warning(cp.toString());
            }
        }
        int val;
        if (tj.equals(Couleur.BLANC)){
            val = -100000;
        }
        else{
            val = 100000;
        }
        int borne;
        List<Coup> coups = new ArrayList<>();
        Coup meilleur = null;
        for (Coup cp: C) {
            prec.add(cp);
            int dL = cp.getPion().getPosition().getL();
            int dC = cp.getPion().getPosition().getC();
            if (tj.equals(Couleur.BLANC)){
                borne =  Minimax(j.joueCoupDuplique(cp),Couleur.NOIR, profondeur-1, prec);
                if(borne > val){
                    val = borne;
                    meilleur = cp;
                    coups.clear();
                }else if(borne == val){
                    coups.add(cp);
                }
            }
            else{
                borne =  Minimax(j.joueCoupDuplique(cp),Couleur.BLANC, profondeur-1, prec);
                if(borne < val){
                    val = borne;
                    meilleur = cp;
                    coups.clear();
                }else if(borne == val){
                    coups.add(cp);
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
        Plateau p = j.getPlateau();
        Random r = new Random();
        ConfigJeu cj = new ConfigJeu(j.joueurCourant(), p);
        List<Coup> cps = returnVal.get(cj);
        return cps.get(r.nextInt(cps.size()));
    }
}

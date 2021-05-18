package controleur.IA;

import java.util.ArrayList;
import java.util.List;

public class IADifficile {
    public int heuristique(Plateau p){
        if(p.roiSorti()){
            return -1000;
        }
        else if(p.roiCapture()){
            return 1000;
        }
        int autourRoi = 0;
        int heuristique = 0;
        Pion roi = p.getRoi();
        int lRoi = roi.getPosition().getL();
        int cRoi = roi.getPosition().getC();


        if(( lRoi+1 == 4 && cRoi == 4) || p.getCases()[lRoi+1][cRoi] == NOIR){
            autourRoi++;
        }

        if(( lRoi-1 == 4 && cRoi == 4) || p.getCases()[lRoi-1][cRoi] == NOIR){
            autourRoi++;
        }

        if(( lRoi == 4 && cRoi+1 == 4) || p.getCases()[lRoi][cRoi+1] == NOIR){
            autourRoi++;
        }

        if(( lRoi == 4 && cRoi-1 == 4) || p.getCases()[lRoi][cRoi-1] == NOIR){
            autourRoi++;
        }

        heuristique += 5*autourRoi;

        /****FIN ENCERCLEMENT ***/

        heuristique -= 8*p.getBlancs().size();
        heuristique += 4*p.getNoirs().size();


        return heuristique;
          }

    //Configuration.instance.logger

    public int Minimax(Plateau p, TypeJoueur j, int profondeur){
        if(profondeur==0 || p.roiSorti() || p.roiCapture()){
            return heuristique(p);
        }
        List<Coup> C = new ArrayList<>();
        List<Pion> jouables = p.getPionsCourant();
        for (Pion pi: jouables) {
            List<Point> accessibles = p.getCasesAccessibles(pi);
            for (Point pt: accessibles) {
                C.add(new Coup(pi, pt));
            }
        }
        int val = 0;
        if (j.equals(TypeJoueur.BLANC)){
            val = 100000;
        }
        else{
            val = -100000;
        }
        for (Coup cp: C) {
            if (j.equals(TypeJoueur.BLANC)){
                val = min(val, Minimax(p.joueCoup(cp),TypeJoueur.NOIR, profondeur-1));
            }
            else{
                val = max(val, Minimax(p.joueCoup(cp),TypeJoueur.BLANC, profondeur-1));
            }
        }
        return val;
    }


    public Point iaJoue(){
        return null;
    }
}

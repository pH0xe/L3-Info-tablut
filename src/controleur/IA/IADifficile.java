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
        return 1;
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

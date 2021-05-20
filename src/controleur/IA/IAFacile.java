package controleur.IA;

import modele.*;

import java.util.*;

public class IAFacile extends IA{
    public int heuristique(Jeu j){
        return 1;
    }
    public int Minimax(Jeu j, Couleur tj, int profondeur, List<Coup> cp, int alpha, int beta){
        return 0;
    }
    public Coup iaJoue(Jeu j){
        Plateau p = j.getPlateau();
        Random r = new Random();
        List<Pion> ps = j.getPionsCourant();
        List<Point> jouables;
        Pion aJouer;
        do {
            aJouer = ps.get(r.nextInt(ps.size()));
            jouables = p.getCasesAccessibles(aJouer);
        }while(jouables.isEmpty());
        return new Coup(aJouer, jouables.get(r.nextInt(jouables.size())));
    }
}

package controleur.IA;

import modele.*;

import java.util.*;

public class IAFacile extends IA{
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

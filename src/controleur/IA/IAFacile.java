package controleur.IA;

import java.util.*;

public class IAFacile {
    public int heuristique(Plateau p){
        return 1;
    }

    public Point iaJoue(Jeu j){
        Plateau p = j.getPlateau();
        List<Point> jouables = p.getCasesAccessibles(p.getPion());
        Random r = new Random();
        return jouables.get(r.nextInt(jouables.size()));
    }
}

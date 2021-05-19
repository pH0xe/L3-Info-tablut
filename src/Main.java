import controleur.Controleur;
import controleur.IA.IA;
import controleur.IA.IADifficile;
import controleur.IA.IAFacile;
import modele.*;
import vue.InterfaceGraphique;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //InterfaceGraphique.demarrer(new Controleur());
        Joueur j1 = new Joueur("Joueur 1", TypeJoueur.BLANC);
        Joueur j2 = new Joueur("Joueur 2", TypeJoueur.NOIR);
        Jeu j = new Jeu(j1, j2);
        IA IAF = new IAFacile();
        IA IAD = new IADifficile();
        Plateau p = j.getPlateau();
        //p.initPionsBlancsVictoire();
        p.affichePlateau();
        j.joueCoup(new Coup(p.getBlancs().get(0), new Point(2,6)));
        j.joueCoup(new Coup(p.getBlancs().get(1), new Point(3,5)));
        j.joueCoup(new Coup(p.getBlancs().get(0), new Point(3,6)));
        j.joueCoup(new Coup(p.getNoirs().get(0), new Point(0, 8)));
        System.out.println();
        p.affichePlateau();
        IAD.Minimax(j, TypeJoueur.BLANC, 4, new ArrayList<Coup>());
        Coup c = IAD.iaJoue(j);
        System.out.println(c);
        j.joueCoup(c);
        p.affichePlateau();
    }
}

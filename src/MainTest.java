import modele.Jeu;
import modele.Joueur.Couleur;
import modele.Joueur.Joueur;
import modele.Plateau;
import modele.pion.EtatPion;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Coup;
import modele.util.Point;

import java.util.ArrayList;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        Pion p1 = new Pion(TypePion.NOIR, new Point(0,0));
        Pion p2 = new Pion(p1);
        p2.changerEtat(EtatPion.INACTIF);
        System.out.println(p1);
        System.out.println(p2);
        System.out.println();

        Point po1 = new Point(0,0);
        Point po2 = new Point(po1);
        po2.setC(6);
        System.out.println(po1);
        System.out.println(po2);
        System.out.println();

        Coup cp1 = new Coup(p1, po1);
        Coup cp2 = new Coup(cp1);
        cp2.setDestination(po2);
        System.out.println(cp1);
        System.out.println(cp2);
        System.out.println();

        Plateau pt1 = new Plateau();
        Plateau pt2 = new Plateau(pt1);
        pt2.getPion(new Point(4,7)).getPosition().setL(8);
        System.out.println(pt1);
        System.out.println("---------------------------------");
        System.out.println(pt2);
        System.out.println();


        Jeu jeu1 = new Jeu(new Joueur("j1", Couleur.BLANC), new Joueur("j2", Couleur.NOIR));
        Jeu jeu2 = new Jeu(jeu1);
        jeu2.joueurSuivant();
        System.out.println(jeu1);
        System.out.println();
        System.out.println(jeu2);
        List<Coup> aaa = jeu1.getListeCoups();
        for (Coup coup : aaa) {
            System.out.println(coup);
        }
    }
}

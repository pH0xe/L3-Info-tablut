package modele;

import global.Configuration;
import structure.Observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Plateau extends Observable {
    private Pion roi;
    private List<Pion> noirs;
    private List<Pion> blancs;
    private final int nbLigne = 9;
    private final int nbColonne = 9;


    public Plateau() {
        noirs = new ArrayList<>();
        blancs = new ArrayList<>();
        initPions();
    }

    public void initPions() {
        //Creation pion blancs;
        this.blancs.add(new Pion(TypePion.BLANC, new Point(2, 4)));
        this.blancs.add(new Pion(TypePion.BLANC, new Point(3, 4)));
        this.blancs.add(new Pion(TypePion.BLANC, new Point(4, 2)));
        this.blancs.add(new Pion(TypePion.BLANC, new Point(4, 3)));
        this.blancs.add(new Pion(TypePion.BLANC, new Point(4, 5)));
        this.blancs.add(new Pion(TypePion.BLANC, new Point(4, 6)));
        this.blancs.add(new Pion(TypePion.BLANC, new Point(5, 4)));
        this.blancs.add(new Pion(TypePion.BLANC, new Point(6, 4)));

        //Creation pion noirs;
        this.noirs.add(new Pion(TypePion.NOIR, new Point(0, 3)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(0, 4)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(0, 5)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(1, 4)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(3, 0)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(3, 8)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(4, 0)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(4, 1)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(4, 7)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(4, 8)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(5, 0)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(5, 8)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(7, 4)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(8, 3)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(8, 4)));
        this.noirs.add(new Pion(TypePion.NOIR, new Point(8, 5)));

        this.roi = new Pion(TypePion.ROI, new Point(4, 4));
        this.blancs.add(this.roi);
    }


    public List<Point> getCasesAccessibles(Pion pion){
        int pl = pion.getPosition().getL();
        int pc = pion.getPosition().getC();

        List<Point> accessibles = new ArrayList<>();
        if(pl!=8) {
            for (int i = pl + 1; i < 9; i++) {
                if (estVide(i, pc))
                    accessibles.add(new Point(i, pc));
                else break;
            }
        }
        if(pl!=0) {
            for (int i = pl - 1; i >= 0; i--) {
                if (estVide(i, pc))
                    accessibles.add(new Point(i, pc));
                else break;
            }
        }
        if(pc!=8) {
            for (int i = pc + 1; i < 9; i++) {
                if (estVide(pl, i))
                    accessibles.add(new Point(pl, i));
                else break;
            }
        }
        if(pc!=0) {
            for (int i = pc - 1; i >=0; i--) {
                if (estVide(pl, i))
                    accessibles.add(new Point(pl, i));
                else break;
            }
        }
        return accessibles;
    }


    public void deplacerPion(Pion pion, int l, int c) {
        pion.deplacerPion(l, c);
    }

    public void deplacerPion(Pion pion, Point dest) {
        deplacerPion(pion, dest.getL(), dest.getC());
    }

    public boolean peutDeplacer(Pion pion, Point dest){
        List<Point> accessible = getCasesAccessibles(pion);
        return accessible.contains(dest);
    }

    public TypePion getTypePion(Point point) {
        Pion p = getPion(point);
        if (p == null) return null;
        return p.getType();
    }

    public TypePion getTypePion(int l, int c) {
        return getTypePion(new Point(l,c));
    }

    public boolean estCaseDeType(Point point, TypePion typePion) {
        return getTypePion(point) == typePion;
    }

    public boolean estCaseDeType(int l, int c, TypePion typePion) {
        return estCaseDeType(new Point(l, c), typePion);
    }

    public boolean estCaseDeCouleur(int l, int c, Couleur couleur) {
        TypePion type = getTypePion(l, c);
        if (type == null)  return false;
        return type.getCouleur() == couleur;
    }

    public void affichePlateau() {
        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbColonne; j++) {
                if (getPion(i, j) == null) {
                    System.out.print(".");
                } else if (estCaseDeType(i, j, TypePion.BLANC)) {
                    System.out.print("B");
                } else if (estCaseDeType(i, j, TypePion.ROI)) {
                    System.out.print("R");
                } else {
                    System.out.print("N");
                }
            }
            System.out.println();
        }
    }

    public List<Pion> getBlancs() {
        return blancs;
    }

    public List<Pion> getNoirs() {
        return noirs;
    }

    public Pion getRoi() {
        return roi;
    }

    public int getBlancsElimine() {
        return (int) blancs.stream().filter(Pion::estPris).count();
    }

    public int getNoirsElimine() {
        return (int) noirs.stream().filter(Pion::estPris).count();
    }

    public Pion getPion(Point point) {
        Pion res = blancs.stream().filter(pion1 -> pion1.getPosition().equals(point)).findFirst().orElse(null);
        if (res != null) return res;
        res = noirs.stream().filter(pion1 -> pion1.getPosition().equals(point)).findFirst().orElse(null);
        return res;
    }

    private Pion getPion(int i, int j) {
        return getPion(new Point(i, j));
    }

    public boolean estVide(Point point) {
        Pion p = getPion(point);
        return p == null || p.estPris();
    }

    public boolean estVide(int l, int c) {
        return estVide(new Point(l,c));

    }

    public void capturerPion(Point point, Pion pion) {
        Pion p = getPion(point);
        p.changerEtat(EtatPion.INACTIF);

        Configuration.instance().logger().info("Capture du pion : " + p);
    }
}

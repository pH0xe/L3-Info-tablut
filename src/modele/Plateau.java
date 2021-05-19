package modele;

import structure.Observable;

import java.util.ArrayList;
import java.util.List;

public class Plateau extends Observable {
    public final int nbLigne = 9;
    public final int nbColonne = 9;
    public final int VIDE = 0;
    public final int NOIR = 1;
    public final int BLANC = 2;
    public final int ROI = 3;


    private int[][] cases;
    private Pion roi;
    private List<Pion> noirs;
    private List<Pion> blancs;


    public Plateau() {
        noirs = new ArrayList<>();
        blancs = new ArrayList<>();
        initPions();
        initPlateau();

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

    public void initPlateau() {
        cases = new int[9][9];

        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbColonne; j++) {
                cases[i][j] = VIDE;
            }
        }


        //Placement roi
        cases[roi.getPosition().getL()][roi.getPosition().getL()] = ROI;


        //Placement pion blancs
        for (Pion p : blancs) {
            int l = p.getPosition().getL();
            int c = p.getPosition().getC();

            cases[l][c] = BLANC;
        }

        //Placement pion noirs
        for (Pion p : noirs) {
            int l = p.getPosition().getL();
            int c = p.getPosition().getC();

            cases[l][c] = NOIR;
        }

    }


    public List<Point> getCasesAccessibles(Pion pion){
        int pl = pion.getPosition().getL();
        int pc = pion.getPosition().getC();

        List<Point> accessibles = new ArrayList<>();
        if(pl!=8) {
            for (int i = pl + 1; i < 9; i++) {
                if (cases[i][pc] == VIDE) {
                    accessibles.add(new Point(i, pc));
                } else {
                    break;
                }
            }
        }
        if(pl!=0) {
            for (int i = pl - 1; i >= 0; i--) {
                if (cases[i][pc] == VIDE) {
                    accessibles.add(new Point(i, pc));
                } else {
                    break;
                }
            }
        }
        if(pc!=8) {
            for (int i = pc + 1; i < 9; i++) {
                if (cases[pl][i] == VIDE) {
                    accessibles.add(new Point(pl, i));
                } else {
                    break;
                }
            }
        }
        if(pc!=0) {
            for (int i = pc - 1; i >=0; i--) {
                if (cases[pl][i] == VIDE) {
                    accessibles.add(new Point(pl, i));
                } else {
                    break;
                }
            }
        }
        return accessibles;
    }


    public void deplacerPion(Pion pion, int l, int c) {
        if (pion.getType() == TypePion.BLANC) {
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
            blancs.get(blancs.indexOf(pion)).getPosition().setL(l);
            blancs.get(blancs.indexOf(pion)).getPosition().setC(c);
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = BLANC;
        } else if (pion.getType() == TypePion.ROI) {
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
            blancs.get(blancs.indexOf(pion)).getPosition().setL(l);
            blancs.get(blancs.indexOf(pion)).getPosition().setC(c);
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = ROI;
        } else {
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
            noirs.get(noirs.indexOf(pion)).getPosition().setL(l);
            noirs.get(noirs.indexOf(pion)).getPosition().setC(c);
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = NOIR;
        }
    }

    public boolean peutDeplacer(Pion pion, Point dest){
        List<Point> accessible = getCasesAccessibles(pion);
        System.out.println(accessible.contains(dest));
        return accessible.contains(dest);
    }

    public TypePion getPion(Point point) {
        return getPion(point.getL(), point.getC());
    }

    public TypePion getPion(int l, int c) {
        TypePion res = null;
        if (cases[l][c] == VIDE)
            return null;
        else {
            switch (cases[l][c]) {
                case NOIR:
                    res = TypePion.NOIR;
                    break;
                case BLANC:
                    res = TypePion.BLANC;
                    break;
                case ROI:
                    res = TypePion.ROI;
                    break;
            }
            return res;
        }
    }

    public void affichePlateau() {
        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbColonne; j++) {
                if (cases[i][j] == VIDE) {
                    System.out.print(".");
                } else if (cases[i][j] == BLANC) {
                    System.out.print("B");
                } else if (cases[i][j] == ROI) {
                    System.out.print("R");
                } else {
                    System.out.print("N");
                }
            }
            System.out.println();
        }
    }

    public int[][] getCases() {
        return cases;
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

    public Pion trouverPion(Point point, Joueur j) {
        if (j.getType() == TypeJoueur.BLANC){
            for (Pion pion : blancs) {
                if (pion.getPosition().equals(point))
                    return pion;
            }
        }

        for (Pion pion : noirs) {
            if (pion.getPosition().equals(point))
                return pion;
        }
        return null;
    }
}

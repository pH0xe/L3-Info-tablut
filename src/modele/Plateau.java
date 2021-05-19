package modele;

import global.Configuration;
import structure.Observable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Plateau extends Observable {
    public final int nbLigne = 9;
    public final int nbColonne = 9;
    public final int VIDE = 0;
    public final int NOIR = 1;
    public final int BLANC = 2;
    public final int ROI = 3;


    public int[][] cases;
    public Pion roi;
    public List<Pion> noirs;
    public List<Pion> blancs;


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


    }

    public void initPlateau() {
        cases = new int[9][9];

        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbColonne; j++) {
                cases[i][j] = VIDE;
            }
        }

        this.roi = new Pion(TypePion.ROI, new Point(4, 4));
        this.blancs.add(roi);
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

    public void initPionsBlancsVictoire(){

        noirs = new ArrayList<>();
        blancs = new ArrayList<>();
        this.roi = new Pion(TypePion.ROI, new Point(2, 4));

        this.blancs.add(roi);
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

        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbColonne; j++) {
                cases[i][j] = VIDE;
            }
        }

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

        cases[roi.getPosition().getL()][roi.getPosition().getL()] = ROI;
    }

    // Un array list des cases auxquelles un pion peut se deplacer
    /*public List<Point> getCasesAccessibles(Pion pion) {
        List<Point> accessibles = new ArrayList<>();
        int pionC = pion.getPosition().getC();
        int pionL = pion.getPosition().getL();
        // Parcourir les cases de la ligne de pion
        int low = pionC - 1;
        int high = pionC + 1;
        while (low >= 0 || high < nbLigne){
            if(low >= 0)
                if(cases[pionL][low] == VIDE){
                    accessibles.add(new Point(pionL, low));
                    low--;
                } else
                    low = -1;
            if(high < nbLigne)
                if(cases[pionL][high] == VIDE){
                    accessibles.add(new Point(pionL, high));
                    high++;
                } else
                    high = nbLigne;
        }
        // Parcourir les cases de la colonne de pion
        low = pionL - 1;
        high = pionL + 1;
        while (low >= 0 || high < nbLigne){
            if(low >= 0)
                if(cases[low][pionC] == VIDE){
                    accessibles.add(new Point(low, pionC));
                    low--;
                } else
                    low = -1;
            if(high < nbLigne)
                if(cases[high][pionC] == VIDE){
                    accessibles.add(new Point(high, pionC));
                    high++;
                } else
                    high = nbLigne;
        }
        return accessibles;
    }*/

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
        } else if(pion.getType() == TypePion.NOIR){
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
            noirs.get(noirs.indexOf(pion)).getPosition().setL(l);
            noirs.get(noirs.indexOf(pion)).getPosition().setC(c);
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = NOIR;
        } else{
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
            roi.getPosition().setL(l);
            roi.getPosition().setC(c);
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = ROI;
        }
    }

    public boolean peutDeplacer(Pion pion, int l, int c){
        Point posCourant = pion.getPosition();
        boolean state = true;
        if(pion.getType() != TypePion.ROI)
            state = !((c == 0 && l == 0) || (c == 0 && l == 8) || (c == 8 && l == 0) || (c == 8 && l == 8));
        if(posCourant.getL() == l){
            if(posCourant.getC() < c) {
                for (int i = posCourant.getC()+1; i <= c; i++)
                    state &= (cases[l][i] == VIDE);
            }else{
                for (int i = posCourant.getC()-1; i >= c; i--)
                    state &= (cases[l][i] == VIDE);
            }
        } else if(posCourant.getC() == c) {
            if(posCourant.getL() < l) {
                for (int i = posCourant.getL()+1; i <= l; i++)
                    state &= (cases[i][c] == VIDE);
            }else{
                for (int i = posCourant.getL()-1; i >= l; i--)
                    state &= (cases[i][c] == VIDE);
            }
        } else {
            return false;
        }
        return state;
    }

    public TypePion getPion(int l, int c) {
        TypePion res = null;
        if (cases[l][c] == VIDE)
            return null;
        else {
            switch (cases[l][c]) {
                case NOIR -> res = TypePion.NOIR;
                case BLANC -> res = TypePion.BLANC;
                case ROI -> res = TypePion.ROI;
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

    public Pion getRoi() {
        return roi;
    }

    public int[][] getCases() {
        return cases;
    }

    public List<Pion> getNoirs() {
        return noirs;
    }

    public List<Pion> getBlancs() {
        return blancs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plateau plateau = (Plateau) o;
        return  Arrays.equals(cases, plateau.cases) && Objects.equals(roi, plateau.roi) && Objects.equals(noirs, plateau.noirs) && Objects.equals(blancs, plateau.blancs);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(nbLigne, nbColonne, VIDE, NOIR, BLANC, ROI, roi, noirs, blancs);
        result = 31 * result + Arrays.hashCode(cases);
        return result;
    }
}

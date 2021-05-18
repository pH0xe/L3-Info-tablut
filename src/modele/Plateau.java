package modele;

import global.Configuration;
import structure.Observable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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


    // La methode confond avec peutDeplacer
    // Sinon idee : un array list des cases auxquelles un pion peut se deplacer; util pour highlighting des cases
    public List<Point> getCasesAccessibles(Pion pion) {
        List<Point> accessibles = new ArrayList<>();
        int pionC = pion.getPosition().getC();
        int pionL = pion.getPosition().getL();
        // Parcourir les cases de la ligne de pion
        int low = pionC - 1;
        int high = pionC + 1;
        for(int i = 0; i < 2; i++){
            while (low >= 0 || high < nbLigne){
                if(low >= 0)
                    if(cases[low][pionL] == VIDE){
                        accessibles.add(new Point(low, pionL));
                        low--;
                    } else
                        low = -1;
                if(high < nbLigne)
                    if(cases[high][pionL] == VIDE){
                        accessibles.add(new Point(high, pionL));
                        high++;
                    } else
                        low = nbLigne;
            }
            // Parcourir les cases de la colonne de pion
            low = pionL - 1;
            high = pionL + 1;
        }
        return accessibles;
    }

    public void deplacerPion(Pion pion, int l, int c) {
//        try {
//            if (pion.getType() == TypePion.BLANC) {
//                this.blancs.remove(pion);
//                cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
//                pion.getPosition().setL(l);
//                pion.getPosition().setC(c);
//                cases[pion.getPosition().getL()][pion.getPosition().getC()] = BLANC;
//                this.blancs.add(pion);
//            } else {
//                this.noirs.remove(pion);
//                cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
//                pion.getPosition().setL(l);
//                pion.getPosition().setC(c);
//                cases[pion.getPosition().getL()][pion.getPosition().getC()] = NOIR;
//                this.noirs.add(pion);
//            }
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
        if (pion.getType() == TypePion.BLANC) {
            this.blancs.remove(pion);
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
            pion.getPosition().setL(l);
            pion.getPosition().setC(c);
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = BLANC;
            this.blancs.add(pion);
        } else {
            this.noirs.remove(pion);
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
            pion.getPosition().setL(l);
            pion.getPosition().setC(c);
            cases[pion.getPosition().getL()][pion.getPosition().getC()] = NOIR;
            this.noirs.add(pion);
        }
    }

    public boolean peutDeplacer(Pion pion, int l, int c){
        Point posCourant = pion.getPosition();
        boolean state = true;
        if(pion.getType() != TypePion.ROI)
            state = !(c == 0 && l == 0 || c == 0 && l == 8 || c == 8 && l == 0 || c == 8 && l == 8);
        if(posCourant.getL() == l){
            for(int i = posCourant.getC(); i <= c; i++)
                state &= (cases[i][l] == VIDE);
        } else if(posCourant.getC() == c) {
            for(int i = posCourant.getL(); i <= l; i++)
                state &= (cases[c][i] == VIDE);
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
}

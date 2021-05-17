package modele;

import structure.Observable;

import java.util.ArrayList;
import java.util.List;

public class Plateau extends Observable {
    public final int nbLigne =  9;
    public final int nbColonne = 9;
    public final int VIDE = 0;
    public final int NOIR = 1;
    public final int BLANC = 2;
    public final int ROI = 3;


    public int[][] cases;
    public Jeu jeu;
    public Pion roi;
    public List<Pion> noirs;
    public List<Pion> blancs;




    public Plateau(Jeu jeu){
        this.jeu = jeu;
        noirs = new ArrayList<>();
        blancs =  new ArrayList<>();
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

        public void initPlateau(){
        cases = new int[9][9];

        for(int i = 0; i< nbLigne;i++){
            for(int j = 0; j < nbColonne; j++){
                cases[i][j] = VIDE;
            }
        }

        this.roi = new Pion(TypePion.ROI, new Point(4,4));

        //Placement roi
        cases[roi.getPosition().getL()][roi.getPosition().getL()] = ROI;


        //Placement pion blancs
        for(Pion p: blancs){
            int l = p.getPosition().getL();
            int c = p.getPosition().getC();

            cases[l][c] = BLANC;
        }

        //Placement pion noirs
        for(Pion p: noirs){
            int l = p.getPosition().getL();
            int c = p.getPosition().getC();

            cases[l][c] = NOIR;
        }

    }

    public List<Pion> getPionCourant(){
        if(jeu.joueurCourant().getType() == TypeJoueur.BLANC){
            return blancs;
        }else{
            return noirs;
        }
    }

    public List<Point> getCasesAccessibles(Pion pion){
        List<Point> accessibles = new ArrayList<>();
        for(int i = 0; i < nbLigne;  i++){
            for(int j = 0; j < nbColonne; j++){
                if(j == pion.getPosition().getC() && i != pion.getPosition().getL() && cases[i][j] == VIDE){
                    accessibles.add(new Point(i, j));
                }else if(i == pion.getPosition().getL() && j != pion.getPosition().getC() && cases[i][j] == VIDE){
                    accessibles.add(new Point(i, j));
                }
            }
        }
        return accessibles;
    }

    public boolean deplacerPion(Pion pion, int l, int c){
        try{
            if (pion.getType() == TypePion.BLANC){
                this.blancs.remove(pion);
                cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
                pion.getPosition().setL(l);
                pion.getPosition().setC(c);
                cases[pion.getPosition().getL()][pion.getPosition().getC()] = BLANC;
                this.blancs.add(pion);
            }else{
                this.noirs.remove(pion);
                cases[pion.getPosition().getL()][pion.getPosition().getC()] = VIDE;
                pion.getPosition().setL(l);
                pion.getPosition().setC(c);
                cases[pion.getPosition().getL()][pion.getPosition().getC()] = NOIR;
                this.noirs.add(pion);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public void affichePlateau(){
        for(int i =0; i < nbLigne; i++){
            for(int j  = 0; j < nbColonne;j++){
                if(cases[i][j] == VIDE) {
                    System.out.print(".");
                }else if(cases[i][j] == BLANC){
                    System.out.print("B");
                }else if(cases[i][j] == ROI){
                    System.out.print("R");
                }else{
                    System.out.print("N");
                }
            }
            System.out.println();
        }
    }
}

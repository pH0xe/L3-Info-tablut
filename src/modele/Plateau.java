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
    public Pion noir1;
    public Pion noir2;
    public Pion noir3;
    public Pion noir4;
    public Pion noir5;
    public Pion noir6;
    public Pion noir7;
    public Pion noir8;
    public Pion noir9;
    public Pion noir10;
    public Pion noir11;
    public Pion noir12;
    public Pion noir13;
    public Pion noir14;
    public Pion noir15;
    public Pion noir16;


    public List<Pion> blancs;
    public Pion blanc1;
    public Pion blanc2;
    public Pion blanc3;
    public Pion blanc4;
    public Pion blanc5;
    public Pion blanc6;
    public Pion blanc7;
    public Pion blanc8;


    public Plateau(Jeu jeu){
        this.jeu = jeu;
        initPions();
        initPlateau();

    }

    public void initPions() {
        //Creation pion blancs;
        blanc1 = new Pion(TypePion.BLANC, new Point(2, 4));
        blanc2 = new Pion(TypePion.BLANC, new Point(3, 4));
        blanc3 = new Pion(TypePion.BLANC, new Point(4, 2));
        blanc4 = new Pion(TypePion.BLANC, new Point(4, 3));
        blanc5 = new Pion(TypePion.BLANC, new Point(4, 5));
        blanc6 = new Pion(TypePion.BLANC, new Point(4, 6));
        blanc7 = new Pion(TypePion.BLANC, new Point(5, 4));
        blanc8 = new Pion(TypePion.BLANC, new Point(6, 4));

        this.blancs.add(blanc1);
        this.blancs.add(blanc2);
        this.blancs.add(blanc3);
        this.blancs.add(blanc4);
        this.blancs.add(blanc5);
        this.blancs.add(blanc6);
        this.blancs.add(blanc7);
        this.blancs.add(blanc8);


        //Creation pion noirs;

        noir1 = new Pion(TypePion.NOIR, new Point(0, 3));
        noir2 = new Pion(TypePion.NOIR, new Point(0, 4));
        noir3 = new Pion(TypePion.NOIR, new Point(0, 5));
        noir4 = new Pion(TypePion.NOIR, new Point(1, 4));
        noir5 = new Pion(TypePion.NOIR, new Point(3, 0));
        noir6 = new Pion(TypePion.NOIR, new Point(3, 8));
        noir7 = new Pion(TypePion.NOIR, new Point(4, 0));
        noir8 = new Pion(TypePion.NOIR, new Point(4, 1));
        noir9 = new Pion(TypePion.NOIR, new Point(4, 7));
        noir10 = new Pion(TypePion.NOIR, new Point(4, 8));
        noir11 = new Pion(TypePion.NOIR, new Point(5, 0));
        noir12 = new Pion(TypePion.NOIR, new Point(5, 8));
        noir13 = new Pion(TypePion.NOIR, new Point(7, 4));
        noir14 = new Pion(TypePion.NOIR, new Point(8, 3));
        noir15 = new Pion(TypePion.NOIR, new Point(8, 4));
        noir16 = new Pion(TypePion.NOIR, new Point(8, 5));

        this.noirs.add(noir1);
        this.noirs.add(noir2);
        this.noirs.add(noir3);
        this.noirs.add(noir4);
        this.noirs.add(noir5);
        this.noirs.add(noir6);
        this.noirs.add(noir7);
        this.noirs.add(noir8);
        this.noirs.add(noir9);
        this.noirs.add(noir10);
        this.noirs.add(noir11);
        this.noirs.add(noir12);
        this.noirs.add(noir13);
        this.noirs.add(noir14);
        this.noirs.add(noir15);
        this.noirs.add(noir16);

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
}

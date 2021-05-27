package modele;

import global.Configuration;
import global.reader.BoardReaderText;
import modele.pion.*;
import modele.Joueur.*;
import modele.util.Point;
import structure.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plateau extends Observable {
    public final int nbLigne = 9;
    public final int nbColonne = 9;

    private TypePion[][] cases;
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
        cases = new TypePion[9][9];

        //Placement pion blancs
        for (Pion p : blancs) {
            int l = p.getPosition().getL();
            int c = p.getPosition().getC();

            if(!p.estPris())
                cases[l][c] = p.getType();
        }

        //Placement pion noirs
        for (Pion p : noirs) {
            int l = p.getPosition().getL();
            int c = p.getPosition().getC();

            if(!p.estPris())
                cases[l][c] = p.getType();
        }

    }


    public List<Point> getCasesAccessibles(Pion pion){
        int pl = pion.getPosition().getL();
        int pc = pion.getPosition().getC();

        List<Point> accessibles = new ArrayList<>();
        if(pl!=8) {
            for (int i = pl + 1; i < 9; i++) {
                if (cases[i][pc] == null) {
                    accessibles.add(new Point(i, pc));
                } else {
                    break;
                }
            }
        }
        if(pl!=0) {
            for (int i = pl - 1; i >= 0; i--) {
                if (cases[i][pc] == null) {
                    accessibles.add(new Point(i, pc));
                } else {
                    break;
                }
            }
        }
        if(pc!=8) {
            for (int i = pc + 1; i < 9; i++) {
                if (cases[pl][i] == null) {
                    accessibles.add(new Point(pl, i));
                } else {
                    break;
                }
            }
        }
        if(pc!=0) {
            for (int i = pc - 1; i >=0; i--) {
                if (cases[pl][i] == null) {
                    accessibles.add(new Point(pl, i));
                } else {
                    break;
                }
            }
        }
        return accessibles;
    }


    public void deplacerPion(Pion pion, int l, int c) {
        cases[pion.getPosition().getL()][pion.getPosition().getC()] = null;
        if (pion.getType().getCouleur() == Couleur.BLANC) {
            blancs.get(blancs.indexOf(pion)).deplacerPion(l, c);
        } else {
            noirs.get(noirs.indexOf(pion)).deplacerPion(l, c);
        }
        cases[pion.getPosition().getL()][pion.getPosition().getC()] = pion.getType();
    }

    public boolean peutDeplacer(Pion pion, Point dest){
        List<Point> accessible = getCasesAccessibles(pion);
        return accessible.contains(dest);
    }

    public TypePion getTypePion(Point point) {
        return getTypePion(point.getL(), point.getC());
    }

    public TypePion getTypePion(int l, int c) {
        return cases[l][c];
    }

    public boolean estCaseDeType(Point point, TypePion typePion) {
        return cases[point.getL()][point.getC()] == typePion;
    }

    public boolean estCaseDeType(int l, int c, TypePion typePion) {
        return cases[l][c] == typePion;
    }

    public boolean estCaseDeCouleur(int l, int c, Couleur couleur) {
        TypePion type = cases[l][c];
        if (type == null)  return false;
        return type.getCouleur() == couleur;
    }

    public void affichePlateau() {
        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbColonne; j++) {
                if (cases[i][j] == null) {
                    System.out.print(".");
                } else if (cases[i][j] == TypePion.BLANC) {
                    System.out.print("B");
                } else if (cases[i][j] == TypePion.ROI) {
                    System.out.print("R");
                } else {
                    System.out.print("N");
                }
            }
            System.out.println();
        }
    }

    public TypePion[][] getCases() {
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

    public Pion trouverPion(Point point, Couleur c) {
        if (c == Couleur.BLANC){
            for (Pion pion : blancs) {
                if (pion.getPosition().equals(point)) {
                    if (pion.estPris()) return null;
                    return pion;
                }
            }
        }

        for (Pion pion : noirs) {
            if (pion.getPosition().equals(point)) {
                if (pion.estPris()) return null;
                return pion;
            }
        }
        return null;
    }

    public void capturerPion(Point point, Pion pion) {
        Pion p = trouverPion(point, pion.getCouleur().getOppose());
        cases[point.getL()][point.getC()] = null;
        p.changerEtat(EtatPion.INACTIF);

        Configuration.instance().logger().info("Capture du pion : " + p);
    }

    public boolean estTrone(Point point){ return point.getL() == 4 && point.getC() == 4; }

    public void setPlateau(BoardReaderText br){
        List<Pion> blancs = new ArrayList<>();
        List<Pion> noirs = new ArrayList<>();
        Random rn = new Random();
        blancs.addAll(br.getBlancs());
        noirs.addAll(br.getNoirs());
        System.out.println("Blancs : " + blancs.size() + ", Noirs : " + noirs.size());
        for(int i = blancs.size(); i < 9; i++){
            Pion pion = new Pion(TypePion.BLANC, new Point(rn.nextInt(8), rn.nextInt(8)));
            pion.changerEtat(EtatPion.INACTIF);
            blancs.add(pion);
        }
        for(int i = noirs.size(); i < 16; i++){
            Pion pion = new Pion(TypePion.NOIR, new Point(rn.nextInt(8), rn.nextInt(8)));
            pion.changerEtat(EtatPion.INACTIF);
            noirs.add(pion);
        }
        this.blancs = blancs;
        this.noirs = noirs;
        this.roi = br.getRoi();
        initPlateau();
    }
}
package modele;

import global.Configuration;
import global.reader.BoardReader;
import global.reader.BoardReaderText;
import modele.Joueur.Couleur;
import modele.pion.EtatPion;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Point;
import structure.Observable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Plateau extends Observable {
    private Pion roi;
    private List<Pion> noirs;
    private List<Pion> blancs;
    private final int nbLigne = 9;
    private final int nbColonne = 9;


    public Plateau() {
        noirs = new ArrayList<>();
        blancs = new ArrayList<>();
        initDefaultPions();
    }

    public Plateau(BoardReader reader) {
        noirs = new ArrayList<>();
        blancs = new ArrayList<>();
        initReaderPions(reader);
    }

    private void initReaderPions(BoardReader reader) {
        this.blancs.addAll(reader.getBlancs());
        this.noirs.addAll(reader.getNoirs());

        this.roi = reader.getRoi();
        this.blancs.add(this.roi);
    }

    public void initDefaultPions() {
        String board = Configuration.instance().getConfig("defaultBoard");
        InputStream in = Configuration.charger(board);
        BoardReader reader = new BoardReaderText(in);
        reader.lirePlateau();
        initReaderPions(reader);
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
        if (res != null) {
            if (res.estPris()) return null;
            return res;
        }
        res = noirs.stream().filter(pion1 -> pion1.getPosition().equals(point)).findFirst().orElse(null);
        if (res != null) {
            if (res.estPris()) return null;
            return res;
        }
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
        if (p.getType() == TypePion.ROI) return;
        p.changerEtat(EtatPion.INACTIF);
        p.deplacerPion(-1, -1);

        Configuration.instance().logger().info("Capture du pion : " + p);
    }
}

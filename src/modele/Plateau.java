package modele;

import global.Configuration;
import global.Operateur;
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
import java.util.stream.Collectors;
import java.util.Objects;

public class Plateau extends Observable {
    private Pion roi;
    private List<Pion> pions;
    private final int nbLigne = 9;
    private final int nbColonne = 9;

    /**
     * Constructeur par defaut, créé le plateau a partir du fichier indiqué dans la configuration
     */
    public Plateau() {
        pions = new ArrayList<>();
        initDefaultPions();
    }

    /**
     * Constructeur utilisé lors du chargment d'une sauvegarde.
     * @param reader l'object contenant les information lu dans la sauvegarde.
     */
    public Plateau(BoardReader reader) {
        pions = new ArrayList<>();
        initReaderPions(reader);
    }

    /**
     * Utilise les informations lu dans le {@link BoardReader} pour initialiser les pions.
     * @param reader le BoardReader contenant les information de jeu
     */
    private void initReaderPions(BoardReader reader) {
        this.pions.addAll(reader.getBlancs());
        this.pions.addAll(reader.getNoirs());

        this.roi = reader.getRoi();
        this.pions.add(this.roi);
    }

    /**
     * Créé le {@link BoardReader} contenant les information de la configuration par defaut puis iitialise les pions avec {@link #initReaderPions(BoardReader)}
     */
    public void initDefaultPions() {
        String board = Configuration.instance().getConfig("defaultBoard");
        InputStream in = Configuration.charger(board);
        BoardReader reader = new BoardReaderText(in);
        reader.lirePlateau();
        initReaderPions(reader);
    }

    /**
     * Calcul toute les cases accessibles pour un pion donné. <br>
     * Regarde dans les 4 directions autours du pions. Tant que la case est vide cette case est accessible. Des qu'il rencontre une case occupé arrete la recherche. <br>
     * Saute le trone. Le trone peut etre traversé mais seul le roi peut si arreter. <br>
     * @param pion le pion depuis lequel on souhaite obtenir les cases accessibles.
     * @return la liste des coordonné accesibles par le pion
     */
    public List<Point> getCasesAccessibles(Pion pion){
        int pl = pion.getPosition().getL();
        int pc = pion.getPosition().getC();
        Operateur[][] op = {
                {Operateur.ADD, Operateur.NOTHING},
                {Operateur.SUB, Operateur.NOTHING},
                {Operateur.NOTHING, Operateur.ADD},
                {Operateur.NOTHING, Operateur.SUB}
        };
        List<Point> accessibles = new ArrayList<>();

        for (Operateur[] operateurs : op) {
            int l = operateurs[0].faire(pl, 1);
            int c = operateurs[1].faire(pc, 1);
            while (coordValide(l, c)) {
                if (testTrone(pion, l, c)) {
                    if (estVide(l, c))
                        accessibles.add(new Point(l, c));
                    else break;
                }
                l = operateurs[0].faire(l, 1);
                c = operateurs[1].faire(c, 1);
            }
        }

        return accessibles;
    }

    private boolean coordValide(int l, int c) {
        return l >= 0 && l <= 8 && c >= 0 && c <= 8;
    }

    private boolean testTrone(Pion pion, int l, int c) {
        if (pion.estRoi()) return true;
        return l != 4 || c != 4;
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

    public List<Pion> getBlancs() {
        return pions.stream().filter(Pion::estBlanc).filter(pion -> !pion.estPris()).collect(Collectors.toList());
    }

    public List<Pion> getNoirs() {
        return pions.stream().filter(Pion::estNoir).filter(pion -> !pion.estPris()).collect(Collectors.toList());
    }

    public Pion getRoi() {
        return roi;
    }

    public int getBlancsElimine() {
        return (int) getBlancs().stream().filter(Pion::estPris).count();
    }

    public int getNoirsElimine() {
        return (int) getNoirs().stream().filter(Pion::estPris).count();
    }

    public Pion getPion(Point point) {
        Pion res = pions.stream().filter(pion1 -> pion1.getPosition().equals(point)).findFirst().orElse(null);
        if (res != null) {
            if (res.estPris()) return null;
            return res;
        }
        return null;
    }

    private Pion getPion(int i, int j) {
        return getPion(new Point(i, j));
    }

    public Pion getPion(Pion pion) {
        return pions.stream().filter(pion1 -> pion1.equals(pion)).findFirst().orElse(null);
    }

    public boolean estVide(Point point) {
        Pion p = getPion(point);
        return p == null || p.estPris();
    }

    public boolean estVide(int l, int c) {
        return estVide(new Point(l,c));

    }

    public Pion capturerPion(Point point) {
        Pion p = getPion(point);
        if(p.getType() == TypePion.ROI) return null;
        pions.remove(p);
        p.changerEtat(EtatPion.INACTIF);
        Configuration.instance().logger().info("Capture du pion : " + p);
        return p;
    }

    public int getSortiesAccessibles() {
        return estSortieAccessibleColonne()+estSortieAccessibleLigne();

    }

    public int estSortieAccessibleLigne(){
        int i = 0;
        int res = 0;
        int roiL = getRoi().getPosition().getL();
        int roiC = getRoi().getPosition().getC();
        while(i != roiL && getPion(i, roiC) == null){
            i++;
        }
        if(i == roiL)
            res++;
        i = nbLigne;
        while(i != roiL && getPion(i, roiC) == null){
            i--;
        }
        if(i == roiL)
            res++;

        return res;


    }

    public int estSortieAccessibleColonne(){
        int i = 0;
        int res = 0;
        int roiL = getRoi().getPosition().getL();
        int roiC = getRoi().getPosition().getC();
        while(i != roiC && getPion(roiL, i) == null){
            i++;
        }
        if(i == roiC)
            res++;
        i = nbColonne;
        while(i != roiC && getPion(roiL, i) == null){
            i--;
        }
        if(i == roiC)
            res++;


        return res;
    }

    public List<Pion> getPions() {
        return pions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbColonne; j++) {
                if (getPion(i, j) == null) {
                    sb.append(".");
                } else if (estCaseDeType(i, j, TypePion.BLANC)) {
                    sb.append("B");
                } else if (estCaseDeType(i, j, TypePion.ROI)) {
                    sb.append("R");
                } else {
                    sb.append("N");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plateau plateau = (Plateau) o;
        return Objects.equals(roi, plateau.roi) && Objects.equals(pions, plateau.pions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roi, pions);
    }
}

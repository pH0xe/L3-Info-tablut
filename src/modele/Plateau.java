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
     * Constructeur de copie
     * @param plateau le plateau a copier
     */
    public Plateau(Plateau plateau) {
        pions = new ArrayList<>();
        for (Pion pion : plateau.getPions()) {
            pions.add(new Pion(pion));
        }
        roi = pions.stream().filter(Pion::estRoi).findFirst().orElse(null);
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

    /**
     * Utilisé par {@link #getCasesAccessibles(Pion)}. Permet de vérifier que les coordonné passé en parametre sont dans le plateau.
     * @param l la ligne
     * @param c la colonne
     * @return true si les coordonné sont dans le plateau, false sinon.
     */
    private boolean coordValide(int l, int c) {
        return l >= 0 && l <= 8 && c >= 0 && c <= 8;
    }

    /**
     * utilisé par {@link #getCasesAccessibles(Pion)}. Permet de vérifier si il s'agit du trone ou non.
     * @param pion le pion sur lequel on cherche les cases accessible.
     * @param l la ligne a tester
     * @param c la colonne a tester
     * @return true si il s'agit du roi ou si le coordonné sont differente du trone, false sinon.
     */
    private boolean testTrone(Pion pion, int l, int c) {
        if (pion.estRoi()) return true;
        return l != 4 || c != 4;
    }

    /**
     * Déplace le pion passé en parametre en (l, c)
     * @param pion le pion a déplacer
     * @param l la ligne de destination
     * @param c la colonne de destination
     */
    public void deplacerPion(Pion pion, int l, int c) {
        pion.deplacerPion(l, c);
    }

    /**
     * Identique a {@link #deplacerPion(Pion, int, int)}. Utilise un objet Point pour la destination du pion.
     * @param pion le pion a déplacer
     * @param dest la destination du pion
     */
    public void deplacerPion(Pion pion, Point dest) {
        deplacerPion(pion, dest.getL(), dest.getC());
    }

    /**
     * Vérifie si le pion passé en paramètre peut etre déplacé a la position dest
     * @param pion le pion a déplacer
     * @param dest le point de destination du pion
     * @return true si le pion peut etre deplacé, false sinon
     */
    public boolean peutDeplacer(Pion pion, Point dest){
        List<Point> accessible = getCasesAccessibles(pion);
        return accessible.contains(dest);
    }

    /**
     * @param point la position du pion
     * @return renvoi le type du pion a la position point, null si il n'y a pas de pion a cette position.
     */
    public TypePion getTypePion(Point point) {
        Pion p = getPion(point);
        if (p == null) return null;
        return p.getType();
    }

    /**
     * Identique a {@link #getTypePion(Point)}
     * @param l la ligne du pion
     * @param c la colonne du pion
     * @return Renvoi le type du pion a la position (l, c), null si il n'y a pas de pion a cette position.
     */
    public TypePion getTypePion(int l, int c) {
        return getTypePion(new Point(l,c));
    }

    /**
     * Vérifie si un pion est du meme type que le type passé en parametre
     * @param point la position du pion a tester
     * @param typePion le type du pion a comparer
     * @return true si le pion est du meme type que typePion, false sinon.
     */
    public boolean estCaseDeType(Point point, TypePion typePion) {
        return getTypePion(point) == typePion;
    }

    /**
     * Identique a {@link #estCaseDeType(Point, TypePion)}, utilise des coordonné l, c pluto qu'un point.
     * @param l la ligne du pion
     * @param c la colonne du pion
     * @param typePion Le type a comparer
     * @return true si le pion est du meme type que typePion, false sinon.
     */
    public boolean estCaseDeType(int l, int c, TypePion typePion) {
        return estCaseDeType(new Point(l, c), typePion);
    }

    /**
     * Permet de vérifier si le pion au coordonné (l, c) est de la meme couleur que celle passé en paramètre.
     * @param l la ligne du pion
     * @param c la colonne du pion
     * @param couleur la couleur a comparer
     * @return true si le pion a la meme couleur, false sinon.
     */
    public boolean estCaseDeCouleur(int l, int c, Couleur couleur) {
        TypePion type = getTypePion(l, c);
        if (type == null)  return false;
        return type.getCouleur() == couleur;
    }

    /**
     * Getter
     * @return la liste des pion blancs encore actif
     */
    public List<Pion> getBlancs() {
        return pions.stream().filter(Pion::estBlanc).filter(pion -> !pion.estPris()).collect(Collectors.toList());
    }

    /**
     * Getter
     * @return la liste des pions noir encore actif
     */
    public List<Pion> getNoirs() {
        return pions.stream().filter(Pion::estNoir).filter(pion -> !pion.estPris()).collect(Collectors.toList());
    }

    /**
     * Getter
     * @return le roi
     */
    public Pion getRoi() {
        return roi;
    }

    /**
     * Getter
     * @return le nombre de pion blanc eliminé
     */
    public int getBlancsElimine() {
        return 9 - getBlancs().size();
    }

    /**
     * Getter
     * @return le nombre de pion noir eliminé
     */
    public int getNoirsElimine() {
        return 16 - getNoirs().size();
    }

    /**
     * Trouve le pion au coordonnées du point et le retourne.
     * @param point les coordonné du point a chercher
     * @return le pion trouvé au coordonné. null si il n'y a pas de pion ou si il est pris.
     */
    public Pion getPion(Point point) {
        return pions.stream().filter(pion1 -> pion1.posEquals(point)).findFirst().orElse(null);
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
        return sb.substring(0, sb.toString().length()-1);
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

    public int getNbCases(Couleur c) {
        int res = 0;
        List<Pion> pi;
        if(c.equals(Couleur.BLANC)){
            pi = this.getBlancs();

        }else{
            pi = this.getNoirs();
        }

        for (Pion pio : pi) {
            if(pio.getType() != TypePion.ROI)
                res += getCasesAccessibles(pio).size();
        }
        return res;
    }
}

package modele;

import global.Configuration;
import global.Operateur;
import global.reader.BoardReaderBinary;
import modele.Joueur.Couleur;
import modele.Joueur.Joueur;
import modele.pion.EtatPion;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Coup;
import modele.util.Point;
import structure.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Jeu extends Observable {
    private Joueur joueurCourant;
    private final Joueur j1;
    private final Joueur j2;
    private Plateau pt;
    private final Stack<Coup> coupsPrecedent, coupsSuivant;
    private Pion pionSelect;
    private boolean estFini = false;

    public Jeu(Joueur j1, Joueur j2){
        this.j1 = j1;
        this.j2 = j2;
        this.joueurCourant = j1;
        pt = new Plateau();
        coupsPrecedent = new Stack<>();
        coupsSuivant = new Stack<>();
    }

    public Jeu(BoardReaderBinary br) {
        coupsPrecedent = br.getCoupsPrecedent();
        coupsSuivant = br.getCoupsSuivant();
        j2 = br.getJoueurNoir();
        j1 = br.getJoueurBlanc();
        joueurCourant = br.getJoueurCourant();
        pt = new Plateau(br);
    }

    public Joueur joueurCourant(){
        return joueurCourant;
    }

    public void joueurSuivant(){
        joueurCourant = getJoueurSuivant();
    }

    public Joueur getJoueurSuivant(){
        if(joueurCourant.equals(j1)){
            return j2;
        }else{
            return j1;
        }
    }

    public List<Pion> getPionsCourant() {
        if (joueurCourant().getCouleur() == Couleur.BLANC) {
            return pt.getBlancs();
        } else {
            return pt.getNoirs();
        }
    }

    public void joueCoup(Coup c){
        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            coupsPrecedent.push(c);
            coupsSuivant.clear();
            c.setCaptures(pionCapture(pion));
            joueurSuivant();
            update();
        } else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getL() + "," + pion.getPosition().getC() + ") -> " + destination.getL() + "," + destination.getC());
    }

    public Jeu joueCoupDuplique(Coup c){
        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            c.setCaptures(this.pionCapture(pion));
        }
        else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getL() + "," + pion.getPosition().getC() + ") -> " + destination.getL() + "," + destination.getC());
        this.joueurSuivant();
        return this;
    }

    public void annulerCoup(List<Coup> c, int destL, int destC){
        Coup dernier = c.get(c.size()-1);
        List<Pion> captures = dernier.getCaptures();

        if(!captures.isEmpty()) {
            for (Pion p : captures) {
                if(p != null) {
                    p.changerEtat(EtatPion.ACTIF);
                    pt.getPions().add(p);
                }
            }
        }
        this.getPlateau().deplacerPion(pt.getPion(dernier.getPion()), destL, destC);
        joueurSuivant();
        c.remove(c.size()-1);
    }


    public boolean roiSorti() {
        Point roiPos = pt.getRoi().getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        return (roiC == 0 || roiL == 0 || roiC == 8 || roiL == 8);
    }

    public boolean roiCapture() {
        Point roiPos = pt.getRoi().getPosition();
        int c = roiPos.getC();
        int l = roiPos.getL();
        return (checkRoi(l+1, c)
                && checkRoi(l-1, c)
                && checkRoi(l, c+1)
                && checkRoi(l, c-1));

    }

    private boolean checkRoi(int l, int c) {
        return (l == 4 && c == 4) || pt.estCaseDeType(l, c, TypePion.NOIR);
    }

    public List<Pion> pionCapture(Pion pion){
        Point posPion = pion.getPosition();
        int pionC = posPion.getC();
        int pionL = posPion.getL();
        List<Pion> captures = new ArrayList<>();

        Operateur[][] ops = {
                {Operateur.SUB,Operateur.NOTHING},
                {Operateur.ADD,Operateur.NOTHING},
                {Operateur.NOTHING,Operateur.SUB},
                {Operateur.NOTHING,Operateur.ADD}
        };

        for (Operateur[] op : ops) {
            if (checkPion(pionL, pionC, op[0], op[1], pion.getCouleur())) {
                Pion p = pt.capturerPion(new Point(op[0].faire(pionL, 1), op[1].faire(pionC, 1)));
                if (p != null)
                    captures.add(p);
            }
        }
        return captures;
    }

    private boolean checkPion(int l, int c, Operateur opL, Operateur opC, Couleur couleur) {
        if (opL.faire(l,2) > 8 || opL.faire(l,2) < 0) return false;
        if (opC.faire(c,2) > 8 || opC.faire(c,2) < 0) return false;

        return pt.estCaseDeCouleur(opL.faire(l,1), opC.faire(c,1), couleur.getOppose())
                && (pt.estCaseDeCouleur(opL.faire(l,2), opC.faire(c,2), couleur)
                || ((l == 4 && c == 4 )|| (opL.faire(l,2) == 4 && opC.faire(c,2) == 4)));
    }

    public void annulerCoup() {
        Coup c = coupsPrecedent.pop();
        for (Pion pion : c.getCaptures()) {
            if (pion == null) continue;
            pion.changerEtat(EtatPion.ACTIF);
            pt.getPions().add(pion);
        }

        Point dest = c.getOrigine();

        Pion p = pt.getPion(c.getDestination());
        pt.deplacerPion(p, dest.getL(), dest.getC());
        joueurSuivant();
        coupsSuivant.push(c);
        update();
    }

    public void refaireCoup() {
        Coup c = coupsSuivant.pop();

        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            coupsPrecedent.push(c);
            update();
        } else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getC() + "," + pion.getPosition().getL() + ") -> " + destination.getL() + "," + destination.getC());
    }

    public void setSelectionner(Point point) {
        Pion tmp = pt.getPion(point);
        if (tmp.getCouleur() != joueurCourant.getCouleur()) pionSelect = null;
        if (tmp != pionSelect)
            pionSelect = tmp;
        else
            pionSelect = null;
        update();
    }

    public Pion getSelectionne() {
        return pionSelect;
    }

    public List<Point> getClickable() {
        return pt.getCasesAccessibles(pionSelect);
    }

    public void verifierPion(Point point) {
        if (deMemeCouleur(point) && estClickable(point))
            setSelectionner(point);

    }

    private boolean estClickable(Point point) {
        Pion p = pt.getPion(point);
        return getPionClickable().contains(p);
    }

    private boolean deMemeCouleur(Point point) {
        TypePion tmp = pt.getTypePion(point);
        if (tmp == null) return false;
        return tmp.getCouleur() ==  joueurCourant.getCouleur();
    }

    public boolean verifierCoup(Point point) {
        if (pionSelect == null) return false;

        List<Point> accessible = pt.getCasesAccessibles(pionSelect);
        if (accessible.contains(point)) {
            Coup coup = new Coup(pionSelect, point, pionSelect.getPosition().getL(), pionSelect.getPosition().getC());
            joueCoup(coup);
            pionSelect = null;
            return true;
        }
        return false;
    }

    public List<Pion> getPionClickable() {
        if (estFini()) return new ArrayList<>();
        List<Pion> pions = getPionsCourant();
        return pions.stream().filter(pion -> !pt.getCasesAccessibles(pion).isEmpty()).collect(Collectors.toList());
    }

    public Plateau getPlateau() {
        return pt;
    }

    public List<Coup> getListeCoups(){
        List<Coup> C = new ArrayList<>();
        List<Pion> jouables = this.getPionsCourant();
        for (Pion pi: jouables) {
            List<Point> accessibles = pt.getCasesAccessibles(pi);
            for (Point pt: accessibles) {
                C.add(new Coup(pi, pt, pi.getPosition().getL(), pi.getPosition().getC()));
            }
        }
        return C;
    }

    public Joueur getJoueurBlanc() {
        return j1;
    }

    public Joueur getJoueurNoir() {
        return j2;
    }

    public Stack<Coup> getCoupsSuivant() {
        return coupsSuivant;
    }

    public Stack<Coup> getCoupsPrecedent() {
        return coupsPrecedent;
    }

    public boolean estFini() {
        return estFini || roiCapture() || roiSorti();
    }

    public void setEstFini(boolean b) {
        this.estFini = true;
    }
}

package modele;

import global.Configuration;
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
    private final Stack<Coup> coupsPrecedant, coupsSuivant;
    private Pion pionSelect;

    public Jeu(Joueur j1, Joueur j2){
        this.j1 = j1;
        this.j2 = j2;
        this.joueurCourant = j1; // j1 = blancs
        pt = new Plateau();
        coupsPrecedant = new Stack<>();
        coupsSuivant = new Stack<>();
    }

    public Jeu(Jeu j){
        this.j1 = new Joueur(j.j1);
        this.j2 = new Joueur(j.j2);
        if(j.joueurCourant().getCouleur() == Couleur.BLANC)
            this.joueurCourant = this.j1; // j1 = blancs
        else
            this.joueurCourant = this.j2; // j1 = blancs
        this.pt = new Plateau(j.getPlateau());
        coupsPrecedant = new Stack<>();
        coupsSuivant = new Stack<>();
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
            coupsPrecedant.push(c);
            coupsSuivant.clear();
            c.setCaptures(pionCapture(pion));
            joueurSuivant();
            update();
        }  else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getL() + "," + pion.getPosition().getC() + ") -> " + destination.getL() + "," + destination.getC());
    }

    public Jeu joueCoupDuplique(Coup c){
        Jeu jeu2 = new Jeu(this);
        Pion pion = c.getPion();
        Point destination = c.getDestination();
        Plateau plat = jeu2.getPlateau();
        if(plat.peutDeplacer(pion, destination))
            plat.deplacerPion(pion, destination.getL(), destination.getC());
        else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getL() + "," + pion.getPosition().getC() + ") -> " + destination.getL() + "," + destination.getC());
        jeu2.joueurSuivant();
        return jeu2;
    }

    public void annulerCoup(List<Coup> c, int destL, int destC){
        Coup dernier = c.get(c.size()-1);
        //System.out.println("Dernier coup " + dernier);
        c.remove(c.size()-1);
        for (Pion p: dernier.getCaptures()) {
            p.changerEtat(EtatPion.ACTIF);
        }
        this.getPlateau().deplacerPion(dernier.getPion(), destL, destC);
        joueurSuivant();
        //System.out.println("Coup d'annulation: " + cp);
        //deplacer pion
        //joueur suivant
        //reactiver pions
    }

    public boolean roiSorti() {
        Point roiPos = pt.getRoi().getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        return (roiC == 0 || roiL == 0 || roiC == 8 || roiL == 8);
    }

    public boolean roiCapture() {
        Point roiPos = pt.getRoi().getPosition();
        int roiC = roiPos.getC();
        int roiL = roiPos.getL();
        if(roiC == 3 && roiL == 4)
            return (pt.estCaseDeType(roiPos.getL(), roiPos.getC()-1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()-1, roiPos.getC(), TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()+1, roiPos.getC(), TypePion.NOIR));
        if(roiC == 5 && roiL == 4)
            return (pt.estCaseDeType(roiPos.getL(), roiPos.getC()+1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()-1, roiPos.getC(), TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()+1, roiPos.getC(), TypePion.NOIR));
        if(roiC == 4 && roiL == 3)
            return (pt.estCaseDeType(roiPos.getL(), roiPos.getC()-1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL(), roiPos.getC()+1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()-1, roiPos.getC(), TypePion.NOIR));
        if(roiC == 4 && roiL == 5)
            return (pt.estCaseDeType(roiPos.getL(), roiPos.getC()+1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL(), roiPos.getC()-1, TypePion.NOIR)
                    && pt.estCaseDeType(roiPos.getL()+1, roiPos.getC(), TypePion.NOIR));
        return (pt.estCaseDeType(roiPos.getL()+1, roiPos.getC(), TypePion.NOIR)
                && pt.estCaseDeType(roiPos.getL()-1, roiPos.getC(), TypePion.NOIR)
                && pt.estCaseDeType(roiPos.getL(), roiPos.getC()+1, TypePion.NOIR)
                && pt.estCaseDeType(roiPos.getL(), roiPos.getC()-1, TypePion.NOIR));
    }

    public List<Pion> pionCapture(Pion pion){
        Point posPion = pion.getPosition();
        int pionC = posPion.getC();
        int pionL = posPion.getL();
        List<Pion> captures = new ArrayList<>();

        if(pionL-2 >= 0 && pt.estCaseDeCouleur(pionL-1, pionC, pion.getCouleur().getOppose()) && pt.estCaseDeCouleur(pionL-2, pionC, pion.getCouleur()))
            captures.add(pt.capturerPion(new Point(pionL-1, pionC), pion));

        if (pionL+2 <= 8 && pt.estCaseDeCouleur(pionL+1, pionC, pion.getCouleur().getOppose()) && pt.estCaseDeCouleur(pionL+2, pionC, pion.getCouleur()))
            captures.add(pt.capturerPion(new Point(pionL+1, pionC), pion));

        if (pionC-2 >= 0 && pt.estCaseDeCouleur(pionL, pionC-1, pion.getCouleur().getOppose()) && pt.estCaseDeCouleur(pionL, pionC-2, pion.getCouleur()))
            captures.add(pt.capturerPion(new Point(pionL, pionC-1), pion));

        if ((pionC+2 <= 8 && pt.estCaseDeCouleur(pionL, pionC+1, pion.getCouleur().getOppose()) && pt.estCaseDeCouleur(pionL, pionC+2, pion.getCouleur())))
            captures.add(pt.capturerPion(new Point(pionL, pionC+1), pion));
        return captures;
    }

    public void annulerCoup() {
        // TODO annuler le coup dans jeu
        Coup c = coupsPrecedant.pop();
        coupsSuivant.push(c);
    }

    public void refaireCoup() {
        Coup c = coupsSuivant.pop();

        Pion pion = c.getPion();
        Point destination = c.getDestination();
        if(pt.peutDeplacer(pion, destination)) {
            pt.deplacerPion(pion, destination.getL(), destination.getC());
            coupsPrecedant.push(c);
        } else
            Configuration.instance().logger().severe("Deplacement impossible : ( " + pion.getType() + ":" + pion.getPosition().getC() + "," + pion.getPosition().getL() + ") -> " + destination.getL() + "," + destination.getC());
    }

    public void setSelectionner(Point point) {
        Pion tmp = pt.trouverPion(point, joueurCourant.getCouleur());
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
        Pion p = pt.trouverPion(point, joueurCourant.getCouleur());
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
            Coup coup = new Coup(pionSelect, point);
            joueCoup(coup);
            pionSelect = null;
            return true;
        }
        return false;
    }

    public List<Pion> getPionClickable() {
        List<Pion> pions = getPionsCourant();

        return pions.stream().filter(pion -> !pt.getCasesAccessibles(pion).isEmpty()).collect(Collectors.toList());
    }

    public Plateau getPlateau() {
        return pt;
    }
}

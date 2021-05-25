package controleur.IA;

import global.Configuration;
import modele.ConfigJeu;
import modele.Jeu;
import modele.Joueur.Couleur;
import modele.Plateau;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Coup;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class IADifficile extends IA{
    public final int MAX = 100000;
    public final int MIN = -100000;

    Map<ConfigJeu, List<Coup>> returnVal;
    Set<ConfigJeu> mem;

    public IADifficile(){
        returnVal = new HashMap<>();
        mem = new HashSet<>();
    }

    public int heuristique(Jeu j){
        Plateau p = j.getPlateau();
        Pion roi = p.getRoi();
        if(j.roiSorti()){
            //System.out.println("Roi Sorti");
            return MAX;
        }
        else if(j.roiCapture()){
            //System.out.println("Roi Capture");
            return MIN;
        }
        else if(j.getPlateau().getSortiesAccessibles() >= 2){
            return MAX - 1;
        }
        int autourRoiNoir = 0;
        int heuristique = 0;

        int lRoi = roi.getPosition().getL();
        int cRoi = roi.getPosition().getC();


        if(( lRoi+1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi+1, cRoi, TypePion.NOIR)){
            autourRoiNoir++;
        }

        if(( lRoi-1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi-1, cRoi, TypePion.NOIR)){
            autourRoiNoir++;
        }

        if(( lRoi == 4 && cRoi+1 == 4) || p.estCaseDeType(lRoi, cRoi+1, TypePion.NOIR)){
            autourRoiNoir++;
        }

        if(( lRoi == 4 && cRoi-1 == 4) || p.estCaseDeType(lRoi, cRoi-1, TypePion.NOIR)){
            autourRoiNoir++;
        }

        heuristique -= 5*autourRoiNoir;

        /***FIN ENCERCLEMENT ***/

        heuristique += 8*p.getBlancs().size();
        heuristique -= 16*(16-p.getNoirs().size());

        heuristique += 2 * j.getPlateau().getCasesAccessibles(roi).size();

        heuristique -= 1024 * j.getPlateau().getSortiesAccessibles();
        return heuristique;
    }

    //Configuration.instance.logger

    public int Minimax(Jeu j, Couleur tj, int profondeur, List<Coup> prec, int alpha, int beta){

        if(profondeur==0 || j.roiSorti() || j.roiCapture()){
            return heuristique(j);
        }
        ConfigJeu cj = new ConfigJeu(j.joueurCourant(), j);
        if(!mem.contains(cj)) {
            List<Coup> C = j.getListeCoups();
            int val;
            if (tj.equals(Couleur.BLANC)) {
                val = MIN;
            } else {
                val = MAX;
            }
            int borne;
            List<Coup> coups = new ArrayList<>();
            Coup meilleur = null;
            for (Coup cp : C) {
                prec.add(cp);
                int dL = cp.getPion().getPosition().getL();
                int dC = cp.getPion().getPosition().getC();
                //System.out.println("Prof " + profondeur);
                if (tj.equals(Couleur.BLANC)) {
                    //System.out.println("Prof blanc " + profondeur);
                    Coup temp = new Coup(cp);
                    borne = Minimax(j.joueCoupDuplique(cp), Couleur.NOIR, profondeur - 1, prec, alpha, beta);
                    if (borne > val) {
                        val = borne;
                        meilleur = new Coup(temp);
                        coups.clear();
                    } else if (borne == val) coups.add(cp);
                    alpha = max(alpha, val);
                    if (alpha >= beta) {
                        j.annulerCoup(prec, dL, dC);
                        break;
                    }
                } else {
                    //System.out.println("Prof noir " + profondeur);
                    Coup temp = new Coup(cp);
                    borne = Minimax(j.joueCoupDuplique(cp), Couleur.BLANC, profondeur - 1, prec, alpha, beta);
                    if (borne < val) {
                        val = borne;
                        //if(profondeur == 1) System.out.println(profondeur + ": " + cp + " *** " + temp + "heuristique: " + borne);
                        //System.out.println("temp in boucle : " + temp);
                        meilleur = new Coup(temp);
                        //System.out.println("meilleur boucle: " + meilleur);
                        coups.clear();
                    } else if (borne == val) coups.add(cp);
                    //if(profondeur==1) System.out.println(coups);
                    beta = min(beta, val);
                    if (beta <= alpha) {
                        j.annulerCoup(prec, dL, dC);
                        break;
                    }
                }
                j.annulerCoup(prec, dL, dC);
            }
            if (meilleur == null) {
                Configuration.instance().logger().severe("Pas de coups jouables");
            } else {
                //System.out.println("meilleur fin :    " + meilleur);
                coups.add(meilleur);
                List<Coup> copy = new ArrayList<>(coups);
                returnVal.put(cj, copy);
                coups.clear();
            }
            return val;
        }
        return heuristique(cj.getJeu());
    }


    public Coup iaJoue(Jeu j){
        Minimax(j, j.joueurCourant().getCouleur(),3, new ArrayList<>(),MIN, MAX);
        Random r = new Random();
        ConfigJeu cj = new ConfigJeu(j.joueurCourant(), j);
        System.out.println("Liste coups: -----------------------");
        List<Coup> cps = returnVal.get(cj);
        for (Coup c: cps
             ) {
            System.out.println(c);
            //j.annulerCoup(c, dL, dC);
        }
        Coup res = cps.get(r.nextInt(cps.size()));
        System.out.println("Taille : " + cps.size() + "   Coup choisi: " + res);
        return res;
    }

}

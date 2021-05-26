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

    Map<ConfigJeu, Coup> returnVal;
    Set<ConfigJeu> mem;

    public IADifficile(){
        returnVal = new HashMap<>();
        mem = new HashSet<>();
    }

    public int heuristique(Jeu j, int profondeur){
        Plateau p = j.getPlateau();
        Pion roi = p.getRoi();
        if(j.roiSorti()){
            //System.out.println("Roi Sorti a la profondeur : " + profondeur);
            return MAX+profondeur-100;
        }
        if(j.roiCapture()){
            //System.out.println("Roi Capture");
            return MIN;
        }
        if(j.getPlateau().getSortiesAccessibles() >= 2){
            return MAX + profondeur - 164;
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

        heuristique -= 8*autourRoiNoir;

        /***FIN ENCERCLEMENT ***/

        heuristique += 8*p.getBlancs().size();
        heuristique -= 16*(16-p.getNoirs().size());

        heuristique += 4 * j.getPlateau().getCasesAccessibles(roi).size();

        heuristique += 1024 * j.getPlateau().getSortiesAccessibles();

        return heuristique;
    }


    public int Minimax(Jeu j, Couleur couleur, int profondeur, List<Coup> prec, int alpha, int beta){

        if(profondeur==0 || j.roiSorti() || j.roiCapture()){
            return heuristique(j,profondeur);
        }
        ConfigJeu cj = new ConfigJeu(couleur, j);
        if(!mem.contains(cj)) {
            List<Coup> C = j.getListeCoups();
            int val;
            if (couleur.equals(Couleur.BLANC)) {
                val = MIN;
            } else {
                val = MAX;
            }
            int borne;
            Coup meilleur = null;
            Random r = new Random();
            //List<Coup> coups = new ArrayList<>();
            for (Coup cp : C) {
                prec.add(cp);
                int dL = cp.getPion().getPosition().getL();
                int dC = cp.getPion().getPosition().getC();
                if (couleur.equals(Couleur.BLANC)) {
                    borne = Minimax(j.joueCoupDuplique(cp), Couleur.NOIR, profondeur - 1, prec, alpha, beta);
                    if (borne > val) {
                        val = borne;
                        /*coups.clear();
                        coups.add(cp);*/
                        meilleur = cp;
                    } else if (borne == val){
                        if(r.nextBoolean()){
                            meilleur = cp;
                        }
                    }
                    alpha = max(alpha, borne);
                    if (alpha >= beta) {
                        j.annulerCoup(prec,dL,dC);
                        break;
                    }
                } else {
                    Jeu j2 = j.joueCoupDuplique(cp);
                    borne = Minimax(j2, Couleur.BLANC, profondeur - 1, prec, alpha, beta);
                    if (borne < val) {
                        System.out.println("Borne, val: " + borne + "," + val);
                        val = borne;
                        /*coups.clear();
                        coups.add(cp);*/
                        meilleur = cp;
                    } else if (borne == val){
                        if(r.nextBoolean()){
                            meilleur = cp;
                        }
                    }
                    beta = min(beta, borne);
                    if (beta <= alpha) {
                        j.annulerCoup(prec, dL, dC);
                        break;
                    }
                }
                j.annulerCoup(prec,dL,dC);
            }
            //List<Coup> copy = new ArrayList<>(coups);
            returnVal.put(cj, meilleur);
            //coups.clear();
            return val;
        }
        return heuristique(cj.getJeu(), profondeur);
    }


    public Coup iaJoue(Jeu j){
        Couleur couleur = j.joueurCourant().getCouleur();
        Minimax(j, couleur,4, new ArrayList<>(),MIN, MAX);
        //Random r = new Random();
        ConfigJeu cj = new ConfigJeu(couleur, j);
        /*List<Coup> cps = returnVal.get(cj);
        System.out.println("Liste coups: -----------------------");
        for (Coup c: cps) {
            int dL = c.getPion().getPosition().getL();
            int dC = c.getPion().getPosition().getC();
            List<Coup> aled = new ArrayList<>();
            aled.add(c);
            System.out.print(c);
            Jeu j2 = j.joueCoupDuplique(c);
            System.out.println("    Heuristique: " + heuristique(j2,0));
            j.annulerCoup(aled,dL,dC);
        }*/

        Coup res = returnVal.get(cj);
        //System.out.println("Taille : " + cps.size() + "   Coup choisi: " + res);
        System.out.println(res);
        return res;
    }

}

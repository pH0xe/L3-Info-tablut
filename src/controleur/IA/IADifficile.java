package controleur.IA;

import global.Configuration;
import jdk.swing.interop.SwingInterOpUtils;
import modele.ConfigJeu;
import modele.Jeu;
import modele.Joueur.Couleur;
import modele.Plateau;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Coup;

import java.time.Instant;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class IADifficile extends IAMiniMax{



    public IADifficile(){
        returnVal = new HashMap<>();
        mem = new HashSet<>();
        prof=4;
    }

    public int heuristique(Jeu j, int profondeur){
        Plateau p = j.getPlateau();
        Pion roi = p.getRoi();


        if(j.roiSorti()){
            return MAX + 32*profondeur;

        }
        if(j.roiCapture()){

            return MIN - 32 * profondeur;
        }

        if(j.getPlateau().getSortiesAccessibles()>=2){
            return MAX - 1000 + 5*profondeur;
        }

        int adjacentRoi = 0;
        int heuristique = 0;

        int lRoi = roi.getPosition().getL();
        int cRoi = roi.getPosition().getC();


        if(( lRoi+1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi+1, cRoi, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 5*adjacentRoi;
        }

        if(( lRoi-1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi-1, cRoi, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 5*adjacentRoi;
        }

        if(( lRoi == 4 && cRoi+1 == 4) || p.estCaseDeType(lRoi, cRoi+1, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 5*adjacentRoi;
        }

        if(( lRoi == 4 && cRoi-1 == 4) || p.estCaseDeType(lRoi, cRoi-1, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 5*adjacentRoi;
        }

        //heuristique -= 24*adjacentRoi;


        if(j.joueurCourant().getCouleur() == Couleur.BLANC){
            heuristique += 8*p.getBlancs().size();
            heuristique -= 6*(16-p.getNoirs().size());
        }else{
            heuristique -= 8*p.getNoirs().size();
            heuristique += 6*(16-p.getBlancs().size());
        }


        heuristique += 4 * j.getPlateau().getCasesAccessibles(roi).size();

        heuristique += 64 * j.getPlateau().getSortiesAccessibles() + 5*profondeur;

        //heuristique += (j.getPlateau().getNbCases(Couleur.BLANC));

        return heuristique;
    }


    /*public int Minimax(Jeu j, Couleur couleur, int profondeur, List<Coup> prec, int alpha, int beta) {
        ConfigJeu cj = new ConfigJeu(couleur, j, profondeur);
        int borne = couleur.equals(Couleur.BLANC) ? MIN : MAX;
        if(Instant.now().compareTo(maintenant.plusSeconds(15)) < 0 ){
            if (profondeur == 0 || j.roiSorti() || j.roiCapture()) {
                return heuristique(j, profondeur);
            }

            //if (!mem.contains(cj) ) {

                List<Coup> C = j.getListeCoups();
                Coup meilleur = null;
                Random r = new Random();
                for (Coup cp : C) {
                    prec.add(cp);
                    int dL = cp.getPion().getPosition().getL();
                    int dC = cp.getPion().getPosition().getC();
                    if (couleur.equals(Couleur.BLANC)) {
                        borne = Minimax(j.joueCoupDuplique(cp), Couleur.NOIR, profondeur - 1, prec, alpha, beta);
                        if (borne > alpha) {
                            alpha = borne;
                            if(profondeur == prof)
                                meilleur = cp;
                        } else if (borne == alpha) {
                            if (r.nextBoolean()) {
                                meilleur = cp;
                            }
                        }
                        if (alpha >= beta) {
                            j.annulerCoup(prec, dL, dC);
                            break;
                        }
                    } else {
                        Jeu j2 = j.joueCoupDuplique(cp);
                        borne = Minimax(j2, Couleur.BLANC, profondeur - 1, prec, alpha, beta);
                        if (borne < beta) {
                            beta = borne;
                            if(profondeur == prof)
                                meilleur = cp;
                        } else if (borne == beta) {
                            if (r.nextBoolean()) {
                                meilleur = cp;
                            }
                        }
                        if (beta <= alpha) {
                            j.annulerCoup(prec, dL, dC);
                            break;
                        }
                    }
                    j.annulerCoup(prec, dL, dC);
                }
                returnVal.put(cj, meilleur);
                mem.add(cj);
                return borne;
            }
            return heuristique(cj.getJeu(), profondeur);

    }*/


    public Coup iaJoue(Jeu j){
        prof = 4;
        Couleur couleur = j.joueurCourant().getCouleur();

        maintenant = Instant.now();
        System.out.println(maintenant);
        System.out.println(j.getPlateau().getNbCases(Couleur.BLANC));
        do{
            System.out.println("DIFFICILE " + prof);
            Minimax(j, couleur, prof , new ArrayList<>(),MIN, MAX);
            prof++;
        }
        while(Instant.now().compareTo(maintenant.plusSeconds(10)) < 0 && prof <= 99);


        Coup res = null;
        System.out.println("Temps d'éxecution " + Instant.now());
        ConfigJeu cj = new ConfigJeu(couleur, j, prof);
        Random r = new Random();


        returnVal.get(cj).remove(dernierCoupJoue);

        int size = returnVal.get(cj).size();
        System.out.println(size);
        if(size > 0){
            res = returnVal.get(cj).get(r.nextInt(size));
            dernierCoupJoue = new Coup(res).inverseCoup();
            System.out.println(res +" ");
            return res;
        }
        else{
            j.getListeCoups().remove(dernierCoupJoue);
            res = j.getListeCoups().get(r.nextInt(j.getListeCoups().size()));
        }
        returnVal.clear();
        dernierCoupJoue = res;
        return res;
    }

}

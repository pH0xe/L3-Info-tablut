package controleur.IA;

import modele.ConfigJeu;
import modele.Jeu;
import modele.Joueur.Couleur;
import modele.Plateau;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Coup;

import java.time.Instant;
import java.util.*;

public abstract class IAMiniMax extends IA{
    public final int MAX = 1000000;
    public final int MIN = -1000000;

    Map<ConfigJeu, List<Coup>> returnVal;
    Set<ConfigJeu> mem;
    Instant maintenant;
    Coup dernierCoupJoue;
    int prof;

    @Override
    public abstract Coup iaJoue(Jeu j);

    public abstract int heuristique(Jeu j, int profondeur);
    /*public int heuristique(Jeu j, int profondeur){
        Plateau p = j.getPlateau();
        Pion roi = p.getRoi();
        if(j.roiSorti()){
            //System.out.println("Roi Sorti a la profondeur : " + profondeur);
            return MAX + 32*profondeur;

        }
        if(j.roiCapture()){
            //System.out.println("Roi Capture");
            return MIN - 32 * profondeur;
        }
        /*if(j.getPlateau().getSortiesAccessibles() >= 2){
            return MAX + 8*profondeur-64 ;
        }
        int adjacentRoi = 0;
        int heuristique = 0;

        int lRoi = roi.getPosition().getL();
        int cRoi = roi.getPosition().getC();


        if(( lRoi+1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi+1, cRoi, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 3*adjacentRoi;
        }

        if(( lRoi-1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi-1, cRoi, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 3*adjacentRoi;
        }

        if(( lRoi == 4 && cRoi+1 == 4) || p.estCaseDeType(lRoi, cRoi+1, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 3*adjacentRoi;
        }

        if(( lRoi == 4 && cRoi-1 == 4) || p.estCaseDeType(lRoi, cRoi-1, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 3*adjacentRoi;
        }

        heuristique -= 9*adjacentRoi;


        heuristique += 8*p.getBlancs().size();
        heuristique -= 6*p.getNoirs().size();

        heuristique += 16 * j.getPlateau().getCasesAccessibles(roi).size();

        heuristique += 256 * j.getPlateau().getSortiesAccessibles() + 64*profondeur;

        heuristique += 4*(j.getPlateau().getNbCases(Couleur.BLANC)+j.getPlateau().getNbCases(Couleur.NOIR));

        return heuristique;
    }*/


    public int Minimax(Jeu j, Couleur couleur, int profondeur, List<Coup> prec, int alpha, int beta) {
        ConfigJeu cj = new ConfigJeu(couleur, j, profondeur);
        int borne = couleur.equals(Couleur.BLANC) ? MIN : MAX;
        if(Instant.now().compareTo(maintenant.plusSeconds(10)) < 0 ){
            if (profondeur == 0 || j.roiSorti() || j.roiCapture()) {

                return heuristique(j, profondeur);
            }

            //if (!mem.contains(cj)  && profondeur >= prof-profondeur) {
                List<Coup> C = j.getListeCoups();
                //int borne;
                Coup meilleur = null;
                Random r = new Random();
                //r.setSeed(30102000);
                List<Coup> coups = new ArrayList<>();
                for (Coup cp : C) {
                    prec.add(cp);
                    int dL = cp.getPion().getPosition().getL();
                    int dC = cp.getPion().getPosition().getC();
                    if (couleur.equals(Couleur.BLANC)) {
                        borne = Minimax(j.joueCoupDuplique(cp), Couleur.NOIR, profondeur - 1, prec, alpha, beta);
                        if (borne > alpha) {
                            alpha = borne;
                            //cj.setAlpha(alpha);
                            if(profondeur == prof && dernierCoupJoue != null && cp !=dernierCoupJoue )
                                meilleur = cp;
                                coups.clear();
                                coups.add(cp);
                        } else if (borne == alpha) {
                            coups.add(cp);
                            meilleur = cp;
                        }
                            /*alpha = max(alpha, borne);
                            if(alpha < borne){
                                alpha = borne;
                                if(profondeur == prof){
                                    meilleur = cp;
                                }
                            }*/
                        if (alpha >= beta) {
                            j.annulerCoup(prec, dL, dC);
                            break;
                        }
                    } else {
                        Jeu j2 = j.joueCoupDuplique(cp);
                        borne = Minimax(j2, Couleur.BLANC, profondeur - 1, prec, alpha, beta);
                        if (borne < beta) {
                            beta = borne;
                            //cj.setBeta(beta);

                            if(profondeur == prof && dernierCoupJoue != null && cp !=dernierCoupJoue)
                                meilleur = cp;
                                coups.clear();
                                coups.add(cp);
                        } else if (borne == beta) {
                            if (r.nextBoolean()) {
                                meilleur = cp;
                                coups.add(cp);
                            }
                        }
                        //beta = min(beta, borne);
                            /*if(beta > borne){
                                beta = borne;
                                if(profondeur == prof){
                                    meilleur = cp;
                                }
                            }*/
                        if (beta <= alpha) {
                            j.annulerCoup(prec, dL, dC);
                            break;
                        }
                    }
                    j.annulerCoup(prec, dL, dC);
                }
                List<Coup> copy = new ArrayList<>(coups);
                returnVal.put(cj, copy);
                //mem.add(cj);
                coups.clear();
                return borne;
        }

        return heuristique(cj.getJeu(), profondeur);

        //}
        //return heuristique(cj.getJeu(), profondeur);
    }
}

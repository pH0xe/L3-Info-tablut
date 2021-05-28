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

public class IADifficile extends IA{
    public final int MAX = 100000;
    public final int MIN = -100000;

    Map<ConfigJeu, Coup> returnVal;
    Set<ConfigJeu> mem;
    Instant maintenant;
    int prof;
    public IADifficile(){
        returnVal = new HashMap<>();
        mem = new HashSet<>();
        prof=4;
    }

    public int heuristique(Jeu j, int profondeur){
        Plateau p = j.getPlateau();
        Pion roi = p.getRoi();
        if(j.roiSorti()){
            //System.out.println("Roi Sorti a la profondeur : " + profondeur);
                return MAX + 12*profondeur;

        }
        if(j.roiCapture()){
            //System.out.println("Roi Capture");
            return MIN;
        }
        if(j.getPlateau().getSortiesAccessibles() >= 2){
            return MAX + 8*profondeur-64 ;
        }
        int adjacentRoi = 0;
        int heuristique = 0;

        int lRoi = roi.getPosition().getL();
        int cRoi = roi.getPosition().getC();


        if(( lRoi+1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi+1, cRoi, TypePion.NOIR)){
            adjacentRoi++;
        }

        if(( lRoi-1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi-1, cRoi, TypePion.NOIR)){
            adjacentRoi++;
        }

        if(( lRoi == 4 && cRoi+1 == 4) || p.estCaseDeType(lRoi, cRoi+1, TypePion.NOIR)){
            adjacentRoi++;
        }

        if(( lRoi == 4 && cRoi-1 == 4) || p.estCaseDeType(lRoi, cRoi-1, TypePion.NOIR)){
            adjacentRoi++;
        }

        heuristique -= 8*adjacentRoi;

        /***FIN ENCERCLEMENT ***/

        heuristique += 8*p.getBlancs().size();
        heuristique -= 6*p.getNoirs().size();

        heuristique += 16 * j.getPlateau().getCasesAccessibles(roi).size();

        heuristique += 256 * j.getPlateau().getSortiesAccessibles();

        heuristique += 4*(j.getPlateau().getNbCases(Couleur.BLANC)+j.getPlateau().getNbCases(Couleur.NOIR));

        return heuristique;
    }


    public int Minimax(Jeu j, Couleur couleur, int profondeur, List<Coup> prec, int alpha, int beta) {
        ConfigJeu cj = new ConfigJeu(couleur, j);
        if(Instant.now().compareTo(maintenant.plusSeconds(20)) < 0 ){
            if (profondeur == 0 || j.roiSorti() || j.roiCapture()) {
                return heuristique(j, profondeur);
            }

            if (!mem.contains(cj) ) {

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
                //r.setSeed(30102000);
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

                        } else if (borne == val) {
                            if (r.nextBoolean()) {
                                meilleur = cp;
                            }
                        }
                        //alpha = max(alpha, borne);
                        if(alpha < borne){
                            alpha = borne;
                            /*if(profondeur == prof){
                                meilleur = cp;
                            }*/
                        }
                        if (alpha >= beta) {
                            j.annulerCoup(prec, dL, dC);
                            break;
                        }
                    } else {
                        Jeu j2 = j.joueCoupDuplique(cp);
                        borne = Minimax(j2, Couleur.BLANC, profondeur - 1, prec, alpha, beta);
                        if (borne < val) {
                            val = borne;
                            /*coups.clear();
                            coups.add(cp);*/
                            meilleur = cp;
                        } else if (borne == val) {
                            if (r.nextBoolean()) {
                                meilleur = cp;
                            }
                        }
                        //beta = min(beta, borne);
                        if(beta > borne){
                            beta = borne;
                            /*if(profondeur == prof){
                                meilleur = cp;
                            }*/
                        }
                        if (beta <= alpha) {
                            j.annulerCoup(prec, dL, dC);
                            break;
                        }
                    }
                    j.annulerCoup(prec, dL, dC);
                }
                //List<Coup> copy = new ArrayList<>(coups);
                returnVal.put(cj, meilleur);

                //mem.add(cj);
                //coups.clear();
                return val;
            }
            return heuristique(cj.getJeu(), profondeur);
        }
        return couleur.equals(Couleur.BLANC) ? MIN : MAX;
    }


    public Coup iaJoue(Jeu j){


        Couleur couleur = j.joueurCourant().getCouleur();

        maintenant = Instant.now();
        System.out.println(maintenant);
        System.out.println(j.getPlateau().getNbCases(Couleur.BLANC));
        //do{
            //System.out.println("boucle " + Instant.now() + " sortie à : " +maintenant.plusSeconds(15));
            Minimax(j, couleur, prof , new ArrayList<>(),MIN, MAX);
          //  prof++;
        //}
        //while(Instant.now().compareTo(maintenant.plusSeconds(20)) < 0);



        System.out.println("Temps d'éxecution " + Instant.now());
        ConfigJeu cj = new ConfigJeu(couleur, j);
        Coup res = returnVal.get(cj);

        System.out.println(res +" "+ heuristique(cj.getJeu(),prof));
        System.out.println(cj.getJeu().getPlateau()+ "\n Sorties accessibles : " + cj.getJeu().getPlateau().getSortiesAccessibles());
        return res;
    }

}

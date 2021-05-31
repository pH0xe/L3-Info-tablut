package controleur.IA;

import modele.ConfigJeu;
import modele.Jeu;
import modele.Joueur.Couleur;
import modele.Plateau;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Coup;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class IAMoyenne extends IAMiniMax{

    public IAMoyenne(){
        returnVal = new HashMap<>();
        mem = new HashSet<>();
        prof=2;
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

        int adjacentRoi = 0;
        int heuristique = 0;

        int lRoi = roi.getPosition().getL();
        int cRoi = roi.getPosition().getC();


        if(( lRoi+1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi+1, cRoi, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 2*adjacentRoi;
        }

        if(( lRoi-1 == 4 && cRoi == 4) || p.estCaseDeType(lRoi-1, cRoi, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 2*adjacentRoi;
        }

        if(( lRoi == 4 && cRoi+1 == 4) || p.estCaseDeType(lRoi, cRoi+1, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 2*adjacentRoi;
        }

        if(( lRoi == 4 && cRoi-1 == 4) || p.estCaseDeType(lRoi, cRoi-1, TypePion.NOIR)){
            adjacentRoi++;
            heuristique -= 2*adjacentRoi;
        }

        heuristique -= 15*adjacentRoi;

        heuristique += 8*p.getBlancs().size();
        heuristique -= 6*p.getNoirs().size();

        heuristique += 16 * j.getPlateau().getCasesAccessibles(roi).size();

        heuristique += 256 * j.getPlateau().getSortiesAccessibles() + 32*profondeur;

        heuristique += 2*(j.getPlateau().getNbCases(Couleur.BLANC));

        return heuristique;
    }

    @Override
    public Coup iaJoue(Jeu j) {

        prof = 2;
        Couleur couleur = j.joueurCourant().getCouleur();

        maintenant = Instant.now();
        System.out.println(maintenant);
        System.out.println(j.getPlateau().getNbCases(Couleur.BLANC));
        do{
            System.out.println("MOYENNE " + prof);
            Minimax(j, couleur, prof , new ArrayList<>(),MIN, MAX);
            prof++;
        }
        while(Instant.now().compareTo(maintenant.plusSeconds(5)) < 0 && prof <= 4);


        Coup res = null;
        System.out.println("Temps d'éxecution " + Instant.now());
        ConfigJeu cj = new ConfigJeu(couleur, j, prof);
        Random r = new Random();
        int size = returnVal.get(cj).size();
        if(size > 0){
            res = returnVal.get(cj).get(r.nextInt(size));
            System.out.println(res +" ");
            return res;
        }
        else{
            res = j.getListeCoups().get(r.nextInt(j.getListeCoups().size()));
        }
        returnVal.clear();
        return res;


        //System.out.println(cj.getJeu().getPlateau()+ "\n Sorties accessibles : " + cj.getJeu().getPlateau().getSortiesAccessibles());return res;
    }
}

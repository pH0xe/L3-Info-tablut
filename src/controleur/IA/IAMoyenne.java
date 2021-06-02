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

        heuristique -= 20*adjacentRoi;


        heuristique += 8*p.getBlancs().size();
        heuristique -= 6*p.getNoirs().size();

        heuristique += 16 * j.getPlateau().getCasesAccessibles(roi).size();

        heuristique += 256 * j.getPlateau().getSortiesAccessibles() + 64*profondeur;

        heuristique += 4*(j.getPlateau().getNbCases(Couleur.BLANC)+j.getPlateau().getNbCases(Couleur.NOIR));

        return heuristique;
        /*Plateau p = j.getPlateau();
        Pion roi = p.getRoi();

        if(j.roiSorti()){
            return MAX + 64*profondeur;

        }
        if(j.roiCapture()){

            return MIN - 64 * profondeur;
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

        heuristique -= 15*adjacentRoi;

        heuristique += 8*p.getBlancs().size();
        heuristique -= 16*p.getNoirs().size();

        heuristique += 16 * p.getCasesAccessibles(roi).size();

        heuristique += 256 * p.getSortiesAccessibles() + 32*profondeur;

        heuristique += 4*(p.getNbCases(Couleur.BLANC));

        return heuristique;*/
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
        while(Instant.now().compareTo(maintenant.plusSeconds(5)) < 0 && prof <= 3);


        Coup res = null;
        System.out.println("Coup a ne pas jouer : " + dernierCoupJoue);
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


        //System.out.println(cj.getJeu().getPlateau()+ "\n Sorties accessibles : " + cj.getJeu().getPlateau().getSortiesAccessibles());return res;
    }
}

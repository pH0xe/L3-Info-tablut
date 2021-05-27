package controleur;

import global.Configuration;
import global.reader.BoardReaderText;
import modele.Jeu;
import modele.JeuTuto;
import modele.Joueur.*;
import vue.InterfaceGraphique;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controleur implements CollecteurEvenements {
    private Jeu j;
    private JeuTuto jt;
    private InterfaceGraphique it;
    Timer timer = new Timer(10,null);

    public Controleur(){

    }

    @Override
    public void clicSourisTuto(int l, int c) {
        switch (jt.getEtat()){
            case 0 -> jt.setHighlightCase(4,3);
            case 1 -> jt.traiteDeplacement(l,c,2,3);
            case 2 -> jt.traiteDeplacement(l,c,3,3);
            case 3 -> jt.traiteDeplacement(l,c,4,3);
            case 4 -> jt.traiteDeplacement(l,c,4,2);
            case 5 -> jt.traiteDeplacement(l,c,6,0);
            case 6 -> jt.traiteDeplacement(l,c,6,1);
            case 7 -> jt.traiteDeplacement(l,c,5,5);
            case 8 -> jt.traiteDeplacement(l,c,5,2);
            case 9 -> jt.traiteDeplacement(l,c, 5,7);
            case 10 -> jt.traiteDeplacement(l,c, 5,3);
            case 11 -> jt.traiteDeplacement(l,c, 2,8);
            case 12 -> jt.traiteDeplacement(l,c, 4,2);
            case 13 -> jt.traiteDeplacement(l,c, 0,8);
        }
        if( jt.getEtat() == 0)
            jt.setEtat(1);
        if(jt.getEtat() == 5 && jt.getEtatDeplace() == 0)
            loadPlateauTuto("TutoBoard2.txt");
        if(jt.getEtat() == 9 && jt.getEtatDeplace() == 0)
            loadPlateauTuto("TutoBoard3.txt");
        if(jt.getEtat() == 11 && jt.getEtatDeplace() == 0)
            loadPlateauTuto("TutoBoard4.txt");
        it.update();
        if(jt.getEtat() == 14)
            clicRefaireTuto();
        if(jt.getEtatDeplace() == 2 && !(jt.getUnanimateEtat().contains(jt.getEtat())))
                timer.start();
    }

    @Override
    public void animationChangerEtat() {
        switch (jt.getEtat()){
            case 1 -> jt.setHighlightCase(3,0);
            case 2 -> jt.setHighlightCase(4,2);
            case 5 -> jt.setHighlightCase(7,1);
            case 6 -> jt.setHighlightCase(6,5);
            case 7 -> jt.setHighlightCase(6,2);
            case 9 -> jt.setHighlightCase(5,0);
            case 11 -> jt.setHighlightCase(4,1);
            case 12 -> jt.setHighlightCase(2,8);
        }
        jt.setEtatDeplace(0);
        it.update();
        jt.setEtat(jt.getEtat()+1);
    }

    @Override
    public void stopTimer(){ timer.stop(); }

    @Override
    public void clicRefaireTuto() {
        jt = new JeuTuto(new Jeu(new Joueur("Jouer1", Couleur.BLANC), new Joueur("Jouer2", Couleur.NOIR)), 0);
        it.addJeuTuto(jt);
        it.update();
    }

    @Override
    public void loadPlateauTuto(String filename) {
        InputStream in = Configuration.charger("tutorials" + File.separator + filename);
        BoardReaderText br = new BoardReaderText(in);
        br.lirePlateau();
        jt.getJeu().getPlateau().setPlateau(br);
        it.update();
        Configuration.instance().logger().info("Loaded board for tutorial : " + filename);
    }

    @Override
    public void fixerInterface(InterfaceGraphique interfaceGraphique) {
        it = interfaceGraphique;
    }

    @Override
    public void fixerJeu(Jeu jeu) {
        j = jeu;
    }

    @Override
    public void fixerJeuTuto(JeuTuto jeu){ jt = jeu;}

    @Override
    public void fixerTimer(Timer tm) { timer = tm; }

    @Override
    public void demarrerJeu() {
    }
}

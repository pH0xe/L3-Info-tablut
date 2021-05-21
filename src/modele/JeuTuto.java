package modele;

import global.Configuration;
import java.util.List;

public class JeuTuto {
    private Jeu jeu;
    private int etat;
    private int sizeCase;
    private Point highlightCase;
    public Point sourceCase;
    private boolean showCasesAccessibles;

    public JeuTuto(Jeu jeu, int etat){
        this.jeu = jeu;
        this.etat = etat;
        showCasesAccessibles = false;
        highlightCase = new Point(4,3);
    }

    public Jeu getJeu(){ return jeu; }

    public int getEtat(){ return etat; }

    public void setEtat(int e){
        etat = e;
    }

    public void setSizeCase(int sz){ sizeCase = sz; }

    public int getSizeCase(){ return sizeCase; }

    public void setHighlightCase(int l, int c) { highlightCase = new Point(l, c); }

    public Point getHighlightCase(){ return highlightCase; }

    public void setShowCasesAccessibles(boolean bool){ showCasesAccessibles = bool; }

    public boolean getShowCasesAccessibles(){ return showCasesAccessibles; }
}

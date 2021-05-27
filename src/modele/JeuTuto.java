package modele;

import global.reader.BoardReaderText;
import modele.pion.EtatPion;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class JeuTuto {
    private Jeu jeu;
    private int etat;
    private int etatDeplace;
    private Point highlightCase;
    public Point sourceCase;
    private boolean showCasesAccessibles;
    private final List<Integer> unanimateEtat = new ArrayList<>(Arrays.asList(3,4,8,10,13));

    public JeuTuto(Jeu jeu, int etat){
        this.jeu = jeu;
        this.etat = etat;
        this.etatDeplace = 0;
        showCasesAccessibles = false;
        highlightCase = new Point(-1,-1);
    }

    public List<Integer> getUnanimateEtat(){ return unanimateEtat; }

    public Jeu getJeu(){ return jeu; }

    public int getEtat(){ return etat; }

    public void setEtat(int e){
        etat = e;
    }

    public int getEtatDeplace(){ return etatDeplace; }

    public void setEtatDeplace(int e){ etatDeplace = e; }

    public void setHighlightCase(int l, int c) { highlightCase = new Point(l, c); }

    public Point getHighlightCase(){ return highlightCase; }

    public void setShowCasesAccessibles(boolean bool){ showCasesAccessibles = bool; }

    public boolean getShowCasesAccessibles(){ return showCasesAccessibles; }

    public boolean isHighlightCase(int l, int c){ return l == getHighlightCase().getL() && c == getHighlightCase().getC(); }

    public Pion getPionCourant(Point point){
        Plateau pt = jeu.getPlateau();
        return pt.trouverPion(point, pt.getTypePion(point).getCouleur());
    }

    public void traiteDeplacement(int l, int c, int destL, int destC){
        switch (getEtatDeplace()){
            case 0:
                if(isHighlightCase(l,c)){
                    setShowCasesAccessibles(true);
                    sourceCase = getHighlightCase();
                    setHighlightCase(destL, destC);
                    setEtatDeplace(getEtatDeplace()+1);
                }
                break;
            case 1:
                if(isHighlightCase(l,c)){
                    getJeu().getPlateau().deplacerPion(getPionCourant(sourceCase),getHighlightCase().getL(),getHighlightCase().getC());
                    if(!getJeu().roiCapture())
                        getJeu().pionCapture(getPionCourant(getHighlightCase()));
                    setShowCasesAccessibles(false);
                    setHighlightCase(-1,-1);
                    setEtatDeplace(getEtatDeplace()+1);
                }
                break;
            case 2:
                if(unanimateEtat.contains(getEtat()))
                    traiteCasSpe();
                break;
        }
    }
    
    public void traiteCasSpe(){
        if(!(getEtat() == 10))
            getJeu().joueurSuivant();
        switch (getEtat()){
            case 3 -> setHighlightCase(4,1);
            case 4 -> setHighlightCase(7,0);
            case 8 -> setHighlightCase(4,7);
            case 10 -> setHighlightCase(2,5);
            case 13 -> setHighlightCase(-1,-1);
        }
        setEtatDeplace(0);
            setEtat(getEtat()+1);
    }
}

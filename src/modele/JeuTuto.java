package modele;

import global.reader.BoardReaderBinary;
import global.writer.BoardWriterBinary;
import modele.util.Point;

import java.io.File;
import java.util.List;
import java.util.Stack;

public class JeuTuto {
    private Jeu jeu;
    private int etat;
    private int etatDeplace;
    private Point highlightCase;
    public Point sourceCase;
    private boolean showCasesAccessibles;
    private final Stack<Integer> coupsPrecedent;
    private final Stack<Point> clickPrecedent;
    private final Stack<String> boardPrecedent;

    public JeuTuto(Jeu jeu, int etat){
        this.jeu = jeu;
        this.etat = etat;
        this.etatDeplace = 0;
        showCasesAccessibles = false;
        highlightCase = new Point(-1,-1);
        coupsPrecedent = new Stack<>();
        clickPrecedent = new Stack<>();
        boardPrecedent = new Stack<>();
    }

    public void addJeu(Jeu jeu){ this.jeu = jeu; }

    public Stack<Integer> getCoupsPrecedent(){ return coupsPrecedent; }

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

    public void addClickPrecedent(Point point){ clickPrecedent.push(point); }

    public void traiteDeplacement(int l, int c, int destL, int destC){
        switch (getEtatDeplace()){
            case 0:
                if(isHighlightCase(l,c)){
                    clickPrecedent.push(getHighlightCase());
                    coupsPrecedent.push(getEtat());
                    coupsPrecedent.push(getEtatDeplace());
                    setShowCasesAccessibles(true);
                    sourceCase = getHighlightCase();
                    setHighlightCase(destL, destC);
                    setEtatDeplace(getEtatDeplace()+1);
                }
                break;
            case 1:
                if(isHighlightCase(l,c)){
                    coupsPrecedent.push(getEtat());
                    coupsPrecedent.push(getEtatDeplace());
                    clickPrecedent.push(getHighlightCase());
                    jeu.setSelectionner(sourceCase);
                    if(jeu.verifierCoup(getHighlightCase())){
                        jeu.roiCapture();
                    }
                    setShowCasesAccessibles(false);
                    setHighlightCase(-1,-1);
                    setEtatDeplace(getEtatDeplace()+1);
                }
                break;
            case 2:
                if(getEtat() == 4 || getEtat() == 8 || getEtat() == 10){
                    String filename = "TutoBoardSave" + getEtat();
                    BoardWriterBinary bw = new BoardWriterBinary(filename);
                    try{
                        bw.ecrireJeu(jeu);
                    } catch(Exception ignored){}
                    boardPrecedent.push(filename);
                }

                switch (getEtat()){
                    case 1:
                        setHighlightCase(3,0);
                        break;
                    case 2:
                        setHighlightCase(4,2);
                        break;
                    case 3:
                        setHighlightCase(4,1);
                        break;
                    case 4:
                        setHighlightCase(7,0);
                        break;
                    case 5:
                        setHighlightCase(7,1);
                        break;
                    case 6:
                        setHighlightCase(6,5);
                        break;
                    case 7:
                        setHighlightCase(6,2);
                        break;
                    case 8:
                        setHighlightCase(4,7);
                        break;
                    case 9:
                        setHighlightCase(5,0);
                        break;
                    case 10:
                        setHighlightCase(2,5);
                        break;
                    case 11:
                        setHighlightCase(-1,-1);
                        break;
                }
                setEtatDeplace(0);
                setEtat(getEtat()+1);
                break;
        }
    }

    public void annulerCoupTuto(){
        if(getEtatDeplace() == 0 && (getEtat() == 5 || getEtat() == 9 || getEtat() == 11)){
            String filename = boardPrecedent.pop();
            BoardReaderBinary br = new BoardReaderBinary("data" + File.separator + "tutorial_saves" + File.separator + filename + ".dat");
            br.lirePlateau();
            jeu = new Jeu(br);
        }
        if(getEtatDeplace() == 0 || getEtatDeplace() == 2){
            List<Point> listParamTuto = jeu.annulerCoupTuto();
            sourceCase = listParamTuto.get(0);
            setHighlightCase(listParamTuto.get(1).getL(), listParamTuto.get(1).getC());
            setShowCasesAccessibles(true);
        } else if(getEtatDeplace() == 1)
            setShowCasesAccessibles(false);
        setEtatDeplace(coupsPrecedent.pop());
        setEtat(coupsPrecedent.pop());
        Point point = clickPrecedent.pop();
        setHighlightCase(point.getL(), point.getC());
    }
}

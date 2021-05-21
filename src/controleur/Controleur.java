package controleur;

import modele.Jeu;
import modele.JeuTuto;
import vue.InterfaceGraphique;

public class Controleur implements CollecteurEvenements {
    private Jeu j;
    private JeuTuto jt;
    private InterfaceGraphique it;

    public Controleur(){

    }

    @Override
    public void clicSourisTuto(int l, int c) {
        switch (jt.getEtat()){
            case 0:
                System.out.println("Clicked : " + l + "," + c );
                if(l == jt.getHighlightCase().getL() && c == jt.getHighlightCase().getC()){
                    jt.setShowCasesAccessibles(true);
                    jt.sourceCase = jt.getHighlightCase();
                    jt.setHighlightCase(1,3);
                    jt.setEtat(1);
                    it.update();
                }
                break;
            default:
                break;
        }
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
    public void demarrerJeu() {
    }
}

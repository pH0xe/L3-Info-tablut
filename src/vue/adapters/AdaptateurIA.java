package vue.adapters;

import controleur.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurIA implements ActionListener {
    private CollecteurEvenements controleur;
    private int IA;

    public AdaptateurIA(CollecteurEvenements controleur, int IA){
        this.controleur=controleur;
        this.IA=IA;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(IA == 1){
            controleur.joueIA1();
        }
        else{
            controleur.joueIA2();
        }
    }
}


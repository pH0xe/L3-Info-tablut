package modele;

import modele.Joueur.Couleur;
import modele.Joueur.Joueur;

import java.util.Objects;

public class ConfigJeu {
    private Couleur couleur;
    private Jeu jeu;
    private int  profondeur;

    public ConfigJeu(Couleur c, Jeu p, int profondeur){
        couleur = c;
        jeu = p;
        this.profondeur = profondeur;

    }


    public Couleur getCouleur() {
        return couleur;
    }

    public Jeu getJeu() {
        return jeu;
    }

    /*public int getAlpha(){return alpha;}

    public int getBeta(){return beta;}

    public void setAlpha(int alpha){ this.alpha = alpha; }

    public void setBeta(int beta){this.beta = beta;}*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigJeu configJeu = (ConfigJeu) o;
        return Objects.equals(couleur, configJeu.couleur) && Objects.equals(jeu, configJeu.jeu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couleur, jeu);
    }
}

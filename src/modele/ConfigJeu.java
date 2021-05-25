package modele;

import modele.Joueur.Joueur;

import java.util.Objects;

public class ConfigJeu {
    private Joueur joueurCourant;
    private Jeu jeu;

    public ConfigJeu(Joueur j, Jeu p){
        joueurCourant=j;
        jeu=p;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public Jeu getJeu() {
        return jeu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigJeu configJeu = (ConfigJeu) o;
        return Objects.equals(joueurCourant, configJeu.joueurCourant) && Objects.equals(jeu, configJeu.jeu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(joueurCourant, jeu);
    }
}

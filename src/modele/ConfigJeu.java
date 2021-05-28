package modele;

import modele.Joueur.Couleur;
import modele.Joueur.Joueur;

import java.util.Objects;

public class ConfigJeu {
    private Couleur couleur;
    private Jeu jeu;

    public ConfigJeu(Couleur c, Jeu p){
        couleur = c;
        jeu = p;
    }


    public Couleur getCouleur() {
        return couleur;
    }

    public Jeu getJeu() {
        return jeu;
    }

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

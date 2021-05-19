package modele;

import java.util.Objects;

public class ConfigJeu {
    private Joueur joueurCourant;
    private Plateau plateau;

    public ConfigJeu(Joueur j, Plateau p){
        joueurCourant=j;
        plateau=p;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigJeu configJeu = (ConfigJeu) o;
        return Objects.equals(joueurCourant, configJeu.joueurCourant) && Objects.equals(plateau, configJeu.plateau);
    }

    @Override
    public int hashCode() {
        return Objects.hash(joueurCourant, plateau);
    }
}

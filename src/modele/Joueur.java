package modele;

import java.util.Objects;

public class Joueur {
    private String nom;
    private TypeJoueur type;

    public Joueur(String nom, TypeJoueur type){
        this.nom = nom;
        this.type = type;
    }
    public String getNom(){
        return nom;
    }

    public TypeJoueur getType(){
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joueur joueur = (Joueur) o;
        return Objects.equals(nom, joueur.nom) && type == joueur.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, type);
    }
}

package modele;

import java.util.Objects;

public class Joueur {
    private String nom;
    private Couleur type;

    public Joueur(String nom, Couleur type){
        this.nom = nom;
        this.type = type;
    }
    public String getNom(){
        return nom;
    }

    public Couleur getCouleur(){
        return type;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    @Override
    public String toString() {
        return "Joueur{" +
                "nom='" + nom + '\'' +
                ", type=" + type +
                '}';
    }
}

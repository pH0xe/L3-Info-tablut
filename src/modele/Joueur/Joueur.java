package modele.Joueur;

import java.util.Objects;
import java.io.*;

public class Joueur implements Serializable {
    private String nom;
    private Couleur type;

    public Joueur(String nom, Couleur type){
        this.nom = nom;
        this.type = type;
    }

    public Joueur(Joueur j){
        this.type = j.getCouleur();
        this.nom = j.getNom();
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
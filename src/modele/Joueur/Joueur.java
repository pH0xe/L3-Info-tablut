package modele.Joueur;

import java.io.*;

public class Joueur implements Serializable {
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
}

package modele.pion;

import modele.Joueur.Couleur;
import modele.Joueur.Joueur;
import modele.util.Point;

import java.io.*;
import java.util.Objects;

public class Pion implements Serializable{
    private TypePion type;
    private EtatPion etat;
    private Point position;

    public Pion(TypePion type, Point position){
        this.type = type;
        this.position = position;
        this.etat = EtatPion.ACTIF;
    }

    public boolean estPris(){
        return etat == EtatPion.INACTIF;
    }

    public boolean estRoi() {
        return type == TypePion.ROI;
    }

    public void changerEtat(EtatPion etat){
        this.etat = etat;
    }

    public boolean deplacerPion(int l, int c){
        try{
            this.position.setL(l);
            this.position.setC(c);
            return true;
        }
        catch(Exception e){
            return false;
        }

    }

    public TypePion getType(){
        return type;
    }

    public EtatPion getEtat(){
        return etat;
    }

    public Point getPosition(){
        return position;
    }

    public Couleur getCouleur() {
        return type.getCouleur();
    }

    public boolean estBlanc() {
        return type.getCouleur() == Couleur.BLANC;
    }

    public boolean estNoir() {
        return type.getCouleur() == Couleur.NOIR;
    }


    @Override
    public String toString() {
        return "Pion{" +
                "type=" + type +
                ", etat=" + etat +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pion pion = (Pion) o;
        return type == pion.type && etat == pion.etat && Objects.equals(position, pion.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, etat, position);
    }
}

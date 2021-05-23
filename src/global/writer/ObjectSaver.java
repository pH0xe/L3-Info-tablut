package global.writer;

import modele.Joueur.Joueur;
import modele.pion.Pion;

import java.io.*;
import java.util.List;

public class ObjectSaver implements Serializable {
    private Joueur joueurB, joueurN;
    private List<Pion> pions;
    private int joueurCourant;

    public ObjectSaver(Joueur j1, Joueur j2, int jCourant, List<Pion> pions) {
        this.pions = pions;
        this.joueurB = j1;
        this.joueurN = j2;
        this.joueurCourant = jCourant;
    }

    public Joueur getJoueurB() {
        return joueurB;
    }

    public Joueur getJoueurN() {
        return joueurN;
    }

    public List<Pion> getPions() {
        return pions;
    }

    public int getJoueurCourant() {
        return joueurCourant;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ObjectSaver{ joueurB=").append(joueurB).append("\njoueurN=").append(joueurN).append("\npions=");
        for (Pion pion : pions) {
            sb.append("\t\n");
            sb.append(pion);
        }
        return sb.toString();
    }
}

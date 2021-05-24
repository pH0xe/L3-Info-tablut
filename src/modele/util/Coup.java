package modele.util;

import modele.pion.Pion;

import java.io.Serializable;

public class Coup implements Serializable {
    private Pion pion;
    private Point caseADeplacer;

    public Coup(Pion p, Point dest){
        pion = p;
        caseADeplacer = dest;
    }

    public Pion getPion() {return pion;}

    public Point getDestination() {return caseADeplacer;}

}

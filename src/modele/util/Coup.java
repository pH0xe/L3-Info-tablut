package modele.util;

import modele.pion.Pion;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

public class Coup implements Serializable {
    private Pion pion;
    private Point destination;
    private List<Pion> captures;

    public Coup(Pion p, Point dest){
        pion = p;
        caseADeplacer = dest;
        captures = new ArrayList<>();
    }

    public Coup(Coup c){
        this.pion = new Pion(c.getPion());
        this.caseADeplacer = new Point(c.getDestination());
        this.captures = new ArrayList<>();
        for (Pion p : c.getCaptures()){
            captures.add(new Pion(p));
        }
    }

    public void setCaptures(List<Pion> captures) {
        this.captures = captures;
    }

    public List<Pion> getCaptures() {
        return captures;
    }

    public Pion getPion() {return pion;}

    public Point getDestination() {return caseADeplacer;}

    @Override
    public String toString() {
        return "Coup{" +
                "pion=" + pion +
                ", caseADeplacer=" + caseADeplacer +
                '}';
    }
}

package modele.util;

import modele.pion.Pion;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;
import java.util.Objects;

public class Coup implements Serializable {
    private Pion pion;
    private Point destination;
    private List<Pion> captures;
    private Point origine;

    public Coup(Pion p, Point dest, int lOrigine, int cOrigine){
        pion = p;
        destination = dest;
        captures = new ArrayList<>();
        origine = new Point(lOrigine, cOrigine);
    }

    public Coup(Coup c){
        this.pion = new Pion(c.getPion());
        this.destination = new Point(c.getDestination());
        this.captures = new ArrayList<>();
        for (Pion p : c.getCaptures()){
            captures.add(new Pion(p));
        }
        this.origine = new Point(c.getOrigine());
    }

    public void setCaptures(List<Pion> captures) {
        this.captures = captures;
    }

    public List<Pion> getCaptures() {
        return captures;
    }

    public Pion getPion() {return pion;}

    public Point getDestination() {return destination;}

    public Point getOrigine() {
        return origine;
    }

    public void setDestination(Point destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Coup{" +
                "pion=" + pion +
                ", destination=" + destination +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coup coup = (Coup) o;
        return Objects.equals(pion, coup.pion) && Objects.equals(destination, coup.destination) && Objects.equals(captures, coup.captures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pion, destination, captures);
    }

    public Coup inverseCoup() {
        Pion tmp = new Pion(this.getPion());
        this.pion = new Pion(tmp.getType(), this.destination);
        this.destination = tmp.getPosition();

        return this;
    }
}
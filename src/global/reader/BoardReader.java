package global.reader;

import modele.pion.Pion;
import modele.util.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class BoardReader {
    private List<Pion> blancs;
    private List<Pion> noirs;
    private Pion roi;

    public BoardReader() {
        blancs = new ArrayList<>();
        noirs = new ArrayList<>();
    }

    public abstract void lirePlateau();

    public void insertNoirs(Pion p) {
        noirs.add(p);
    }

    public void insertBlancs(Pion p) {
        blancs.add(p);
    }

    public void setBlancs(List<Pion> blancs) {
        this.blancs = blancs;
    }

    public void setNoirs(List<Pion> noirs) {
        this.noirs = noirs;
    }

    public void setRoi(Pion roi) {
        this.roi = roi;
    }

    public List<Pion> getBlancs() {
        return blancs;
    }

    public List<Pion> getNoirs() {
        return noirs;
    }

    public Pion getRoi() {
        return roi;
    }
}

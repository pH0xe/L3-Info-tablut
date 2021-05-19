package modele;

public class Coup {
    private Pion pion;
    private Point caseADeplacer;

    public Coup(Pion p, Point dest){
        pion = p;
        caseADeplacer = dest;
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

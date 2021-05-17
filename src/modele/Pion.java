package modele;

public class Pion {
    private TypePion type;
    private EtatPion etat;
    private Point position;

    public Pion(TypePion type, Point position){
        this.type = type;
        this.position = position;
        this.etat = EtatPion.ACTIF;
    }

    public Pion(TypePion type, Point position, EtatPion etat){
        this.type = type;
        this.position = position;
        this.etat = etat;
    }

    public boolean estPris(){
        return etat == EtatPion.INACTIF;
    }

    public void changerEtat(){
        etat = EtatPion.INACTIF;
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
}

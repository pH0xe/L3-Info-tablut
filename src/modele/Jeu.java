package modele;

public class Jeu {
    private Joueur joueurCourant;
    private final Joueur j1;
    private final Joueur j2;

    public Jeu(Joueur j1, Joueur j2){
        this.j1 = j1;
        this.j2 = j2;

        this.joueurCourant = j1; // j1 = blancs
    }

    public Joueur joueurCourant(){
        return joueurCourant;
    }

    public void joueurSuivant(){
        if(joueurCourant == j1){
            joueurCourant = j2;
        }else{
            joueurCourant = j1;
        }
    }

}

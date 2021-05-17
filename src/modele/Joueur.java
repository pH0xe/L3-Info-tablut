package modele;

public class Joueur {
    private String nom;
    private TypeJoueur type;

    public Joueur(String nom, TypeJoueur type){
        this.nom = nom;
        this.type = type;
    }
    public String getNom(){
        return nom;
    }

    public TypeJoueur getType(){
        return type;
    }

}

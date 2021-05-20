package modele.Joueur;

public class Joueur {
    private String nom;
    private Couleur type;

    public Joueur(String nom, Couleur type){
        this.nom = nom;
        this.type = type;
    }
    public String getNom(){
        return nom;
    }

    public Couleur getCouleur(){
        return type;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}

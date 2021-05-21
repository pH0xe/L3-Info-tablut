
package modele;

public enum TypePion {
    ROI(Couleur.BLANC), NOIR(Couleur.NOIR), BLANC(Couleur.BLANC);

    private final Couleur couleur;

    TypePion(Couleur couleur) {
        this.couleur = couleur;
    }

    public Couleur getCouleur() {
        return couleur;
    }
}
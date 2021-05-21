package modele;

public enum Couleur {
    BLANC,NOIR;

    public Couleur getOppose() {
        if (this == BLANC) return NOIR;
        return BLANC;
    }
}
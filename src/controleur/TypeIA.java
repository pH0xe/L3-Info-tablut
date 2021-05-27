package controleur;

public enum TypeIA {
    NONE("Joueur"), FACILE("IA Facile"), MOYENNE("IA Moyenne"), DIFFICILE("IA Difficile");

    private final String label;

    TypeIA(String s) {
        this.label = s;
    }

    public String getLabel() {
        return label;
    }
}

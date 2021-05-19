package controleur.IA;

import modele.*;

import java.util.List;

public abstract class IA {
    public abstract int heuristique(Jeu j);
    public abstract int Minimax(Jeu j, TypeJoueur tj, int profondeur, List<Coup> cp);
    public abstract Coup iaJoue(Jeu j);
}

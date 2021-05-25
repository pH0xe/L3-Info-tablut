package global.writer;

import global.Configuration;
import modele.Jeu;
import modele.Joueur.Couleur;
import modele.pion.Pion;

import java.io.*;
import java.util.List;

public class BoardWriterBinary {
    BufferedOutputStream output;

    public BoardWriterBinary() {
        output = Configuration.creeFichierSave();
    }

    public void ecrireJeu(Jeu jeu) throws IOException {
        if (output == null) return;
        List<Pion> pions = jeu.getPlateau().getBlancs();
        pions.addAll(jeu.getPlateau().getNoirs());
        int joueurCourant = jeu.joueurCourant().getCouleur() == Couleur.BLANC ? 0 : 1;
        ObjectSaver obj = new ObjectSaver(jeu.getJoueurBlanc(), jeu.getJoueurNoir(), joueurCourant, pions, jeu.getCoupsPrecedent(), jeu.getCoupsSuivant());

        ObjectOutputStream objectStream = new ObjectOutputStream(output);
        objectStream.writeObject(obj);

        output.flush();
        output.close();
    }
}

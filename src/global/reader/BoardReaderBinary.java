package global.reader;

import global.writer.ObjectSaver;
import modele.Joueur.Joueur;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Point;

import java.io.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BoardReaderBinary extends BoardReader{
    private ObjectInputStream reader;
    private Joueur joueurBlanc, joueurNoir;
    private Joueur joueurCourant;

    public BoardReaderBinary(String path) {
        try {
            reader = new ObjectInputStream(new FileInputStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lirePlateau(){
        try {
            ObjectSaver obj = (ObjectSaver) reader.readObject();
            setRoi(obj.getPions().stream().filter(Pion::estRoi).findFirst().orElse(null));
            setBlancs(obj.getPions().stream().filter(pion -> pion.getType() == TypePion.BLANC).collect(Collectors.toList()));
            setNoirs(obj.getPions().stream().filter(pion -> pion.getType() == TypePion.NOIR).collect(Collectors.toList()));
            joueurBlanc = obj.getJoueurB();
            joueurNoir = obj.getJoueurN();
            joueurCourant = obj.getJoueurCourant() == 0 ? joueurBlanc : joueurNoir;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Joueur getJoueurBlanc() {
        return joueurBlanc;
    }

    public Joueur getJoueurNoir() {
        return joueurNoir;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }
}

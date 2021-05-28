import controleur.Controleur;
import global.BestScoresUtils;
import global.Configuration;
import vue.InterfaceGraphique;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        createRequiredFolder(Configuration.instance().getConfig("logDir"));
        createRequiredFolder("saves");
        createRequiredFolder("tutorial_saves");
        InterfaceGraphique.demarrer(new Controleur());
    }

    private static void createRequiredFolder(String pathStr) {
        Path path = Paths.get("data" + File.separator + pathStr);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            try {
                Files.createDirectories(path);
            } catch (Exception e) {
                System.err.println("Erreur lors de la création du répartoire " + pathStr);
            }
        }
    }
}

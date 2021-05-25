import controleur.Controleur;
import global.Configuration;
import vue.InterfaceGraphique;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        createRequiredFolder(Configuration.instance().getConfig("logDir"));
        createRequiredFolder("saves");
        InterfaceGraphique.demarrer(new Controleur());
    }

    private static void createRequiredFolder(String pathStr) {
        Path path = Paths.get(pathStr);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            try {
                Files.createDirectories(path);
            } catch (Exception e) {
                System.err.println("Erreur lors de la création du répartoire " + pathStr);
            }
        }
    }
}

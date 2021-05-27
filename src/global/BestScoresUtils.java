package global;

import global.writer.ObjectSaver;
import modele.pion.Pion;
import modele.pion.TypePion;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BestScoresUtils implements Serializable {
    private static BestScoresUtils instance = null;

    private Map<String, Integer> scores;

    public BestScoresUtils() {
        ObjectInputStream reader = null;
        try {
            reader = new ObjectInputStream(new FileInputStream("data/bestplayers.dat"));
            scores = (Map<String, Integer>) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            scores = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        }
    }

    public static BestScoresUtils instance() {
        if (instance == null) {
            instance = new BestScoresUtils();
        }
        return instance;
    }

    public void addVictory(String name) {
        scores.compute(name, (n, c) -> c == null ? 1 : c + 1);
        writeIntoFile();
    }

    private void writeIntoFile() {
        BufferedOutputStream output;
        try {
            String filename = "data" + File.separator + "bestplayers.dat";
            FileOutputStream out = new FileOutputStream(filename);
            output = new BufferedOutputStream(out);

            ObjectOutputStream objectStream = new ObjectOutputStream(output);
            objectStream.writeObject(scores);

            output.flush();
            output.close();
        } catch (Exception ignored) {};
    }

    public Map<String, Integer> getScores() {
        return scores;
    }
}

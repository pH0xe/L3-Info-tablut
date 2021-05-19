package global;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Configuration {
    private static Configuration instance = null;
    private Properties properties;
    private Logger logger;

    public static InputStream charger(String nom) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(nom);
    }

    private Configuration() {
        properties = new Properties();
        try {
            InputStream in = charger("default.cfg");
            properties.load(in);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargment du fichier config");
        }

        setLogger();
    }

    private void setLogger() {
        logger = Logger.getLogger("tablut.logger");
        String logLevel = getConfig("logLevel");
        logger.setLevel(Level.parse(logLevel));
        if (logLevel.equals("OFF")) return;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String logName = dtf.format(now)+".log";
        try {
            FileHandler fh = new FileHandler(getConfig("logDir") + "/" + logName);
            fh.setFormatter(new LogFormatter());
            logger.setUseParentHandlers(false);
            logger.addHandler(fh);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du fichier de log, utilisation du logger par defaut");
        }
    }

    public static Configuration instance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public String getConfig(String key) {
        String res = properties.getProperty(key);
        if (res == null) {
            throw new NoSuchElementException("Config \"" + key +"\" introuvable.");
        }
        return res;
    }

    public Logger logger() {
        return logger;
    }
}

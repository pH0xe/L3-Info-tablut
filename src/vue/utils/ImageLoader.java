package vue.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

public class ImageLoader {
    public static Image chargerImage(String path) {
        Image img = null;
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
            if (in != null)
                img = ImageIO.read(in);
            else {
                System.out.println("Impossible de charger l'image: " + path);
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Impossible de charger l'image: " + path);
            System.exit(1);
        }
        return img;
    }
}

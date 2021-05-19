package vue.customComponent;

import javax.swing.*;
import java.awt.*;

public class ButtonIcon extends JButton {
    public ButtonIcon() {
        super();
    }

    @Override
    public Icon getIcon() {
        ImageIcon ic = (ImageIcon) super.getIcon();
        Image img = ic.getImage();
        int size = getHeight() /2;
        Image img2 = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img2);
    }
}

package vue.customComponent;

import vue.utils.Constants;

import javax.swing.*;
import java.awt.*;

public class CustomTextField extends JTextField {
    private final String placeholder;

    public CustomTextField(String placeholder, String name) {
        super();
        setFont(Constants.DEFAULT_FONT);
        setBackground(Constants.TEXTFIELD_BACKGROUND);
        setForeground(Constants.TEXTFIELD_FOREGROUND);
        setOpaque(false);
        this.placeholder = placeholder;
        setName(name);
    }

    public CustomTextField(String name) {
        super();
        setFont(Constants.DEFAULT_FONT);
        setBackground(Constants.TEXTFIELD_BACKGROUND);
        setForeground(Constants.TEXTFIELD_FOREGROUND);
        setOpaque(false);
        this.placeholder = "";
        setName(name);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D drawable = (Graphics2D) g;
        drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!isEnabled())
            g.setColor(Constants.TEXTFIELD_DISABLE);
        else
            g.setColor(getBackground());

        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        if(getText().isEmpty()){
            drawable.setColor(Color.gray);
            drawable.setFont(Constants.ITALIC_FONT);
            g.drawString(placeholder, getInsets().left, getHeight()/2 + drawable.getFontMetrics().getHeight()/3);
        }



        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D drawable = (Graphics2D) g;
        drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
    }
}

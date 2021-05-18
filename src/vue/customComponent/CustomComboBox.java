package vue.customComponent;

import vue.utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.*;
import java.awt.*;

public class CustomComboBox extends JComboBox{
    public CustomComboBox(String[] options, String name) {
        super(options);
        setUI(new ComboBoxStyle());
        setName(name);
    }


    private static class ComboBoxStyle extends BasicComboBoxUI {

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            JComboBox box = (JComboBox) c;
            box.setRenderer(new CustomRenderer());
            box.setEditable(false);
            box.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
            box.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected JButton createArrowButton() {
            final JButton button = new ArrowButtonCustom(BasicArrowButton.SOUTH, Constants.COMBOBOX_BACKGROUND, Constants.COMBOBOX_BACKGROUND, Constants.COMBOBOX_FOREGROUND, Constants.COMBOBOX_BACKGROUND);
            return button;
        }
    }

    private static class CustomRenderer extends JLabel implements ListCellRenderer {
        private Color bgColor, fgColor;
        public CustomRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setBackground(Constants.COMBOBOX_BACKGROUND);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            this.setText(value.toString());

            if (isSelected) {
                setBackground(Constants.COMBOBOX_BACKGROUND.brighter());
                setForeground(Constants.COMBOBOX_FOREGROUND.brighter());
                bgColor = Constants.COMBOBOX_BACKGROUND.brighter();
                fgColor = Constants.COMBOBOX_FOREGROUND.brighter();
            } else {
                setBackground(Constants.COMBOBOX_BACKGROUND);
                setForeground(Constants.COMBOBOX_FOREGROUND);
                bgColor = Constants.COMBOBOX_BACKGROUND;
                fgColor = Constants.COMBOBOX_FOREGROUND;
            }
            if (index != -1) setBorder(BorderFactory.createMatteBorder(1,0,0,0, Constants.COMBOBOX_SEPARATOR));
            else setBorder(BorderFactory.createEmptyBorder());

            return this;
        }

        @Override
        public Color getBackground() {
            return bgColor;
        }

        @Override
        public Color getForeground() {
            return fgColor;
        }
    }

    private static class ArrowButtonCustom extends BasicArrowButton {

        public ArrowButtonCustom(int direction, Color background, Color shadow, Color darkShadow, Color highlight) {
            super(direction, background, shadow, darkShadow, highlight);
        }

        @Override
        public void paint(Graphics g) {
            g.fillRect(0,0, getWidth(), getHeight());
            super.paintTriangle(g, getWidth()/3, getHeight()/3, getWidth()/3, BasicArrowButton.SOUTH, true);
        }
    }
}

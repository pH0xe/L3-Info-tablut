package vue.customComponent;

import vue.utils.Constants;
import vue.utils.ConstraintBuilder;

import javax.swing.*;
import java.awt.*;

public class formPanel extends JPanel {

    public formPanel(String[] label, JComponent[] component) {
        setLayout(new GridBagLayout());
        setOpaque(false);
        ConstraintBuilder cbLabel = new ConstraintBuilder(0,0).setWeighty(1).setWeightx(0.3).fillBoth().setInset(0,0,0,5).setIpady(15);
        ConstraintBuilder cbComp = new ConstraintBuilder(1,0).setWeighty(1).setWeightx(0.7).fillBoth().setInset(5,5,10,5).setIpady(15);

        for (int i = 0; i < label.length; i++) {
            JLabel l = new JLabel(label[i], JLabel.TRAILING);
            l.setFont(Constants.DEFAULT_FONT);

            if (i == label.length-1) {
                cbComp.setInset(35,5,10,5);
            } else {
                cbLabel.setGridy(i);
                add(l, cbLabel.toConstraints());
            }

            cbComp.setGridy(i);
            add(component[i],cbComp.toConstraints());
        }

    }
}

package vue.panels.saves;

import controleur.CollecteurEvenements;
import global.DateUtils;
import vue.adapters.mouseAdapters.SauvegardeAdapteur;
import vue.customComponent.ButtonBuilder;
import vue.utils.Constants;
import vue.utils.ConstraintBuilder;
import vue.utils.Images;

import javax.swing.*;
import java.awt.*;

public class SavesButton extends JPanel {
    private final CollecteurEvenements controleur;

    public SavesButton(CollecteurEvenements controleur, String filename) {
        this.controleur = controleur;
        setOpaque(false);
        setLayout(new GridBagLayout());
        ConstraintBuilder bc = new ConstraintBuilder(0,0).setWeightx(1).setWeighty(1).fillBoth().setInset(5,0,5,10);

        JButton button = new ButtonBuilder().setBackground(Color.DARK_GRAY).setForeground(Color.WHITE).setText(DateUtils.getFormattedString(filename)).toFlatJButton();
        button.addMouseListener(new SauvegardeAdapteur(controleur, SauvegardeAdapteur.LOAD, filename));
        add(button, bc.toConstraints());

        JButton btnClose = new ButtonBuilder()
                .setBackground(Constants.BUTTON_BACK_BACKGROUND)
                .setIcon(Images.CLOSE)
                .toIconJButton();
        btnClose.addMouseListener(new SauvegardeAdapteur(controleur, SauvegardeAdapteur.DELETE, filename));
        bc.incrGridx().setWeighty(0.2).setInset(5,0,5,0);
        add(btnClose, bc.toConstraints());
    }
}

package vue.utils;

import java.awt.*;

public class ConstraintBuilder {

    private final GridBagConstraints constraints;

    public ConstraintBuilder(int gridx, int gridy) {
        constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
    }

    public ConstraintBuilder setWeightx(final double weightx) {
        constraints.weightx = weightx;
        return this;
    }

    public ConstraintBuilder setWeighty(final double weighty) {
        constraints.weighty = weighty;
        return this;
    }

    public ConstraintBuilder setGridx(final int gridx) {
        constraints.gridx = gridx;
        return this;
    }

    public ConstraintBuilder setGridy(final int gridy) {
        constraints.gridy = gridy;
        return this;
    }

    public ConstraintBuilder incrGridy(){
        constraints.gridy += 1;
        return this;
    }

    public ConstraintBuilder incrGridx(){
        constraints.gridx += 1;
        return this;
    }

    public ConstraintBuilder fillHorizontal() {
        constraints.fill = GridBagConstraints.HORIZONTAL;
        return this;
    }

    public ConstraintBuilder fillVertical() {
        constraints.fill = GridBagConstraints.VERTICAL;
        return this;
    }

    public ConstraintBuilder fillBoth() {
        constraints.fill = GridBagConstraints.BOTH;
        return this;
    }

    public ConstraintBuilder setInset(int top, int right, int bottom, int left) {
        constraints.insets = new Insets(top, right, bottom, left);
        return this;
    }

    public ConstraintBuilder setGridWidth(int gridwidth) {
        constraints.gridwidth = gridwidth;
        return this;
    }

    public ConstraintBuilder setIpady(int ipady) {
        constraints.ipady = ipady;
        return this;
    }

    public ConstraintBuilder setAnchor(int anchor) {
        constraints.anchor = anchor;
        return this;
    }

    public GridBagConstraints toConstraints() {
        return constraints;
    }

}

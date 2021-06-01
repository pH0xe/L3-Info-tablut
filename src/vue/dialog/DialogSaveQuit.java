package vue.dialog;

import controleur.CollecteurEvenements;

import javax.swing.*;

public class DialogSaveQuit {
    private CollecteurEvenements controleur;
    public static final int QUIT_AFTER = 0;
    public static final int MAIN_AFTER = 1;

    public DialogSaveQuit(CollecteurEvenements controleur) {
        this.controleur = controleur;
    }

    public void showMessage(int afterAction, JComponent relativeTo) {
        int res = JOptionPane.showConfirmDialog(
                relativeTo,
                "Sauvegarder la partie avant de quitter ?\nSi vous cliquer sur \"non\" la progression sera definitivement perdu",
                "Confirmation",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            onClickYes(afterAction);
        } else if (res == JOptionPane.NO_OPTION) {
            onClickNo(afterAction);
        }
    }

    private void onClickYes(int afterAction) {
        if (afterAction == QUIT_AFTER)
            controleur.sauvegarderQuitter();
        else if (afterAction == MAIN_AFTER)
            controleur.sauvegarderAccueil();
    }

    private void onClickNo(int afterAction) {
        if (afterAction == QUIT_AFTER)
            controleur.fermerApp();
        else if (afterAction == MAIN_AFTER)
            controleur.retourAccueil();
    }
}

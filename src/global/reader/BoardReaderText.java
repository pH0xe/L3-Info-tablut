package global.reader;

import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Point;

import java.io.InputStream;
import java.util.Scanner;

public class BoardReaderText extends BoardReader{
    private Scanner scanner;

    public BoardReaderText(InputStream in) {
        scanner = new Scanner(in);
    }

    @Override
    public void lirePlateau() {
        String ligne;
        for (int l = 0; l < 9; l++) {
            ligne = scanner.nextLine();
            for (int c = 0; c < 9; c++) {
                switch (ligne.charAt(c)){
                    case 'N':
                        insertNoirs(new Pion(TypePion.NOIR, new Point(l,c)));
                        break;
                    case 'B':
                        insertBlancs(new Pion(TypePion.BLANC, new Point(l,c)));
                        break;
                    case 'R':
                        setRoi(new Pion(TypePion.ROI, new Point(l,c)));
                        break;
                    default:
                        break;
                }
            }
        }
    }
}

package controleur.IA;

import modele.ConfigJeu;
import modele.Jeu;
import modele.Joueur.Couleur;
import modele.Plateau;
import modele.pion.Pion;
import modele.pion.TypePion;
import modele.util.Coup;

import java.time.Instant;
import java.util.*;

public abstract class IAMiniMax extends IA{
    public final int MAX = 1000000;
    public final int MIN = -1000000;

    Map<ConfigJeu, List<Coup>> returnVal;
    Set<ConfigJeu> mem;
    Instant maintenant;
    Coup dernierCoupJoue;
    int prof;

    @Override
    public abstract Coup iaJoue(Jeu j);

    public abstract int heuristique(Jeu j, int profondeur);

    public int Minimax(Jeu j, Couleur couleur, int profondeur, List<Coup> prec, int alpha, int beta) {
        ConfigJeu cj = new ConfigJeu(couleur, j, profondeur);
        int borne = couleur.equals(Couleur.BLANC) ? MIN : MAX;
        if(Instant.now().compareTo(maintenant.plusSeconds(10)) < 0 ){
            if (profondeur == 0 || j.roiSorti() || j.roiCapture()) {

                return heuristique(j, profondeur);
            }

                List<Coup> C = j.getListeCoups();
                Coup meilleur = null;
                Random r = new Random();
                List<Coup> coups = new ArrayList<>();
                for (Coup cp : C) {
                    prec.add(cp);
                    int dL = cp.getPion().getPosition().getL();
                    int dC = cp.getPion().getPosition().getC();
                    if (couleur.equals(Couleur.BLANC)) {
                        borne = Minimax(j.joueCoupDuplique(cp), Couleur.NOIR, profondeur - 1, prec, alpha, beta);
                        if (borne > alpha) {
                            alpha = borne;
                            if(profondeur == prof && dernierCoupJoue != null && cp !=dernierCoupJoue )
                                meilleur = cp;
                                coups.clear();
                                coups.add(cp);
                        } else if (borne == alpha) {
                            coups.add(cp);
                            meilleur = cp;
                        }
                        if (alpha >= beta) {
                            j.annulerCoup(prec, dL, dC);
                            break;
                        }
                    } else {
                        Jeu j2 = j.joueCoupDuplique(cp);
                        borne = Minimax(j2, Couleur.BLANC, profondeur - 1, prec, alpha, beta);
                        if (borne < beta) {
                            beta = borne;

                            if(profondeur == prof && dernierCoupJoue != null && cp !=dernierCoupJoue)
                                meilleur = cp;
                                coups.clear();
                                coups.add(cp);
                        } else if (borne == beta) {
                            if (r.nextBoolean()) {
                                meilleur = cp;
                                coups.add(cp);
                            }
                        }
                        if (beta <= alpha) {
                            j.annulerCoup(prec, dL, dC);
                            break;
                        }
                    }
                    j.annulerCoup(prec, dL, dC);
                }
                List<Coup> copy = new ArrayList<>(coups);
                returnVal.put(cj, copy);
                coups.clear();
                return borne;
        }

        return heuristique(cj.getJeu(), profondeur);
    }
}

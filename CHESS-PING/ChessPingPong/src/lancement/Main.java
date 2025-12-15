package lancement;

import javax.swing.SwingUtilities;
import interface_.fenetres.FenetrePrincipale;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fen = new FenetrePrincipale();
            fen.afficher();
        });
    }
}

package jeu.controle;

import jeu.modele.Plateau;

public class GestionnaireFin {
	public static boolean partieTerminee(Plateau plateau) {
		// la partie se termine si un roi est retiré
		boolean roiBlanc = false, roiNoir = false;
		for (int y = 0; y < 8; y++) for (int x = 0; x < 8; x++) {
			var p = plateau.getPieceAt(x, y);
			if (p != null && p.getNom().equals("Roi")) {
				if (p.getCouleur() == jeu.modele.Piece.Couleur.BLANC) roiBlanc = true;
				if (p.getCouleur() == jeu.modele.Piece.Couleur.NOIR) roiNoir = true;
			}
		}
		return !(roiBlanc && roiNoir);
	}
}


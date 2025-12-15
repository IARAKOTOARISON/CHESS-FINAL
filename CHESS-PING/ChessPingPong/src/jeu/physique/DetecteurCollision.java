package jeu.physique;

import java.util.List;

import jeu.modele.Plateau;

/**
 * Détecte collision entre trajectoire et pièces sur le plateau.
 */
public class DetecteurCollision {
	/**
	 * Parcourt les points de la trajectoire et retourne la première case (x,y)
	 * contenant une pièce, ou null si aucune collision.
	 */
	public static int[] detecter(List<CalculateurTrajectoire.Point> pts, Plateau plateau) {
		for (CalculateurTrajectoire.Point p : pts) {
			int xi = (int)Math.round(p.x);
			int yi = (int)Math.round(p.y);
			if (!plateau.inBounds(xi, yi)) continue;
			if (plateau.getPieceAt(xi, yi) != null) {
				return new int[]{xi, yi};
			}
		}
		return null;
	}
}


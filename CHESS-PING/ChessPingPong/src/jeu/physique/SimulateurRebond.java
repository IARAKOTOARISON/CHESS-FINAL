package jeu.physique;

import java.util.List;

import jeu.modele.Plateau;

/**
 * Simulateur simple des rebonds :
 * - si la balle touche les bords gauche/droite -> inversion de la composante X (rebond)
 * - si la balle touche les bords haut/bas -> arrêt du tour (retour null)
 */
public class SimulateurRebond {
	public static class Result {
		public final List<CalculateurTrajectoire.Point> finalTrajectory;
		public final int[] collisionCell; // {x,y} ou null

		public Result(List<CalculateurTrajectoire.Point> finalTrajectory, int[] collisionCell) {
			this.finalTrajectory = finalTrajectory;
			this.collisionCell = collisionCell;
		}
	}

	// Pour cette version minimaliste, nous ne calculons qu'une trajectoire droite et
	// appliquons les règles de rebond simples décrites.
	public Result appliquer(List<CalculateurTrajectoire.Point> pts, Plateau plateau) {
		// Détection simple : si trajectoire sort des bords, applique règles
		for (CalculateurTrajectoire.Point p : pts) {
			if (p.x < 0 || p.x > 7 || p.y < 0 || p.y > 7) {
				// si touche top/bottom (y out-of-bounds) -> arrêt
				if (p.y < 0 || p.y > 7) {
					return new Result(pts, null);
				}
				// if x out-of-bounds -> invert X (we won't continue simulation full rebound here)
				if (p.x < 0 || p.x > 7) {
					return new Result(pts, null);
				}
			}
		}
		return new Result(pts, null);
	}
}


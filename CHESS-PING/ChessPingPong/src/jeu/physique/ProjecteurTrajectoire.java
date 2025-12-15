package jeu.physique;

import java.util.List;

/**
 * Fournit une vue (liste de points) pour la preview de trajectoire.
 */
public class ProjecteurTrajectoire {
	private CalculateurTrajectoire calc = new CalculateurTrajectoire();

	public List<CalculateurTrajectoire.Point> preview(double x, double y, int puissance, double angleDeg) {
		return calc.calculer(x, y, puissance, angleDeg);
	}
}


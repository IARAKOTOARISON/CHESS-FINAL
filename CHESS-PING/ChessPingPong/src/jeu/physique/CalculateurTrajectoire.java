package jeu.physique;

import java.util.ArrayList;
import java.util.List;

import jeu.modele.ConfigurationJeu;
import jeu.modele.Plateau;

/**
 * Calcule une trajectoire linéaire (liste de points) pour une balle.
 */
public class CalculateurTrajectoire {
	public static class Point {
		public final double x, y;
		public Point(double x, double y) { this.x = x; this.y = y; }
	}

	/**
	 * Retourne une liste de points représentant la trajectoire jusqu'à la
	 * distance déterminée par la puissance. Ici la conversion est 1:1 pour la simplicité.
	 */
	public List<Point> calculer(double startX, double startY, int puissance, double angleDeg) {
		double angleRad = Math.toRadians(angleDeg);
		double dx = Math.cos(angleRad);
		double dy = Math.sin(angleRad);

		double distance = puissance * ConfigurationJeu.PIXELS_PAR_UNITE;
		int steps = Math.max(1, (int)Math.ceil(distance));

		List<Point> pts = new ArrayList<>();
		for (int i = 1; i <= steps; i++) {
			double x = startX + dx * i;
			double y = startY + dy * i;
			pts.add(new Point(x, y));
		}
		return pts;
	}
}


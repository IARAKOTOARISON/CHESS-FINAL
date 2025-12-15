package jeu.modele;

/**
 * Représente la balle lancée par les joueurs.
 */
public class Balle {
	private double x;
	private double y;
	private int puissance; // 1-100
	private double angleDeg; // 0-360

	public Balle(double x, double y, int puissance, double angleDeg) {
		this.x = x;
		this.y = y;
		this.puissance = puissance;
		this.angleDeg = angleDeg;
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public int getPuissance() { return puissance; }
	public double getAngleDeg() { return angleDeg; }
}


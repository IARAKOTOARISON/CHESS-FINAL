package jeu.modele;

/**
 * Classe de base pour les pièces (roi, dame, tour, pion).
 */
public abstract class Piece {
	public enum Couleur { BLANC, NOIR }

	protected String nom;
	protected String image; // chemin vers l'image (peut rester null)
	protected int vie;
	protected int startX;
	protected int startY;
	protected Couleur couleur;

	public Piece(String nom, int vie, int startX, int startY, Couleur couleur) {
		this.nom = nom;
		this.vie = vie;
		this.startX = startX;
		this.startY = startY;
		this.couleur = couleur;
	}

	public String getNom() { return nom; }
	public int getVie() { return vie; }
	public void setVie(int v) { vie = v; }
	public int getStartX() { return startX; }
	public int getStartY() { return startY; }
	public Couleur getCouleur() { return couleur; }

	public boolean isDead() { return vie <= 0; }
}

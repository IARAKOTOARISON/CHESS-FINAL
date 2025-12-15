package jeu.modele;

public class Partie {
	private Plateau plateau;
	private Piece.Couleur joueurActif;
	// Nombre de vies par joueur (fonctionnalité UI)
	private int viesBlanc;
	private int viesNoir;

	public Partie() {
		this.plateau = new Plateau();
		this.joueurActif = Piece.Couleur.BLANC;
		// Valeurs par défaut (peuvent être adaptées plus tard via configuration)
		this.viesBlanc = 3;
		this.viesNoir = 3;
	}

	public Plateau getPlateau() { return plateau; }
	public Piece.Couleur getJoueurActif() { return joueurActif; }

	public void switchJoueur() {
		joueurActif = (joueurActif == Piece.Couleur.BLANC) ? Piece.Couleur.NOIR : Piece.Couleur.BLANC;
	}

	// Getters pour l'interface
	public int getViesBlanc() { return viesBlanc; }
	public int getViesNoir() { return viesNoir; }

	// Méthodes utilitaires basiques pour gestion de vies
	public void setViesBlanc(int v) { this.viesBlanc = Math.max(0, v); }
	public void setViesNoir(int v) { this.viesNoir = Math.max(0, v); }

	public void decrementerVie(Piece.Couleur couleur) {
		if (couleur == Piece.Couleur.BLANC) {
			viesBlanc = Math.max(0, viesBlanc - 1);
		} else {
			viesNoir = Math.max(0, viesNoir - 1);
		}
	}

	public void reinitialiserVies(int viesInitiales) {
		this.viesBlanc = Math.max(0, viesInitiales);
		this.viesNoir = Math.max(0, viesInitiales);
	}
}


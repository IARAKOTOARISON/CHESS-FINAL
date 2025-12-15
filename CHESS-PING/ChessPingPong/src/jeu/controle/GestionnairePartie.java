package jeu.controle;

import jeu.modele.Balle;
import jeu.modele.Partie;
import jeu.modele.Plateau;
import jeu.physique.CalculateurTrajectoire;
import jeu.physique.DetecteurCollision;

/**
 * Orchestration d'un tour simple : calculer trajectoire, détecter collision et appliquer dégâts.
 */
public class GestionnairePartie {
	private Partie partie;

	public GestionnairePartie(Partie p) { this.partie = p; }

	/**
	 * Lance un tour : retourne un message de résultat pour affichage.
	 */
	public String lancerTour(Balle balle) {
		Plateau plateau = partie.getPlateau();
		CalculateurTrajectoire calc = new CalculateurTrajectoire();
		var pts = calc.calculer(balle.getX(), balle.getY(), balle.getPuissance(), balle.getAngleDeg());
		int[] collision = DetecteurCollision.detecter(pts, plateau);
		if (collision != null) {
			plateau.damagePieceAt(collision[0], collision[1], 1);
			return "Collision en (" + collision[0] + "," + collision[1] + ") - pièce endommagée";
		}
		return "Aucune collision";
	}
}


package jeu.controle;

import jeu.modele.Balle;
import jeu.modele.Partie;

public class ControleurTour {
	private Partie partie;

	public ControleurTour(Partie p) { this.partie = p; }

	public boolean validerEtLancer(Balle balle) {
		// placeholder: validation should happen elsewhere
		return true;
	}
}


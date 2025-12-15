package jeu.controle;

import jeu.modele.Plateau;

public class ValidateurMouvement {
	public static boolean positionDansZoneAutorisee(Plateau plateau, double x, double y, boolean joueurBlanc) {
		// Règle simple : joueur blanc place dans les rangs 0-3 (y <= 3), noir dans 4-7 (y >=4)
		if (joueurBlanc) return y >= 0 && y <= 3;
		else return y >= 4 && y <= 7;
	}
}


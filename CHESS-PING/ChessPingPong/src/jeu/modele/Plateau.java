package jeu.modele;

// (imports inutilisés supprimés)

/**
 * Plateau 8x8 avec placement initial des pièces (simplifié).
 */
public class Plateau {
	private Piece[][] board = new Piece[8][8];

	public Plateau() {
		placeInitialPieces();
	}

	private void placeInitialPieces() {
		// Place pawns
		for (int x = 0; x < 8; x++) {
			board[1][x] = new Pion(x, 1, Piece.Couleur.BLANC);
			board[6][x] = new Pion(x, 6, Piece.Couleur.NOIR);
		}

		// Place rooks
		board[0][0] = new Tour(0, 0, Piece.Couleur.BLANC);
		board[0][7] = new Tour(7, 0, Piece.Couleur.BLANC);
		board[7][0] = new Tour(0, 7, Piece.Couleur.NOIR);
		board[7][7] = new Tour(7, 7, Piece.Couleur.NOIR);

		// Place kings and queens (simple)
		board[0][4] = new Roi(4, 0, Piece.Couleur.BLANC);
		board[0][3] = new Dame(3, 0, Piece.Couleur.BLANC);
		board[7][4] = new Roi(4, 7, Piece.Couleur.NOIR);
		board[7][3] = new Dame(3, 7, Piece.Couleur.NOIR);
	}

	public Piece getPieceAt(int x, int y) {
		if (inBounds(x, y)) return board[y][x];
		return null;
	}

	public void removePieceAt(int x, int y) {
		if (inBounds(x, y)) board[y][x] = null;
	}

	public void damagePieceAt(int x, int y, int dmg) {
		Piece p = getPieceAt(x, y);
		if (p != null) {
			p.setVie(p.getVie() - dmg);
			if (p.isDead()) removePieceAt(x, y);
		}
	}

	public boolean inBounds(int x, int y) {
		return x >= 0 && x < 8 && y >= 0 && y < 8;
	}

	// For debug / console
	public void printBoard() {
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < 8; x++) {
				Piece p = board[y][x];
				System.out.print((p == null ? "." : p.getNom().charAt(0)) + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Retourne la liste des pièces présentes sur le plateau pour une couleur donnée.
	 * L'ordre est arbitraire (balayage ligne par ligne).
	 */
	public java.util.List<Piece> getPieces(Piece.Couleur couleur) {
		java.util.List<Piece> res = new java.util.ArrayList<>();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Piece p = board[y][x];
				if (p != null && p.getCouleur() == couleur) {
					res.add(p);
				}
			}
		}
		return res;
	}
}

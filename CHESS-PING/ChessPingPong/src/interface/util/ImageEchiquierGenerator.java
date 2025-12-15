package interface_.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import jeu.modele.Plateau;
import jeu.modele.Piece;

/**
 * Génère une image PNG de l'échiquier avec la position initiale des pièces.
 */
class ImageEchiquierGeneratorOld {
    private static final int CELL_SIZE = 64; // pixels par case
    private static final int BOARD_SIZE = 8;
    private static final int MARGIN = 24;    // marge autour du plateau
    private static final Color LIGHT = new Color(240, 217, 181);
    private static final Color DARK = new Color(181, 136, 99);

    /**
     * Génère et enregistre une image du plateau.
     * @param outputPath chemin du fichier de sortie (PNG)
     */
    public static void generateOld(String outputPath) throws IOException {
        int width = MARGIN * 2 + BOARD_SIZE * CELL_SIZE;
        int height = MARGIN * 2 + BOARD_SIZE * CELL_SIZE;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fond
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Dessin des cases
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                boolean dark = (x + y) % 2 == 1;
                g.setColor(dark ? DARK : LIGHT);
                g.fillRect(MARGIN + x * CELL_SIZE, MARGIN + y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // Bordures
        g.setColor(new Color(90, 60, 30));
        g.setStroke(new BasicStroke(2f));
        g.drawRect(MARGIN, MARGIN, BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE);

        // Indices (a-h, 1-8)
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (int x = 0; x < BOARD_SIZE; x++) {
            char fileChar = (char) ('a' + x);
            g.drawString(String.valueOf(fileChar), MARGIN + x * CELL_SIZE + CELL_SIZE / 2 - 4, height - 6);
        }
        for (int y = 0; y < BOARD_SIZE; y++) {
            String rank = String.valueOf(BOARD_SIZE - y);
            g.drawString(rank, 6, MARGIN + y * CELL_SIZE + CELL_SIZE / 2 + 4);
        }

        // Charger et dessiner les pièces basées sur le plateau initial
        Plateau plateau = new Plateau();
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                Piece p = plateau.getPieceAt(x, y);
                if (p == null) continue;
                Image imgPiece = loadPieceImage(p);
                if (imgPiece != null) {
                    int px = MARGIN + x * CELL_SIZE;
                    int py = MARGIN + y * CELL_SIZE;
                    g.drawImage(imgPiece, px + 4, py + 4, CELL_SIZE - 8, CELL_SIZE - 8, null);
                } else {
                    // Fallback: dessiner un cercle avec une lettre
                    g.setColor(p.getCouleur() == Piece.Couleur.BLANC ? new Color(230, 230, 255) : new Color(60, 60, 100));
                    g.fillOval(MARGIN + x * CELL_SIZE + 8, MARGIN + y * CELL_SIZE + 8, CELL_SIZE - 16, CELL_SIZE - 16);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("SansSerif", Font.BOLD, 18));
                    String label = p.getNom().substring(0, 1).toUpperCase();
                    g.drawString(label, MARGIN + x * CELL_SIZE + CELL_SIZE / 2 - 6, MARGIN + y * CELL_SIZE + CELL_SIZE / 2 + 6);
                }
            }
        }

        g.dispose();

        // Assurer le dossier de sortie
        File out = new File(outputPath);
        out.getParentFile().mkdirs();
        ImageIO.write(img, "png", out);
    }

    private static Image loadPieceImage(Piece p) {
        String prefix = p.getCouleur() == Piece.Couleur.BLANC ? "B" : "N";
        String name;
        switch (p.getNom().toLowerCase()) {
            case "roi": name = "roi"; break;
            case "dame": name = "dame"; break;
            case "tour": name = "tour"; break;
            case "pion": name = "soldat"; break; // selon votre convention actuelle
            default: name = p.getNom().toLowerCase();
        }
        String path = "ressources/images/" + prefix + name + ".png";
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            return null; // on tombera sur le fallback
        }
    }

    // Petit main pour générer rapidement l'image
    public static void main(String[] args) {
        String output = args != null && args.length > 0 ? args[0] : "ressources/output/echiquier.png";
        try {
            generateOld(output);
            System.out.println("Image de l'échiquier générée: " + output);
        } catch (IOException e) {
            System.err.println("Erreur génération image: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

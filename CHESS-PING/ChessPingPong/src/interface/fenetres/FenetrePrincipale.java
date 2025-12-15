package interface_.fenetres;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import jeu.modele.Partie;
import jeu.modele.Balle;
import jeu.modele.Plateau;
import jeu.modele.Piece;
import jeu.physique.ProjecteurTrajectoire;
import jeu.physique.CalculateurTrajectoire;
import jeu.controle.GestionnairePartie;

/**
 * Fenêtre principale minimaliste:
 * - Image du damier
 * - Inputs: position (x,y), puissance, angle
 * - Boutons: Voir trajectoire, Lancer la balle
 * Sans dépendre des anciens JPanels personnalisés.
 */
class FenetrePrincipaleOld {
    private JFrame frame;
    private JTabbedPane tabs;

    // Tab 1: Jeu
    private JLabel labelBoard;
    private JTextField txtX, txtY, txtPuissance, txtAngle;
    private JButton btnPreview, btnValider;
    private JLabel lblJoueurActif;

    // Tab 2: Vies
    private JLabel lblViesBlanc;
    private JLabel lblViesNoir;
    private JLabel imgBlanc;
    private JLabel imgNoir;

    // Tab 3: Reset
    private JButton btnResetPartie;

    private Partie partie;
    private Plateau plateau;
    private ProjecteurTrajectoire projecteur;

    public FenetrePrincipaleOld() {
        this.partie = new Partie();
        this.plateau = partie.getPlateau();
        this.projecteur = new ProjecteurTrajectoire();

        initUI();
    }

    private void initUI() {
        frame = new JFrame("Chess Ping - Fenêtre");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        tabs = new JTabbedPane();
        tabs.addTab("Jeu", buildTabJeu());
        tabs.addTab("Vies", buildTabVies());
        tabs.addTab("Réinitialiser", buildTabReset());
        frame.add(tabs, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private JPanel buildTabJeu() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Haut: nom du joueur actif
        lblJoueurActif = new JLabel("Joueur actif: " + (partie.getJoueurActif() == Piece.Couleur.BLANC ? "BLANC" : "NOIR"));
        lblJoueurActif.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(lblJoueurActif, BorderLayout.NORTH);

        // Centre: gauche = échiquier ; droite = formulaire
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));

        // Damier
        labelBoard = new JLabel();
        labelBoard.setHorizontalAlignment(SwingConstants.CENTER);
        labelBoard.setVerticalAlignment(SwingConstants.CENTER);
        labelBoard.setOpaque(true);
        labelBoard.setBackground(Color.WHITE);
        JScrollPane boardScroll = new JScrollPane(labelBoard);
        center.add(boardScroll);

        // Formulaire
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Position X:"));
        txtX = new JTextField("3.5", 6); row1.add(txtX);
        row1.add(new JLabel("Y:"));
        txtY = new JTextField("2.0", 6); row1.add(txtY);
        form.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Direction (Angle°):"));
        txtAngle = new JTextField("45", 6); row2.add(txtAngle);
        form.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(new JLabel("Puissance:"));
        txtPuissance = new JTextField("50", 6); row3.add(txtPuissance);
        form.add(row3);

        center.add(form);
        panel.add(center, BorderLayout.CENTER);

        // Bas: boutons actions
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPreview = new JButton("Voir la trajectoire");
        btnValider = new JButton("Valider le mouvement");
        bottom.add(btnPreview);
        bottom.add(btnValider);
        panel.add(bottom, BorderLayout.SOUTH);

        // Listeners
        btnPreview.addActionListener(e -> voirTrajectoire());
        btnValider.addActionListener(e -> validerMouvement());

        // Charger le damier initial
        rafraichirDamier(null);

        return panel;
    }

    private JPanel buildTabVies() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel rowBlanc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imgBlanc = new JLabel(loadPlayerImage(true));
        lblViesBlanc = new JLabel("Vies BLANC: " + partie.getViesBlanc());
        rowBlanc.add(imgBlanc);
        rowBlanc.add(lblViesBlanc);

        JPanel rowNoir = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imgNoir = new JLabel(loadPlayerImage(false));
        lblViesNoir = new JLabel("Vies NOIR: " + partie.getViesNoir());
        rowNoir.add(imgNoir);
        rowNoir.add(lblViesNoir);

        panel.add(rowBlanc);
        panel.add(rowNoir);
        return panel;
    }

    private JPanel buildTabReset() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnResetPartie = new JButton("Recommencer la partie");
        btnResetPartie.addActionListener(e -> resetPartie());
        panel.add(btnResetPartie);
        return panel;
    }

    private void rafraichirDamier(java.util.List<CalculateurTrajectoire.Point> traj) {
        try {
            // Base: image générée (en amont) ou fallback depuis util
            File imgFile = new File("ressources/output/echiquier.png");
            BufferedImage base;
            if (imgFile.exists()) {
                base = ImageIO.read(imgFile);
            } else {
                // Fallback: générer à la volée via util si manquant
                interface_.util.ImageEchiquierGenerator.generate("ressources/output/echiquier.png");
                base = ImageIO.read(new File("ressources/output/echiquier.png"));
            }

            // Si trajectoire fournie, dessiner par-dessus
            if (traj != null && !traj.isEmpty()) {
                Graphics2D g2 = base.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 0, 0, 200));
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f, new float[]{10f,7f}, 0f));
                int cell = base.getWidth() / 8; // approximatif si marge; sinon calcul dédié
                for (int i = 0; i < traj.size() - 1; i++) {
                    var p1 = traj.get(i);
                    var p2 = traj.get(i + 1);
                    int x1 = (int)(p1.x * cell + cell / 2);
                    int y1 = (int)((7 - p1.y) * cell + cell / 2);
                    int x2 = (int)(p2.x * cell + cell / 2);
                    int y2 = (int)((7 - p2.y) * cell + cell / 2);
                    g2.drawLine(x1, y1, x2, y2);
                }
                g2.dispose();
            }

            labelBoard.setIcon(new ImageIcon(base));
        } catch (IOException ex) {
            labelBoard.setText("Impossible de charger le damier: " + ex.getMessage());
        }
    }

    private void voirTrajectoire() {
        try {
            double x = Double.parseDouble(txtX.getText());
            double y = Double.parseDouble(txtY.getText());
            int puissance = Integer.parseInt(txtPuissance.getText());
            double angle = Double.parseDouble(txtAngle.getText());

            var points = projecteur.preview(x, y, puissance, angle);
            rafraichirDamier(points);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Valeurs invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validerMouvement() {
        try {
            double x = Double.parseDouble(txtX.getText());
            double y = Double.parseDouble(txtY.getText());
            int puissance = Integer.parseInt(txtPuissance.getText());
            double angle = Double.parseDouble(txtAngle.getText());

            boolean joueurBlanc = partie.getJoueurActif() == Piece.Couleur.BLANC;
            if (joueurBlanc && (y < 2 || y > 3)) {
                JOptionPane.showMessageDialog(frame, "Joueur BLANC: placer Y=2 ou 3", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!joueurBlanc && (y < 4 || y > 5)) {
                JOptionPane.showMessageDialog(frame, "Joueur NOIR: placer Y=4 ou 5", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Balle balle = new Balle(x, y, puissance, angle);
            GestionnairePartie gp = new GestionnairePartie(partie);
            String res = gp.lancerTour(balle);
            JOptionPane.showMessageDialog(frame, res, "Résultat", JOptionPane.INFORMATION_MESSAGE);
            // Mise à jour des vies si GestionnairePartie les modifie
            rafraichirVies();

            partie.switchJoueur();
            updateJoueurActifLabel();
            rafraichirDamier(null);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Valeurs invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetPartie() {
        // Réinitialise la partie et l'affichage
        this.partie = new Partie();
        this.plateau = partie.getPlateau();
        updateJoueurActifLabel();
        rafraichirVies();
        rafraichirDamier(null);
        JOptionPane.showMessageDialog(frame, "La partie a été réinitialisée.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateJoueurActifLabel() {
        if (lblJoueurActif != null) {
            lblJoueurActif.setText("Joueur actif: " + (partie.getJoueurActif() == Piece.Couleur.BLANC ? "BLANC" : "NOIR"));
        }
    }

    private void rafraichirVies() {
        if (lblViesBlanc != null) {
            lblViesBlanc.setText("Vies BLANC: " + partie.getViesBlanc());
        }
        if (lblViesNoir != null) {
            lblViesNoir.setText("Vies NOIR: " + partie.getViesNoir());
        }
    }

    public void afficher() {
        frame.setVisible(true);
    }

    private Icon loadPlayerImage(boolean blanc) {
        String path = blanc ? "ressources/images/Bjoueur.png" : "ressources/images/Njoueur.png";
        try {
            Image img = ImageIO.read(new File(path));
            return new ImageIcon(img.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            return UIManager.getIcon("OptionPane.informationIcon");
        }
    }
}

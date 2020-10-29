package com.bobliou.chessgame.GUI;

import com.bobliou.chessgame.Game.Board;
import com.bobliou.chessgame.Game.Position;
import com.bobliou.chessgame.Pieces.Piece;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class GameData {

    public String whitePlayer = "";
    public String blackPlayer = "";
    public boolean isWhiteTurn = true; // White player starts
}

/**
 *
 * @author Bob Liou - 18013456
 */
public class ChessGUI extends JFrame {

    // Chess game engine
    Board board;
    GameData gameData;
    public static final int BOARD_LENGTH = Board.BOARD_LENGTH;
    Listeners listener;

    // GUI components
    JLayeredPane layeredPanel;
    JPanel boardPanel;
    JPanel piecePanel;
    JPanel highlightPanel;
    
    // Input Panel
    JPanel inputPanel;
    JLabel infoLabel;
    JTextField inputField;
    JButton moveButton;
    
    Map<String, ImageIcon> imageMap;

    public ChessGUI() {
        super("Chess Game");

        // Chess game engine
        board = new Board();
        gameData = new GameData();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadImages();
        setupLayeredPane();
        buildLayeredPanel();
        buildInputPanel();

        add(layeredPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }
    
    public void buildInputPanel() {
        inputPanel = new JPanel(new BorderLayout());
        
        infoLabel = new JLabel("White player's turn");
        inputPanel.add(infoLabel, BorderLayout.NORTH);
        
        inputField = new JTextField();
        inputPanel.add(inputField, BorderLayout.CENTER);
        
        moveButton = new JButton("Move");
        inputPanel.add(moveButton, BorderLayout.EAST);
    }

    public void buildLayeredPanel() {
//        board.generateAllPossibleMoves();

        // loop through the board
        for (int row = 0; row < BOARD_LENGTH; row++) {
            boolean isOddRow = (row + 1) % 2 == 1;
            for (int col = 0; col < BOARD_LENGTH; col++) {
                boolean isOddCol = (col + 1) % 2 == 1;

                Position currentPosition = board.getPosition(row, col);
                Piece currentPiece = currentPosition.getPiece() != null ? currentPosition.getPiece() : null;
                if (currentPiece != null) {
                    currentPiece.generatePossibleMoves(board, currentPosition);
                    String name = currentPiece.getName();
                    String colour = currentPiece.getIsWhite() ? "W" : "B";
                    JLabel label = new JLabel(imageMap.get(colour + name));
                    piecePanel.add(label);
                } else {
                    // blank square
                    JLabel label = new JLabel(imageMap.get("blank"));
                    piecePanel.add(label);
                }

                if (isOddRow == isOddCol) {
                    // is white square
                    JLabel label = new JLabel(imageMap.get("WhiteSquare"));
                    boardPanel.add(label);
                } else {
                    // is black square
                    JLabel label = new JLabel(imageMap.get("BlackSquare"));
                    boardPanel.add(label);
                }
            }
        }
    }

    public void setupLayeredPane() {
        boardPanel = new JPanel(new GridLayout(8, 8, 0, 0));
        boardPanel.setOpaque(false);
        boardPanel.setPreferredSize(new Dimension(640, 640));
        boardPanel.setBounds(0, 0, 640, 640);

        piecePanel = new JPanel(new GridLayout(8, 8, 0, 0));
        piecePanel.setOpaque(false);
        piecePanel.setPreferredSize(new Dimension(640, 640));
        piecePanel.setBounds(0, 0, 640, 640);

        layeredPanel = new JLayeredPane();
        layeredPanel.setPreferredSize(new Dimension(640, 640));
        layeredPanel.setVisible(true);

        layeredPanel.add(boardPanel, new Integer(0));
        layeredPanel.add(piecePanel, new Integer(1));
    }

    public void loadImages() {
        imageMap = new HashMap<>();

        imageMap.put("WKing", getImage("WK"));
        imageMap.put("WQueen", getImage("WQ"));
        imageMap.put("WRook", getImage("WR"));
        imageMap.put("WBishop", getImage("WB"));
        imageMap.put("WKnight", getImage("WN"));
        imageMap.put("WPawn", getImage("WP"));
        imageMap.put("BKing", getImage("BK"));
        imageMap.put("BQueen", getImage("BQ"));
        imageMap.put("BRook", getImage("BR"));
        imageMap.put("BBishop", getImage("BB"));
        imageMap.put("BKnight", getImage("BN"));
        imageMap.put("BPawn", getImage("BP"));
        imageMap.put("WhiteSquare", getImage("whiteSquare"));
        imageMap.put("BlackSquare", getImage("blackSquare"));
        imageMap.put("Highlight", getImage("highlight"));
        imageMap.put("Blank", getImage("blank"));
    }

    public ImageIcon getImage(String name) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("images/" + name + ".png"));
//            img = ImageIO.read(getClass().getResource(name + ".png"));

        } catch (IOException ex) {
            System.err.println("Error: " + ex);
        }
        ImageIcon imageIcon = new ImageIcon(img);
        return imageIcon;
    }

    public static void main(String[] args) {
        // GUI components
        JFrame frame = new ChessGUI();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
    }
}

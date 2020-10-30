package com.bobliou.chessgame.GUI;

import com.bobliou.chessgame.DB.DBOperations;
import com.bobliou.chessgame.Game.Board;
import com.bobliou.chessgame.Game.GameData;
import com.bobliou.chessgame.Game.GameEngine;
import com.bobliou.chessgame.Game.Position;
import com.bobliou.chessgame.Pieces.Piece;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.ComboBox;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * The main GUI for the project.
 *
 * @author Bob Liou - 18013456
 */
public class ChessGUI extends JFrame {

    // Chess game engine
    private Board board;
    private GameData gameData;
    private GameEngine gameEngine;
    public static final int BOARD_LENGTH = Board.BOARD_LENGTH;
    private String gameName;
    private DBOperations db;

    // GUI components
    private JPanel selectionPanel;
    private JPanel gamePanel;

    private JLayeredPane layeredPanel;
    private JPanel boardPanel;
    private JPanel piecePanel;
    private JPanel highlightPanel;
    private JMenuBar topBar;
    JComboBox comboBox;
    String gameNameArr[];

    // Input Panel
    private JPanel inputPanel;
    private JLabel infoLabel;
    private JTextField inputField;
    private JButton moveButton;

    private Map<String, ImageIcon> imageMap;

    public ChessGUI() {
        super("Chess Game");

        selectionPanel = new JPanel();
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        // Chess game engine
        board = new Board();
        gameData = new GameData();
        db = new DBOperations();
        db.establishConnection();
        gameEngine = new GameEngine(board, gameData, this, db);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadImages();
        setupMenu();
        setupSelectionPanel();
        setupLayeredPane();
        buildLayeredPanel();
        buildInputPanel();

        gamePanel.add(layeredPanel, BorderLayout.CENTER);
        gamePanel.add(inputPanel, BorderLayout.SOUTH);
        setJMenuBar(topBar);

        add(selectionPanel);

        pack();
        setVisible(true);
        setResizable(false);
    }

    /**
     * Swap to game panel
     * @param gameName The game name
     */
    private void swapToGamePanel(String gameName) {
        this.gameName = gameName;
        this.getContentPane().removeAll();
        add(gamePanel);
        pack();
        updateGUI();
    }

    /**
     * Swap to home panel
     */
    private void swapToHomePanel() {
        gameNameArr = db.getGameName();
        comboBox.removeAllItems();
        for (String name : gameNameArr) {
            comboBox.addItem(name);
        }
        this.getContentPane().removeAll();
        add(selectionPanel);
        pack();
        revalidate();
        repaint();
    }

    /**
     * Setup the selection panel
     */
    private void setupSelectionPanel() {

        JLabel welcomeLabel = new JLabel("Welcome to a new chess game!", SwingConstants.CENTER);
        welcomeLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        selectionPanel.add(welcomeLabel, BorderLayout.NORTH);

        JButton newGameButton = new JButton("Start a new game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameName = JOptionPane.showInputDialog("Please enter a name for this game:");
                if (gameName == null) {

                } else if (gameName.trim().equals("")) {
                    // empty
                    JOptionPane.showMessageDialog(null, "Game name cannot be empty. Try again.", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // add this game name to database
                    board = new Board();
                    gameData = new GameData();
                    gameEngine.reset(board, gameData);
                    db.addNewGame(gameName);
                    swapToGamePanel(gameName);
                }
            }
        });
        newGameButton.setSize(new Dimension(50, 10));
        selectionPanel.add(newGameButton);

        JPanel loadPanel = new JPanel(new BorderLayout());
        JLabel loadLabel = new JLabel("Load existing game", SwingConstants.CENTER);
        loadLabel.setBorder(new EmptyBorder(15, 0, 10, 0));
        loadPanel.add(loadLabel, BorderLayout.NORTH);

        gameNameArr = db.getGameName();
        comboBox = new JComboBox(gameNameArr);
        comboBox.setBorder(new EmptyBorder(5, 0, 10, 0));
        loadPanel.add(comboBox, BorderLayout.CENTER);

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new LoadGameListener());
        loadPanel.add(loadButton, BorderLayout.SOUTH);

        selectionPanel.add(loadPanel, BorderLayout.SOUTH);
        selectionPanel.setPreferredSize(new Dimension(225, 200));
    }

    /**
     * Load game listener to listener to selection.
     */
    private class LoadGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedGame = gameNameArr[comboBox.getSelectedIndex()];
            String history = db.getGameHistory(selectedGame);
            board = new Board();
            gameData = new GameData();
            gameEngine.reset(board, gameData);
            gameEngine.loadExistingGame(history);
            swapToGamePanel(selectedGame);
        }
    }

    /**
     * Reset the text of the textbox
     */
    public void resetTextBox() {
        inputField.setText("");
    }

    /**
     * Update the GUI (refresh)
     */
    public void updateGUI() {
        infoLabel.setText((gameData.isWhiteTurn ? "White" : "Black") + " player's turn");
        piecePanel.removeAll();
        boardPanel.removeAll();
        buildLayeredPanel();
        revalidate();
        repaint();
    }

    /**
     * Setup the menu bar
     */
    private void setupMenu() {
        topBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileMenuItemHome = new JMenuItem("Go To Home");
        fileMenu.add(fileMenuItemHome);
        fileMenuItemHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                swapToHomePanel();
            }
        });
        topBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpMenuItemGame = new JMenuItem("Show Help");
        helpMenu.add(helpMenuItemGame);
        helpMenuItemGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameEngine.displayHelp();
            }
        });
        topBar.add(helpMenu);
    }

    /**
     * Build the input panel
     */
    private void buildInputPanel() {
        inputPanel = new JPanel(new BorderLayout());

        infoLabel = new JLabel((gameData.isWhiteTurn ? "White" : "Black") + " player's turn");
        inputPanel.add(infoLabel, BorderLayout.NORTH);

        inputField = new JTextField();
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gameEngine.processMove(inputField.getText());
                }
            }
        });

        moveButton = new JButton("Move");
        inputPanel.add(moveButton, BorderLayout.EAST);
        moveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameEngine.processMove(inputField.getText());
            }
        });
    }

    /**
     * Build the layered panel
     */
    private void buildLayeredPanel() {
        // loop through the board
        for (int row = 0; row < BOARD_LENGTH; row++) {
            // first of the row is the number text
            piecePanel.add(new JLabel(imageMap.get(Integer.toString(8 - row))));
            boardPanel.add(new JLabel(imageMap.get("blank")));

            boolean isOddRow = (row + 1) % 2 == 1;
            JLabel pieceLabel;
            JLabel boardLabel;
            for (int col = 0; col < BOARD_LENGTH; col++) {
                boolean isOddCol = (col + 1) % 2 == 1;

                Position currentPosition = board.getPosition(row, col);
                Piece currentPiece = currentPosition.getPiece() != null ? currentPosition.getPiece() : null;
                if (currentPiece != null) {
                    currentPiece.generatePossibleMoves(board, currentPosition);
                    String name = currentPiece.getName();
                    String colour = currentPiece.getIsWhite() ? "W" : "B";
                    pieceLabel = new JLabel(imageMap.get(colour + name));
                    piecePanel.add(pieceLabel);
                } else {
                    // blank square
                    pieceLabel = new JLabel(imageMap.get("blank"));
                    piecePanel.add(pieceLabel);
                }

                if (isOddRow == isOddCol) {
                    // is white square
                    boardLabel = new JLabel(imageMap.get("WhiteSquare"));
                    boardPanel.add(boardLabel);
                } else {
                    // is black square
                    boardLabel = new JLabel(imageMap.get("BlackSquare"));
                    boardPanel.add(boardLabel);
                }
            }
        }

        // add last row's letter marker 
        for (int i = 0; i < 9; i++) {
            if (i == 0) {
                piecePanel.add(new JLabel(imageMap.get("blank")));
            } else {
                Character c = (char) (i + 64);
                String s = c.toString();
                piecePanel.add(new JLabel(imageMap.get(s)));
            }
            boardPanel.add(new JLabel(imageMap.get("blank")));
        }
    }

    /**
     * Setup the layered panel
     */
    private void setupLayeredPane() {
        boardPanel = new JPanel(new GridLayout(9, 9, 0, 0));
        boardPanel.setOpaque(false);
        boardPanel.setPreferredSize(new Dimension(720, 720));
        boardPanel.setBounds(0, 0, 720, 720);

        piecePanel = new JPanel(new GridLayout(9, 9, 0, 0));
        piecePanel.setOpaque(false);
        piecePanel.setPreferredSize(new Dimension(720, 720));
        piecePanel.setBounds(0, 0, 720, 720);

        layeredPanel = new JLayeredPane();
        layeredPanel.setPreferredSize(new Dimension(720, 720));
        layeredPanel.setVisible(true);

        layeredPanel.add(boardPanel, new Integer(0));
        layeredPanel.add(piecePanel, new Integer(1));
    }

    /**
     * Load the images
     */
    private void loadImages() {
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
        for (Integer i = 1; i <= 8; i++) {
            imageMap.put(i.toString(), getImage(i.toString()));
            Character c = (char) (i + 64);
            String s = c.toString();
            imageMap.put(s, getImage(s));
        }
    }

    /**
     * Get image given file name
     * @param name file name
     * @return ImageIcon object
     */
    private ImageIcon getImage(String name) {
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
    }

    /**
     * @return the gameName
     */
    public String getGameName() {
        return gameName;
    }
}

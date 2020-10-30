package com.bobliou.chessgame.Game;

import com.bobliou.chessgame.GUI.ChessGUI;
import com.bobliou.chessgame.Pieces.Pawn;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * The driver class of the program that stores the game data and get user's
 * input.
 *
 * @author Bob Liou - 18013456
 */
public class GameEngine {

    private Board board;
    private static GameData gameData;
    private ChessGUI chessGUI;
    private File fileObj;

    public GameEngine(Board board, GameData gameData, ChessGUI chessGUI) {
        this.board = board;
        this.gameData = gameData;
        this.chessGUI = chessGUI;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Check if the save file for Chess exist.
     *
     * @return True, if the save file exist. Otherwise, false.
     */
    private boolean fileExist() {
        File f = new File("savefile.txt");
        return f.exists() && !f.isDirectory();
    }

    /**
     * Write a given string to the save file using the append method.
     *
     * @param line The string to write to the save file.
     * @return True, if successfully. False otherwise.
     */
    private boolean writeLine(String line) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileObj, true));
            bw.write(line);
            bw.newLine();
            bw.close();
            return true;
        } catch (IOException ex) {
            return false; // Failed
        }
    }

    /**
     * Displays the help menu.
     */
    private void displayHelp() {
        System.out.println("\n#--------------HELP MENU-----------------");
        System.out.println("Welcome to the help menu:\n");
        System.out.println("To move a piece, please enter the position");
        System.out.println("of the starting square, followed by a space");
        System.out.println("and then followed by the ending position.");
        System.out.println("E.g. 'E2 E4' or 'e2 e4'. Not case sensitive.");
        System.out.println("\nWhen you're put into check by your opponent,");
        System.out.println("you need to move your King out of check.");
        System.out.println("Failure to do so will result in an illegal move,");
        System.out.println("and the game will be over for you.");
        System.out.println("#----------------------------------------");

        // Make sure that the user is ready before continuing. 
        String line;
        System.out.println("\nPlease enter 'ready' when you're ready to go!");
//        while (!(line = scan.nextLine()).equals("ready")) {
//            System.out.println("\nPlease enter 'ready' when you're ready to go!");
//        }
        System.out.println();
    }

    /**
     * Load an existing save file. Simulate the piece movements by playing the
     * game internally until the last save point.
     *
     * @return True, if the game save file was successfully loaded. False
     * otherwise.
     */
    private boolean loadExistingGame() {
        try {

            BufferedReader br = new BufferedReader(new FileReader(fileObj));
            // First two line stores the player name for white and black player correspondingly 
            gameData.whitePlayer = br.readLine().trim();
            gameData.blackPlayer = br.readLine().trim();

            // Read until the end of the file
            String line = "";
            while (true) {
                line = br.readLine();

                if (line == null) {
                    break;
                }
                if (line.equalsIgnoreCase("")) {
                    break;
                }

                line = line.trim();
                String promotionName = "";

                if (line.split(" ").length == 3) {
                    // contains the pawn promotion piece name
                    String promotionNewName = line.split(" ")[2].trim();
                    if (promotionNewName.equalsIgnoreCase("Queen")) {
                        promotionName = "Queen";
                    } else if (promotionNewName.equalsIgnoreCase("Bishop")) {
                        promotionName = "Bishop";
                    } else if (promotionNewName.equalsIgnoreCase("Knight")) {
                        promotionName = "Knight";
                    } else if (promotionNewName.equalsIgnoreCase("Rook")) {
                        promotionName = "Rook";
                    } else {
                        promotionName = "Error";
                    }
                }

                String startPositionString = line.split(" ")[0];
                String endPositionString = line.split(" ")[1];
                Position startPosition = board.getPosition(startPositionString);
                Position endPosition = board.getPosition(endPositionString);

                if (startPosition == null || endPosition == null || promotionName.equals("Error")) {
                    br.close();
                    System.out.println("\nYour " + fileObj.getName() + " file is corrupted. Deleting save file.");
                    System.out.println("Please open the program again.");
                    fileObj.delete();
                    System.exit(0);
                }

                // special check for pawn that was promoted to another piece
                if (!promotionName.equals("")) {
                    Pawn tempPawn = (Pawn) startPosition.getPiece();
                    if (tempPawn.validMove(board, startPosition, endPosition, promotionName)) {
                        board.movePiece(startPosition, endPosition);
                        gameData.isWhiteTurn = !gameData.isWhiteTurn;
                        continue;
                    }
                } else {
                    if (startPosition.getPiece().validMove(board, startPosition, endPosition)) {
                        board.movePiece(startPosition, endPosition);
                        gameData.isWhiteTurn = !gameData.isWhiteTurn;
                        continue;
                    }
                }

                br.close();
                System.out.println("\nInvalid move detected in your " + fileObj.getName() + " file. Deleting save file.");
                System.out.println("Please open the program again.");
                fileObj.delete();
                System.exit(0);
            }
            br.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Run the chess game and get user input until the game finishes or an
     * expected error occurs.
     *
     * @param filename The name of the save file.
     */
    public void processMove(String move) {
        System.out.println(move);
        boolean inCheck = false;
        String message = "";

        String result = Check.checkForCheck(board);
        if (result.equals("checkmate")) {
            // checkmate
            // game finish 
            message += "\n" + "#--------------------";
            message += "\n" + "Checkmate!";
            message += "\n" + (gameData.isWhiteTurn ? "Black" : "White") + " player won!";
            message += "\n" + "#--------------------";
            message += "\n" + "Game exiting...";
            showMessage(message);
            fileObj.delete();
            System.exit(0);
        } else if (result.equals("check")) {
            // current player's king is in check
            inCheck = true;
            message += "\n" + ("#--------------------");
            message += "\n" + ((gameData.isWhiteTurn ? "White" : "Black") + " King is in check. Please move your king.");
            message += "\n" + ("#--------------------");
            showMessage(message);
        } else if (result.equals("stalemate")) {
            // stalemate
            // game end in draw, game is finished
            message += "\n" + ("#--------------------");
            message += "\n" + ("Stalemate!");
            message += "\n" + ("Game ends in a draw.");
            message += "\n" + ("#--------------------");
            message += "\n" + ("Game exiting...");
            showMessage(message);
            fileObj.delete();
            System.exit(0);
        }

        if (move.equalsIgnoreCase("quit")) {
        }

        if (move.equalsIgnoreCase("resign")) {
            // player resign
            // game finish 
            message += "\n" + ("#--------------------");
            message += "\n" + ((gameData.isWhiteTurn ? "White" : "Black") + " player resigned.");
            message += "\n" + ((gameData.isWhiteTurn ? "Black" : "White") + " player won!");
            message += "\n" + ("#--------------------");
            message += "\n" + ("Game exiting...");
            showMessage(message);
            fileObj.delete();
            System.exit(0);
        }

        if (move.equalsIgnoreCase("help")) {
            displayHelp();
        }

        if (move.split(" ").length != 2) {
            // The passed in value is incorrect
            showMessage("Please enter the move correctly. E.g. E2 E4");
            return;
        }

        String startPositionString = move.split(" ")[0];
        String endPositionString = move.split(" ")[1];
        Position startPosition = board.getPosition(startPositionString);
        Position endPosition = board.getPosition(endPositionString);

        if (startPosition == null || endPosition == null) {
            showMessage("Invalid input. Please try again.");
            return;
        }

        if (startPosition.getPiece() == null) {
            showMessage("You have selected an empty square. Please try again.");
            return;
        }

        // White players turn but moved a black piece
        if (gameData.isWhiteTurn && !startPosition.getPiece().getIsWhite()) {
            showMessage("It is white player's turn. Please move a white piece.");
            return;
        } else if (!gameData.isWhiteTurn && startPosition.getPiece().getIsWhite()) {
            showMessage("It is black player's turn. Please move a black piece.");
            return;
        }

        boolean promotionWasDone = false;
        String newPieceName = "";
        String originalName = "";
        if (startPosition.getPiece() != null) {
            originalName = startPosition.getPiece().getName();
        }
        // Check that this is a valid move for that Chess piece
        if (startPosition.getPiece().validMove(board, startPosition, endPosition)) {
            // check if promotion was done
            if (startPosition.getPiece().getIsWhite() && originalName.equals("Pawn") && endPosition.getLocationY() == 0) {
                promotionWasDone = true;
            } else if (!startPosition.getPiece().getIsWhite() && originalName.equals("Pawn") && endPosition.getLocationY() == 7) {
                promotionWasDone = true;
            }
            board.movePiece(startPosition, endPosition);
        } else {
            showMessage("Invalid move for " + startPosition.getPiece().getName() + ". Please try again.");
            return;
        }

        if (promotionWasDone) {
            newPieceName = board.getPosition(endPosition.getLocationY(), endPosition.getLocationX()).getPiece().getName();
        }

        // check if player has moved out of the check
        board.generateAllPossibleMoves();;
        String result2 = Check.checkForCheck(board);
        if (result2.equals("check") && inCheck) {
            // player did not move out of check
            // illegal move made
            // current player loses game
            message += "#--------------------";
            message += "\n" + (gameData.isWhiteTurn ? "White" : "Black") + " did not move out of check.";
            message += "\n" + "Illegal move detected!";
            message += "\n" + (gameData.isWhiteTurn ? "Black" : "White") + " player won!";
            message += "\n" + "#--------------------";
            message += "\n" + "Game exiting...";
            showMessage(message);
//            fileObj.delete();
            System.exit(0);
        }

//        writeLine(move.toUpperCase() + " " + newPieceName);
        gameData.isWhiteTurn = !gameData.isWhiteTurn;
        chessGUI.resetTextBox();
        chessGUI.updateGUI();
    }

    /**
     * @return the gameData
     */
    public static GameData getGameData() {
        return gameData;
    }
}

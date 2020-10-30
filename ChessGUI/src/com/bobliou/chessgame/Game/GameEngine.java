package com.bobliou.chessgame.Game;

import com.bobliou.chessgame.DB.DBOperations;
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
    private DBOperations db;

    public GameEngine(Board board, GameData gameData, ChessGUI chessGUI, DBOperations db) {
        this.board = board;
        this.gameData = gameData;
        this.chessGUI = chessGUI;
        this.db = db;
    }

    /**
     * Reset the game
     * @param board the new baord
     * @param gameData the new game data
     * @return the result
     */
    public boolean reset(Board board, GameData gameData) {
        if (board != null && gameData != null) {
            this.board = board;
            this.gameData = gameData;
            return true;
        }
        return false;
    }

    /**
     * Display a message
     * @param message The message to show
     * @return result
     */
    public boolean showMessage(String message) {
        if (message != null && !message.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(null, message, "", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Write a given string to the save file using the append method.
     *
     * @param line The string to write to the save file.
     * @return True, if successfully. False otherwise.
     */
    private boolean writeLine(String line) {
        String gameName = chessGUI.getGameName();
        Thread thread = new Thread(new UpdateDB(db, gameName, line));
        thread.start();
        return true;
    }

    /**
     * Runnable class to update database entry
     */
    private class UpdateDB implements Runnable {

        DBOperations db;
        String gameName;
        String line;

        public UpdateDB(DBOperations db, String gameName, String line) {
            this.db = db;
            this.gameName = gameName;
            this.line = line;
        }

        @Override
        public void run() {
            db.addGameMoveEntry(gameName, line);
        }
    }

    /**
     * Displays the help menu.
     */
    public void displayHelp() {
        String message = "";
        message += ("\n#------------------------------HELP MENU----------------------------------");
        message += "\n" + ("Welcome to the help menu:\n");
        message += "\n" + ("To move a piece, please enter the position");
        message += "\n" + ("of the starting square, followed by a space");
        message += "\n" + ("and then followed by the ending position.");
        message += "\n" + ("E.g. 'E2 E4' or 'e2 e4'. Not case sensitive.");
        message += "\n" + ("\nPress the 'Move' button or press 'Enter' to confirm the move.");
        message += "\n" + ("\nWhen you're put into check by your opponent,");
        message += "\n" + ("you need to move your King out of check.");
        message += "\n" + ("Failure to do so will result in an illegal move,");
        message += "\n" + ("and the game will be over for you.");
        message += "\n" + ("#-------------------------------------------------------------------------");

        showMessage(message);
    }

    /**
     * Load an existing save file.Simulate the piece movements by playing the
     * game internally until the last save point.
     *
     * @param history the game history
     * @return True, if the game save file was successfully loaded. False
     * otherwise.
     */
    public boolean loadExistingGame(String history) {

        for (String line : history.split(",")) {
            line = line.trim();

            if (line == null) {
                continue;
            }

            if (line.equalsIgnoreCase("")) {
                continue;
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
                String mesasge = "";
                mesasge += "\n" + ("\nYour " + fileObj.getName() + " file is corrupted. Deleting save file.");
                mesasge += "\n" + ("Please open the program again.");
                showMessage(mesasge);
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

            String message = "";
            message += "\n" + ("\nInvalid move detected in your " + fileObj.getName() + " file. Deleting save file.");
            message += "\n" + ("Please open the program again.");
            showMessage(message);
            System.exit(0);
        }

        return true;
    }

    /**
     * Run the chess game and get user input until the game finishes or an
     * expected error occurs.
     *
     * @param move The move taken.
     * @return result
     */
    public boolean processMove(String move) {
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
            return false;
        }

        String startPositionString = move.split(" ")[0];
        String endPositionString = move.split(" ")[1];
        Position startPosition = board.getPosition(startPositionString);
        Position endPosition = board.getPosition(endPositionString);

        if (startPosition == null || endPosition == null) {
            showMessage("Invalid input. Please try again.");
            return false;
        }

        if (startPosition.getPiece() == null) {
            showMessage("You have selected an empty square. Please try again.");
            return false;
        }

        // White players turn but moved a black piece
        if (gameData.isWhiteTurn && !startPosition.getPiece().getIsWhite()) {
            showMessage("It is white player's turn. Please move a white piece.");
            return false;
        } else if (!gameData.isWhiteTurn && startPosition.getPiece().getIsWhite()) {
            showMessage("It is black player's turn. Please move a black piece.");
            return false;
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
            return false;
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

        writeLine(move.toUpperCase() + " " + newPieceName);
        gameData.isWhiteTurn = !gameData.isWhiteTurn;
        chessGUI.resetTextBox();
        chessGUI.updateGUI();
        return true;
    }

    /**
     * @return the gameData
     */
    public static GameData getGameData() {
        return gameData;
    }
}

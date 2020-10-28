package com.bobliou.chessgame.Game;

import com.bobliou.chessgame.Pieces.Pawn;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * A simple class structure to keep track of the white and black player's name
 * and who's turn it currently is.
 *
 * @author Bob Liou - 18013456
 */
class GameData {

    public String whitePlayer = "";
    public String blackPlayer = "";
    public boolean isWhiteTurn = true; // White player starts
}

/**
 * The driver class of the program that stores the game data and get user's
 * input.
 *
 * @author Bob Liou - 18013456
 */
public class GameMain {

    private static Board board;
    private static Scanner scan;
    public static GameData gameData;
    private static File fileObj;

    /**
     * Check if the save file for Chess exist.
     *
     * @return True, if the save file exist. Otherwise, false.
     */
    private static boolean fileExist() {
        File f = new File("savefile.txt");
        return f.exists() && !f.isDirectory();
    }

    /**
     * Write a given string to the save file using the append method.
     *
     * @param line The string to write to the save file.
     * @return True, if successfully. False otherwise.
     */
    private static boolean writeLine(String line) {
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
    private static void displayHelp() {
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
        while (!(line = scan.nextLine()).equals("ready")) {
            System.out.println("\nPlease enter 'ready' when you're ready to go!");
        }
        System.out.println();
    }

    /**
     * Load an existing save file. Simulate the piece movements by playing the
     * game internally until the last save point.
     *
     * @return True, if the game save file was successfully loaded. False
     * otherwise.
     */
    private static boolean loadExistingGame() {
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
    private static void runGame(String filename) {
        fileObj = new File(filename);
        boolean loadExisting = false; // If an existing save file was loaded

        if (fileExist()) {
            System.out.println("There is an existing save file. Would you like to load it? (y/n)");
            String response = scan.nextLine().trim();
            if (response.equalsIgnoreCase("y")) {
                // Load file and simulate play until finish
                if (loadExistingGame()) {
                    loadExisting = true;
                    System.out.println("\nSuccessfully loaded the save file!");
                }
            } else {
                fileObj.delete();
            }
        }

        if (!loadExisting) {
            // Show welcome message for a new game
            // Welcome message
            System.out.println("Welcome to a new Chess game!\n");

            // Ask the two players for their name
            System.out.print("Please enter white player's name: ");
            gameData.whitePlayer = scan.nextLine();
            writeLine(gameData.whitePlayer);
            System.out.print("Please enter black player's name: ");
            gameData.blackPlayer = scan.nextLine();
            writeLine(gameData.blackPlayer);
        }

        // GameMain start
        System.out.println(String.format("\nWelcome %s and %s!", gameData.whitePlayer, gameData.blackPlayer));
        System.out.println("#----------------------------------------");
        System.out.println("You may enter \'quit\' at anytime to save the game and exit.");
        System.out.println("You may enter \'resign\' at anytime to accept your defeat.");
        System.out.println("You may enter \'help\' at anytime to view the user manual.");
        System.out.println("#----------------------------------------\n");

        // Get game input
        boolean looping = true;
        String move;

        while (looping) {
            // Continue to run the game until the user want to quit
            board.printBoard();
            boolean inCheck = false;

            System.out.println(String.format("\n%s - %s player\'s turn", gameData.isWhiteTurn ? gameData.whitePlayer : gameData.blackPlayer, gameData.isWhiteTurn ? "White" : "Black"));
            String result = Check.checkForCheck(board);
            if (result.equals("checkmate")) {
                // checkmate
                // game finish 
                System.out.println("#--------------------");
                System.out.println("Checkmate!");
                System.out.println((gameData.isWhiteTurn ? "Black" : "White") + " player won!");
                System.out.println("#--------------------");
                System.out.println("Game exiting...");
                fileObj.delete();
                System.exit(0);
            } else if (result.equals("check")) {
                // current player's king is in check
                inCheck = true;
                System.out.println("#--------------------");
                System.out.println((gameData.isWhiteTurn ? "White" : "Black") + " King is in check. Please move your king.");
                System.out.println("#--------------------");
            } else if (result.equals("stalemate")) {
                // stalemate
                // game end in draw, game is finished
                System.out.println("#--------------------");
                System.out.println("Stalemate!");
                System.out.println("Game ends in a draw.");
                System.out.println("#--------------------");
                System.out.println("Game exiting...");
                fileObj.delete();
                System.exit(0);
            }
            System.out.print("Please enter your move: ");
            move = scan.nextLine().trim();

            if (move.equalsIgnoreCase("quit")) {
                looping = false; // Save and exit game
                continue;
            }

            if (move.equalsIgnoreCase("resign")) {
                // player resign
                // game finish 
                System.out.println("#--------------------");
                System.out.println((gameData.isWhiteTurn ? "White" : "Black") + " player resigned.");
                System.out.println((gameData.isWhiteTurn ? "Black" : "White") + " player won!");
                System.out.println("#--------------------");
                System.out.println("Game exiting...");
                fileObj.delete();
                System.exit(0);
            }

            if (move.equalsIgnoreCase("help")) {
                displayHelp();
                continue;
            }

            if (move.split(" ").length != 2) {
                // The passed in value is incorrect
                System.out.println("\nPlease enter the move correctly. E.g. E2 E4\n");
                continue;
            }

            String startPositionString = move.split(" ")[0];
            String endPositionString = move.split(" ")[1];
            Position startPosition = board.getPosition(startPositionString);
            Position endPosition = board.getPosition(endPositionString);

            if (startPosition == null || endPosition == null) {
                System.out.println("\nInvalid input. Please try again.\n");
                continue;
            }

            if (startPosition.getPiece() == null) {
                System.out.println("\nYou have selected an empty square. Please try again.\n");
                continue;
            }

            // White players turn but moved a black piece
            if (gameData.isWhiteTurn && !startPosition.getPiece().getIsWhite()) {
                System.out.println("\nIt is white player's turn. Please move a white piece.\n");
                continue;
            } else if (!gameData.isWhiteTurn && startPosition.getPiece().getIsWhite()) {
                System.out.println("\nIt is black player's turn. Please move a black piece.\n");
                continue;
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
                System.out.println("\nInvalid move for " + startPosition.getPiece().getName() + ". Please try again.\n");
                continue;
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
                System.out.println("\n#--------------------");
                System.out.println((gameData.isWhiteTurn ? "White" : "Black") + " did not move out of check.");
                System.out.println("Illegal move detected!");
                System.out.println((gameData.isWhiteTurn ? "Black" : "White") + " player won!");
                System.out.println("#--------------------");
                System.out.println("Game exiting...");
                fileObj.delete();
                System.exit(0);
            }

            writeLine(move.toUpperCase() + " " + newPieceName);
            gameData.isWhiteTurn = !gameData.isWhiteTurn;
            System.out.println();
        }
    }

    /**
     * The main driver class. Initialise the static variables.
     * 
     * @param args The arguments passed in.
     */
    public static void main(String[] args) {
        scan = new Scanner(System.in);
        board = new Board();
        gameData = new GameData();
        runGame("savefile.txt");
    }
}

package com.bobliou.chessgame.Game;

import com.bobliou.chessgame.Pieces.Piece;
import com.bobliou.chessgame.Pieces.Bishop;
import com.bobliou.chessgame.Pieces.Queen;
import com.bobliou.chessgame.Pieces.King;
import com.bobliou.chessgame.Pieces.Knight;
import com.bobliou.chessgame.Pieces.Pawn;
import com.bobliou.chessgame.Pieces.Rook;
import java.util.HashMap;

/**
 * The chess board class keeps track of the 64 squares on a chess board. Each
 * square could have a chess piece. The class also specially keeps track of the
 * two king's current position, this will be used to determine if a king is in
 * check.
 *
 * @author Bob Liou - 18013456
 */
public class Board {

    public final static int BOARD_LENGTH = 8;
    private Position[][] boardArray = new Position[BOARD_LENGTH][BOARD_LENGTH]; // 2D array of 64 possbile squares

    // The hash map maps the name and colour of a piece to their respective emoji symbol.
    private HashMap<String, String> chessMap = new HashMap<String, String>() {
        {
            put("WKing", "♔");
            put("WQueen", "♕");
            put("WRook", "♖");
            put("WBishop", "♗");
            put("WKnight", "♘");
            put("WPawn", "♙");
            put("BKing", "♚");
            put("BQueen", "♛");
            put("BRook", "♜");
            put("BBishop", "♝");
            put("BKnight", "♞");
            put("BPawn", "♟");
        }
    };

    // Stores the white and black king's position. 
    // This is used for keeping track if a king is in check.
    private Position kingPositions[] = new Position[2];

    // Empty constructor
    public Board() {
        initialiseBoard();
    }

    /**
     * Initialise the board array with chess pieces and their positions.
     */
    private void initialiseBoard() {
        // Initalise black pieces
        int row = 0;
        boardArray[row][0] = new Position(0, row, new Rook(false));
        boardArray[row][1] = new Position(1, row, new Knight(false));
        boardArray[row][2] = new Position(2, row, new Bishop(false));
        boardArray[row][3] = new Position(3, row, new Queen(false));
        boardArray[row][4] = new Position(4, row, new King(false));
        boardArray[row][5] = new Position(5, row, new Bishop(false));
        boardArray[row][6] = new Position(6, row, new Knight(false));
        boardArray[row][7] = new Position(7, row, new Rook(false));

        setKingPositions(boardArray[row][4], false);

        // Initialise black pawns
        row += 1;
        for (int column = 0; column < BOARD_LENGTH; column++) {
            boardArray[row][column] = new Position(column, row, new Pawn(false));
        }

        // Initialise empty squares
        for (row = 2; row < 6; row++) {
            for (int column = 0; column < BOARD_LENGTH; column++) {
                boardArray[row][column] = new Position(column, row, null);
            }
        }

        // Initalise white pawns
        row = 6;
        for (int column = 0; column < BOARD_LENGTH; column++) {
            boardArray[row][column] = new Position(column, row, new Pawn(true));
        }

        row += 1;
        // Initalise white pieces
        boardArray[row][0] = new Position(0, row, new Rook(true));
        boardArray[row][1] = new Position(1, row, new Knight(true));
        boardArray[row][2] = new Position(2, row, new Bishop(true));
        boardArray[row][3] = new Position(3, row, new Queen(true));
        boardArray[row][4] = new Position(4, row, new King(true));
        boardArray[row][5] = new Position(5, row, new Bishop(true));
        boardArray[row][6] = new Position(6, row, new Knight(true));
        boardArray[row][7] = new Position(7, row, new Rook(true));

        setKingPositions(boardArray[row][4], true);
    }

    /**
     * Print out the whole board. The '\\u2003' and '\\u2006' unicode characters
     * used here are special space characters used to align the text and chess
     * emoji.
     */
    public void printBoard() {
        for (int row = 0; row < BOARD_LENGTH; row++) {
            boolean isOddRow = (row + 1) % 2 == 1;
            String line = "";
            line += String.format("%3s", BOARD_LENGTH - row);
            for (int column = 0; column < BOARD_LENGTH; column++) {
                boolean isOddCol = (column + 1) % 2 == 1;
                Piece currentPiece = boardArray[row][column].getPiece() != null ? boardArray[row][column].getPiece() : null;
                if (currentPiece != null) {
                    currentPiece.generatePossibleMoves(this, boardArray[row][column]); // UNCOMMENT TODO
                    String name = currentPiece.getName();
                    String colour = currentPiece.getIsWhite() ? "W" : "B";
                    line += String.format("%3s", chessMap.get(colour + name));
                } else {
                    if (isOddRow == isOddCol) {
                        // print space
                        // unicode character \u2003 used for aligning the characters
                        line += String.format("%3s", "\u2003");
                    } else {
                        // print black square
                        // unicode character \u2006 used for aligning the characters
                        line += String.format("%3s", "\u2006\u2006\u2006\u2006\u2006\u2006\u2006\u2006⬛");
                    }
                }
            }
            System.out.println(line);
        }

        String line = "   ";
        for (int i = 0; i < BOARD_LENGTH; i++) {
            line += String.format("%s", "\u2006\u2006\u2006\u2006\u2006\u2006\u2006\u2006" + numberToLetter(i) + "\u2006"); // Lining the letters up with chess pieces
        }
        System.out.println(line);
    }

    /**
     * Get the position object by passing in the square location as string.
     *
     * @param move The string representation of the square location.
     * @return The inquired position object.
     */
    public Position getPosition(String move) {
        // First value in move is the column and second value in move is the row
        // E.g. E2 is row 2 and column E
        try {
            int row = Integer.parseInt(move.substring(1));
            row = BOARD_LENGTH - row;
            if (row < 0 || row >= 8) {
                return null; // Invalid range for row
            }

            // Check that the first value is in the correct range
            char firstValue = move.toUpperCase().charAt(0);
            if (firstValue >= 65 && firstValue <= 72) {
                // In the correct range
                int column = letterToNumber(move);
                return boardArray[row][column];
            } else {
                // Not in the correct range
                // Return null and the calling code will deal with it
                return null;
            }
        } catch (NumberFormatException e) {
            // Error parsing the second value in move
            return null; // Return null and the calling code will deal with it
        }
    }

    /**
     * Get the position object by passing in the row and column values.
     *
     * @param row The row of where the position is at.
     * @param column The column of where the position is at.
     * @return The inquired position object.
     */
    public Position getPosition(int row, int column) {
        return boardArray[row][column];
    }

    /**
     * Convert number to letter using ASCII values.
     *
     * @param number The number to convert to letter.
     * @return The corresponding character to the number.
     */
    public char numberToLetter(int number) {
        // Convert a number to letter using ASCII value
        // 0 to 'a', 1 to 'b', etc.
        return (char) (65 + number);
    }

    /**
     * Convert letter to number using ASCII values.
     *
     * @param letter The letter to convert to number.
     * @return The corresponding integer to the letter.
     */
    public int letterToNumber(String letter) {
        // Convert a letter to number using ASCII value
        // 'A' to 0, 'B' to 1, etc.
        // Opposite of numberToLetter function
        return ((int) letter.toUpperCase().charAt(0) - 65);
    }

    /**
     * Loop through the board and generate all possible moves for all the pieces
     * on the board.
     */
    public void generateAllPossibleMoves() {
        for (int row = 0; row < BOARD_LENGTH; row++) {
            for (int column = 0; column < BOARD_LENGTH; column++) {
                Piece currentPiece = boardArray[row][column].getPiece();
                if (currentPiece == null) {
                    continue;
                }
                currentPiece.generatePossibleMoves(this, boardArray[row][column]);
            }
        }
    }

    /**
     * Move a piece on the board.
     *
     * @param startPosition The starting position of the piece.
     * @param endPosition The ending position of the piece.
     * @return True, if the move was successful. False otherwise.
     */
    public boolean movePiece(Position startPosition, Position endPosition) {
        // Piece at start position moves to end position
        // Start position is now empty
        boardArray[endPosition.getLocationY()][endPosition.getLocationX()] = new Position(endPosition.getLocationX(), endPosition.getLocationY(), startPosition.getPiece());
        boardArray[startPosition.getLocationY()][startPosition.getLocationX()] = new Position(startPosition.getLocationX(), startPosition.getLocationY(), null);

        if (boardArray[endPosition.getLocationY()][endPosition.getLocationX()].getPiece().getName().equals("King")) {
            setKingPositions(boardArray[endPosition.getLocationY()][endPosition.getLocationX()], startPosition.getPiece().getIsWhite());
        }

        startPosition.getPiece().generatePossibleMoves(this, boardArray[endPosition.getLocationY()][endPosition.getLocationX()]);

        return true;
    }

    /**
     * Returns the array of kings position.
     *
     * @return The array of kings position.
     */
    public Position[] getKingPositions() {
        return kingPositions;
    }

    /**
     * Set the king's position.
     *
     * @param kingPosition The position of the king.
     * @param isWhiteKing The colour of the king.
     */
    public void setKingPositions(Position kingPosition, boolean isWhiteKing) {
        if (isWhiteKing) {
            this.kingPositions[0] = kingPosition;
        } else {
            this.kingPositions[1] = kingPosition;
        }
    }
}

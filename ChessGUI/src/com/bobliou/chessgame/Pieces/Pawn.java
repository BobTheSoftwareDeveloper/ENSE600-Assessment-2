package com.bobliou.chessgame.Pieces;

import com.bobliou.chessgame.Game.Board;
import com.bobliou.chessgame.Game.Check;
import com.bobliou.chessgame.Game.Move;
import com.bobliou.chessgame.Game.Position;
import java.util.ArrayDeque;
import java.util.Scanner;

/**
 * The pawn chess piece. The class keeps track of when an en passant is possible
 * for the opponent pawn piece. Pawn promotion is possible when your pawn
 * reaches the very last square on the opposite side. You can choose the piece
 * you would like to promote your pawn into.
 *
 * @author Bob Liou - 18013456
 */
public class Pawn extends Piece {

    private Position enPassantPossible;

    public Pawn(boolean isWhite) {
        super(isWhite, "Pawn");
        enPassantPossible = null;
    }

    /**
     *
     * @param board
     * @param start
     * @param end
     * @param promotionName
     */
    private void promotion(Board board, Position start, Position end, String promotionName) {
        Scanner scan = new Scanner(System.in);
        if (promotionName.equals("")) {
            System.out.println("\nPromotion for " + (isWhite ? "White" : "Black") + " pawn!");
        }
        while (true) {
            String input = "";
            Position currentPosition = board.getPosition(start.getLocationY(), start.getLocationX());
            if (promotionName.equals("")) {
                System.out.println("Please enter the piece you will like to promote to: (Queen, Bishop, Knight, or Rook)");
                input = scan.nextLine().trim();
            } else {
                input = promotionName;
            }

            if (input.equalsIgnoreCase("Queen")) {
                currentPosition.setPiece(new Queen(isWhite));
            } else if (input.equalsIgnoreCase("Bishop")) {
                currentPosition.setPiece(new Bishop(isWhite));
            } else if (input.equalsIgnoreCase("Knight")) {
                currentPosition.setPiece(new Knight(isWhite));
            } else if (input.equalsIgnoreCase("Rook")) {
                currentPosition.setPiece(new Rook(isWhite));
            } else {
                System.out.println("Invalid input. Try again.\n");
                continue;
            }
            break;
        }
    }

    /**
     * Check if the given move is a valid move for pawn. This is the entry point
     * for most function calls. The function calls another internal function
     * using method overloading.
     *
     * @param board The current chess board.
     * @param start The starting position of the pawn.
     * @param end The ending position of the pawn.
     * @return True, if the move is valid. False otherwise.
     */
    @Override
    public boolean validMove(Board board, Position start, Position end) {
        return validMove(board, start, end, "");
    }

    /**
     * Check if the given move is a valid move for pawn. This function is only
     * called when loading a save file. In the save file, there would be
     * information of the piece at the pawn was previously promoted to.
     *
     * @param board The current chess board.
     * @param start The starting position of the pawn.
     * @param end The ending position of the pawn.
     * @param promotionName The name of the piece to promote the pawn to. Only
     * used when loading a save file.
     * @return
     */
    public boolean validMove(Board board, Position start, Position end, String promotionName) {
        // Check if the end positon has a piece of the same colour
        if (super.sameColour(start, end)) {
            return false; // moving your piece to a location with the same colour
        }

        boolean promotionPossible = false;
        if (isWhite && end.getLocationY() == 0) {
            // promotion is possible for whtie pawn
            promotionPossible = true;
        } else if (!isWhite && end.getLocationY() == 7) {
            // promotion is possible for black pawn
            promotionPossible = true;
        }

        // Check for en passant
        if (enPassantPossible != null) {
            if (super.getIsWhite()) {
                // White pawn en passant possible when at the fifth row
                if (start.getLocationY() == 3) {
                    // Moved one square in the y-axis and zero in the x-axis
                    if (Move.moveInDiagonal(board, start, end, 1)) {
                        // Check that there is a opposition pawn when the en passant happens
                        if (end.getLocationY() == enPassantPossible.getLocationY() - 1 && end.getLocationX() == enPassantPossible.getLocationX()) {
                            // valid en passant move
                            board.getPosition(enPassantPossible.getLocationY(), enPassantPossible.getLocationX()).setPiece(null);
                            enPassantPossible = null;
                            return true;
                        }
                    }
                }
            } else {
                // Black pawn en passant possible when at the fourth row
                if (start.getLocationY() == 4) {
                    // Moved one square in the y-axis and zero in the x-axis
                    if (Move.moveInDiagonal(board, start, end, 1)) {
                        // Check that there is a opposition pawn when the en passant happens
                        if (end.getLocationY() == enPassantPossible.getLocationY() + 1 && end.getLocationX() == enPassantPossible.getLocationX()) {
                            // valid en passant move
                            board.getPosition(enPassantPossible.getLocationY(), enPassantPossible.getLocationX()).setPiece(null);
                            enPassantPossible = null;
                            return true;
                        }
                    }
                }
            }
        }

        // Check for initial move
        if (start.getPiece().getIsWhite() && start.getLocationY() == 6) {
            // White Pawn at Y = 6 can move two moves as the inital start
            if (Move.moveInYAxis(board, start, end, 2) && end.getPiece() == null) {
                // check if the left and right location of this pawn has black pawns 
                Position left = Move.getLeft(board, end);
                Position right = Move.getRight(board, end);
                if (left != null && left.getPiece().getName().equals("Pawn")) {
                    // check if the left square is a black pawn
                    if (!left.getPiece().isWhite) {
                        // En passant possible
                        Pawn tempPawn = (Pawn) left.getPiece();
                        Position tempPosition = new Position(end.getLocationX(), end.getLocationY(), this);
                        tempPawn.setEnPassantPossible(tempPosition);
                    }
                }
                if (right != null && right.getPiece().getName().equals("Pawn")) {
                    // check if the left square is a black pawn
                    if (!right.getPiece().isWhite) {
                        // En passant possible
                        Pawn tempPawn = (Pawn) right.getPiece();
                        Position tempPosition = new Position(end.getLocationX(), end.getLocationY(), this);
                        tempPawn.setEnPassantPossible(tempPosition);
                    }
                }
                return true;
            }
        } else if (!start.getPiece().getIsWhite() && start.getLocationY() == 1) {
            // The move after the first can only be moved once
            if (Move.moveInYAxis(board, start, end, 2) && end.getPiece() == null) {
                // check if the left and right location of this pawn has black pawns 
                Position left = Move.getLeft(board, end);
                Position right = Move.getRight(board, end);
                if (left != null && left.getPiece().getName().equals("Pawn")) {
                    // check if the left square is a black pawn
                    if (left.getPiece().isWhite) {
                        // En passant possible
                        Pawn tempPawn = (Pawn) left.getPiece();
                        Position tempPosition = new Position(end.getLocationX(), end.getLocationY(), this);
                        tempPawn.setEnPassantPossible(tempPosition);
                    }
                }
                if (right != null && right.getPiece().getName().equals("Pawn")) {
                    // check if the left square is a black pawn
                    if (right.getPiece().isWhite) {
                        // En passant possible
                        Pawn tempPawn = (Pawn) right.getPiece();
                        Position tempPosition = new Position(end.getLocationX(), end.getLocationY(), this);
                        tempPawn.setEnPassantPossible(tempPosition);
                    }
                }
                return true;
            }
        } else {
            // Check if the Pawn is moving backwards
            if (isWhite) {
                // white pawn can only move upwards 
                // y is decreasing
                if (end.getLocationY() >= start.getLocationY()) {
                    // invalid move. pawn moving backwards
                    return false;
                }
            } else {
                // black pawn can only move downwards
                // y is increasing 
                if (end.getLocationY() <= start.getLocationY()) {
                    // invalid move. pawn moving backwards
                    return false;
                }
            }
            if (Move.moveInYAxis(board, start, end, 1) && end.getPiece() == null) {
                if (promotionPossible) {
                    // pawn can promote
                    promotion(board, start, end, promotionName);
                }
                return true;
            }
        }

        // Check for side capture
        if (Move.moveInDiagonal(board, start, end, 1)) {
            if (end.getPiece() != null) {
                if (promotionPossible) {
                    promotion(board, start, end, promotionName);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * @return the enPassantPossible
     */
    public Position getEnPassantPossible() {
        return enPassantPossible;
    }

    /**
     * @param enPassantPossible the enPassantPossible to set
     */
    public void setEnPassantPossible(Position enPassantPossible) {
        this.enPassantPossible = enPassantPossible;
    }

    /**
     * Generate a list of possible moves for the pawn at this instance in time.
     *
     * @param board The current chess board.
     * @param current The current position of this pawn.
     */
    @Override
    public void generatePossibleMoves(Board board, Position current) {
        if (current == null || current.getPiece() == null) {
            return;
        }

        // check all possible moves
        possibleMoves.clear();
        ArrayDeque<Position> ad = new ArrayDeque<>();

        if (isWhite) {
            // check top left
            ad = Check.checkTopLeft(board, current, 1);
            for (Position p : ad) {
                possibleMoves.add(p);
            }

            // check top right
            ad = Check.checkTopRight(board, current, 1);
            for (Position p : ad) {
                possibleMoves.add(p);
            }
        } else {
            // check bottom left
            ad = Check.checkBottomLeft(board, current, 1);
            for (Position p : ad) {
                possibleMoves.add(p);
            }

            // check bottom right
            ad = Check.checkBottomRight(board, current, 1);
            for (Position p : ad) {
                possibleMoves.add(p);
            }
        }

    }
}

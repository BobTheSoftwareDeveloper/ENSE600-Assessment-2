package com.bobliou.chessgame.Pieces;

import com.bobliou.chessgame.Game.Board;
import com.bobliou.chessgame.Game.Check;
import com.bobliou.chessgame.Game.Move;
import com.bobliou.chessgame.Game.Position;
import java.util.ArrayDeque;

/**
 * The king chess piece. The class keeps track of if the piece has been moved or
 * not. This is used when castling is performed. Once a King is moved, you're
 * not allowed to castle with it.
 *
 * @author Bob Liou - 18013456
 */
public class King extends Piece {

    private boolean hasMoved = false;

    public King(boolean isWhite) {
        super(isWhite, "King");
    }

    /**
     * Check if the given move is a valid move for king.
     *
     * @param board The current chess board.
     * @param start The starting position of the king.
     * @param end The ending position of the king.
     * @return True, if the move is valid. False otherwise.
     */
    @Override
    public boolean validMove(Board board, Position start, Position end) {
        // Check if the end positon has a piece of the same colour
        if (super.sameColour(start, end)) {
            return false; // moving your piece to a location with the same colour
        }

        // Check that the king is trying to castle
        if (Move.moveInXAxis(board, start, end, 2, true)) {
            if (hasMoved) {
                // King has moved
                // Cannot castle
                return false;
            }

            // King has moved exactly 2 squares 
            // Attempt to castle 
            int startX = start.getLocationX();
            int endX = end.getLocationX();
            int diff = startX - endX;

            if (diff > 0) {
                // Castle left side
                // Try get the Rook on the left
                Position left = board.getPosition(start.getLocationY(), 0);
                if (left.getPiece() == null || !left.getPiece().getName().equals("Rook")) {
                    return false; // No Rook on the left side
                }
                Rook leftRook = (Rook) left.getPiece();
                if (leftRook.getHasMoved()) {
                    // Rook has moved
                    // Cannot castle
                    return false;
                }

                // Check that all squares are empty between King and Rook
                if (board.getPosition(start.getLocationY(), 1).getPiece() != null) {
                    // There is a chess piece in the way
                    return false;
                }

                // All condition met, can castle
                // Move Rook to the square next to king
                board.movePiece(left, new Position(start.getLocationX() - 1, start.getLocationY(), null));
                hasMoved = true;
                return true; // Castle successful
            } else {
                // Castle right side
                // Try get the Rook on the right
                Position right = board.getPosition(start.getLocationY(), 7);
                if (right.getPiece() == null || !right.getPiece().getName().equals("Rook")) {
                    return false; // No Rook on the right side
                }
                Rook rightRook = (Rook) right.getPiece();
                if (rightRook.getHasMoved()) {
                    // Rook has moved
                    // Cannot castle
                    return false;
                }

                // All condition met, can castle
                // Move Rook to the square next to king
                board.movePiece(right, new Position(start.getLocationX() + 1, start.getLocationY(), null));
                hasMoved = true;
                return true; // Castle successful
            }
        }

        // Check that the move is valid for King
        if (Move.moveInDiagonal(board, start, end, 1)) {
            // Kind moved in diagonal in one move
            hasMoved = true;
            return true;
        } else if (Move.moveInXAxis(board, start, end, 1)) {
            // Kind moved in the x-axis in one move
            hasMoved = true;
            return true;
        } else if (Move.moveInYAxis(board, start, end, 1)) {
            // King moved in the y-axis in one move
            hasMoved = true;
            return true;
        }

        // Invalid move for the King
        return false;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * Generate a list of possible moves for the king at this instance in
     * time.
     *
     * @param board The current chess board.
     * @param current The current position of this king.
     */
    @Override
    public void generatePossibleMoves(Board board, Position current) {
        if (current == null || current.getPiece() == null) {
            return;
        }

        // check all possible moves
        possibleMoves.clear();
        ArrayDeque<Position> ad = new ArrayDeque<>();

        // check top left
        ad = Check.checkTopLeft(board, current, 1);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check up
        ad = Check.checkUp(board, current, 1);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check top right
        ad = Check.checkTopRight(board, current, 1);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check left
        ad = Check.checkLeft(board, current, 1);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check right
        ad = Check.checkRight(board, current, 1);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check bottom left
        ad = Check.checkBottomLeft(board, current, 1);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check down
        ad = Check.checkDown(board, current, 1);
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

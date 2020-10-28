package com.bobliou.chessgame.Pieces;

import com.bobliou.chessgame.Game.Board;
import com.bobliou.chessgame.Game.Check;
import com.bobliou.chessgame.Game.Move;
import com.bobliou.chessgame.Game.Position;
import java.util.ArrayDeque;

/**
 * The rook chess piece. The class keeps track of if the piece has been moved or
 * not. This is used when castling is performed. Once a Rook is moved, you're
 * not allowed to castle with it.
 *
 * @author Bob Liou - 18013456
 */
public class Rook extends Piece {

    private boolean hasMoved = false;

    public Rook(boolean isWhite) {
        super(isWhite, "Rook");
    }

    /**
     * Check if the given move is a valid move for rook.
     *
     * @param board The current chess board.
     * @param start The starting position of the rook.
     * @param end The ending position of the rook.
     * @return True, if the move is valid. False otherwise.
     */
    @Override
    public boolean validMove(Board board, Position start, Position end) {
        // Check if the end positon has a piece of the same colour
        if (super.sameColour(start, end)) {
            return false; // moving your piece to a location with the same colour
        }

        if (Move.moveInXAxis(board, start, end, 0)) {
            // Rook moved in the x-axis
            hasMoved = true;
            return true;
        } else if (Move.moveInYAxis(board, start, end, 0)) {
            // Rook moved in the y-axis
            hasMoved = true;
            return true;
        }

        // Invalid move for Rook
        return false;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * Generate a list of possible moves for the rook at this instance in time.
     *
     * @param board The current chess board.
     * @param current The current position of this rook.
     */
    @Override
    public void generatePossibleMoves(Board board, Position current) {
        if (current == null || current.getPiece() == null) {
            return;
        }

        // check all possible moves
        possibleMoves.clear();
        ArrayDeque<Position> ad = new ArrayDeque<>();

        // check up
        ad = Check.checkUp(board, current, 0);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check down
        ad = Check.checkDown(board, current, 0);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check left
        ad = Check.checkLeft(board, current, 0);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check right
        ad = Check.checkRight(board, current, 0);
        for (Position p : ad) {
            possibleMoves.add(p);
        }
    }
}

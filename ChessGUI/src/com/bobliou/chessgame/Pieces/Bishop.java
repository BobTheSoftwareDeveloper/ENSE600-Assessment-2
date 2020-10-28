package com.bobliou.chessgame.Pieces;

import com.bobliou.chessgame.Game.Board;
import com.bobliou.chessgame.Game.Check;
import com.bobliou.chessgame.Game.Move;
import com.bobliou.chessgame.Game.Position;
import java.util.ArrayDeque;

/**
 * The bishop chess piece.
 *
 * @author Bob Liou - 18013456
 */
public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite, "Bishop");
    }

    /**
     * Check if the given move is a valid move for bishop.
     *
     * @param board The current chess board.
     * @param start The starting position of the bishop.
     * @param end The ending position of the bishop.
     * @return True, if the move is valid. False otherwise.
     */
    @Override
    public boolean validMove(Board board, Position start, Position end) {
        // Check if the end positon has a piece of the same colour
        if (super.sameColour(start, end)) {
            return false; // moving your piece to a location with the same colour
        }

        if (Move.moveInDiagonal(board, start, end, 0)) {
            // Valid move for Bishop
            return true;
        }

        return false;
    }

    /**
     * Generate a list of possible moves for the bishop at this instance in
     * time.
     *
     * @param board The current chess board.
     * @param current The current position of this bishop.
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
        ad = Check.checkTopLeft(board, current, 0);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check top right
        ad = Check.checkTopRight(board, current, 0);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check bottom left
        ad = Check.checkBottomLeft(board, current, 0);
        for (Position p : ad) {
            possibleMoves.add(p);
        }

        // check bottom right
        ad = Check.checkBottomRight(board, current, 0);
        for (Position p : ad) {
            possibleMoves.add(p);
        }
    }
}

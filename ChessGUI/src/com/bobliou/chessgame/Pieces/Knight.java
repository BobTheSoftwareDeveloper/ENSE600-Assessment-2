package com.bobliou.chessgame.Pieces;

import com.bobliou.chessgame.Game.Board;
import com.bobliou.chessgame.Game.Check;
import com.bobliou.chessgame.Game.Move;
import com.bobliou.chessgame.Game.Position;

/**
 * The knight chess piece.
 *
 * @author Bob Liou - 18013456
 */
public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite, "Knight");
    }
    
    /**
     * Check if the given move is a valid move for knight.
     *
     * @param board The current chess board.
     * @param start The starting position of the knight.
     * @param end The ending position of the knight.
     * @return True, if the move is valid. False otherwise.
     */
    @Override
    public boolean validMove(Board board, Position start, Position end) {
        // Check if the end positon has a piece of the same colour
        if (super.sameColour(start, end)) {
            return false; // moving your piece to a location with the same colour
        }
        
        int xDifference = Move.calculateDifference(start, end, true);
        int yDifference = Move.calculateDifference(start, end, false);
        
        if ((xDifference * yDifference) == 2) {
            // Valid move for Knight
            return true;
        }
        
        return false;
    }

    /**
     * Generate a list of possible moves for the knight at this instance in
     * time.
     *
     * @param board The current chess board.
     * @param current The current position of this knight.
     */
    @Override
    public void generatePossibleMoves(Board board, Position current) {
        if (current == null || current.getPiece() == null) {
            return;
        }
        
        // There are eight possible moves for Knight
        possibleMoves.clear();
        int currentX = current.getLocationX();
        int currentY = current.getLocationY();
        int x;
        int y;
        
        // left-top-top
        x = currentX - 1;
        y = currentY - 2;
        if (Check.checkBoundary(x, y)) {
            Position currentPosition = board.getPosition(y, x);
            if (Check.checkPossibleMove(current, currentPosition)) {
                possibleMoves.add(currentPosition);
            }
        }
        
        // left-top-bottom
        x = currentX - 2;
        y = currentY - 1;
        if (Check.checkBoundary(x, y)) {
            Position currentPosition = board.getPosition(y, x);
            if (Check.checkPossibleMove(current, currentPosition)) {
                possibleMoves.add(currentPosition);
            }
        }
        
        // right-top-top
        x = currentX + 1;
        y = currentY - 2;
        if (Check.checkBoundary(x, y)) {
            Position currentPosition = board.getPosition(y, x);
            if (Check.checkPossibleMove(current, currentPosition)) {
                possibleMoves.add(currentPosition);
            }
        }
        
        // right-top-bottom
        x = currentX + 2;
        y = currentY - 1;
        if (Check.checkBoundary(x, y)) {
            Position currentPosition = board.getPosition(y, x);
            if (Check.checkPossibleMove(current, currentPosition)) {
                possibleMoves.add(currentPosition);
            }
        }
        
        // left-bottom-top
        x = currentX - 2;
        y = currentY + 1;
        if (Check.checkBoundary(x, y)) {
            Position currentPosition = board.getPosition(y, x);
            if (Check.checkPossibleMove(current, currentPosition)) {
                possibleMoves.add(currentPosition);
            }
        }
        
        // left-bottom-bottom
        x = currentX - 1;
        y = currentY + 2;
        if (Check.checkBoundary(x, y)) {
            Position currentPosition = board.getPosition(y, x);
            if (Check.checkPossibleMove(current, currentPosition)) {
                possibleMoves.add(currentPosition);
            }
        }
        
        // right-bottom-top
        x = currentX + 2;
        y = currentY + 1;
        if (Check.checkBoundary(x, y)) {
            Position currentPosition = board.getPosition(y, x);
            if (Check.checkPossibleMove(current, currentPosition)) {
                possibleMoves.add(currentPosition);
            }
        }
        
        // right-bottom-bottom
        x = currentX + 1;
        y = currentY + 2;
        if (Check.checkBoundary(x, y)) {
            Position currentPosition = board.getPosition(y, x);
            if (Check.checkPossibleMove(current, currentPosition)) {
                possibleMoves.add(currentPosition);
            }
        }
    }
}

package com.bobliou.chessgame.Game;

/**
 * The moveable interface to determine if a class can be checked for valid moves
 * and generate possible moves.
 *
 * @author Bob Liou - 18013456
 */
public interface Moveable {

    /**
     * Check if the given move is a valid move for a piece.
     *
     * @param board The current chess board.
     * @param start The starting position of the piece.
     * @param end The ending position of the piece.
     * @return True, if the move is valid. False otherwise.
     */
    public boolean validMove(Board board, Position start, Position end);

    /**
     * Generate a list of possible moves for a piece at this instance in
     * time.
     *
     * @param board The current chess board.
     * @param current The current position of this piece.
     */
    public void generatePossibleMoves(Board board, Position current);
}

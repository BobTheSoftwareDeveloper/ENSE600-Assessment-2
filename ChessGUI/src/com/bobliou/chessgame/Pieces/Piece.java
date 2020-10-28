package com.bobliou.chessgame.Pieces;

import com.bobliou.chessgame.Game.Moveable;
import com.bobliou.chessgame.Game.Position;
import java.util.ArrayList;

/**
 * Abstract class for all chess pieces. The class stores the piece's name,
 * colour, and an array list of all possible moves for this piece at an instance
 * in time.
 *
 * @author Bob Liou - 18013456
 */
public abstract class Piece implements Moveable {

    protected boolean isWhite;
    private String name;
    protected ArrayList<Position> possibleMoves;

    public Piece(boolean isWhite, String name) {
        this.setIsWhite(isWhite);
        this.setName(name);
        possibleMoves = new ArrayList<>();
    }

    public boolean getIsWhite() {
        return isWhite;
    }

    public void setIsWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check if the starting position and ending position is to a piece of the
     * same colour. Since you are not allowed to move a white piece to another
     * white piece's square, same for black pieces.
     *
     * @param start The starting position.
     * @param end The ending position.
     * @return True, if the starting and ending position is the same. False
     * otherwise.
     */
    protected boolean sameColour(Position start, Position end) {
        // check if you're moving a white/black piece to a location with another white/black piece
        if (end.getPiece() != null && (start.getPiece().getIsWhite() == end.getPiece().getIsWhite())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s.", this.isWhite ? "White" : "Black", this.getName());
    }

    /**
     * @return The array list of all possible moves for this piece.
     */
    public ArrayList<Position> getPossibleMoves() {
        return possibleMoves;
    }

    /**
     * Sets the array list of all possible moves for this piece.
     *
     * @param possibleMoves The possible moves to set.
     */
    public void setPossibleMoves(ArrayList<Position> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }
}

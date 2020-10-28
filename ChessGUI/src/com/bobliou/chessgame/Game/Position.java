package com.bobliou.chessgame.Game;

import com.bobliou.chessgame.Pieces.Piece;

/**
 * The position class stores the x and y position on the board. If there are no
 * chess piece at this position, piece would be set to null.
 *
 * @author Bob Liou - 18013456
 */
public class Position {

    private int locationX;
    private int locationY;
    private Piece piece;

    public Position(int locationX, int locationY, Piece piece) {
        this.setLocationX(locationX);
        this.setLocationY(locationY);
        this.setPiece(piece);
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public String toString() {
        String name = piece != null ? piece.getIsWhite() == true ? "White" : "Black" : "";
        name += piece != null ? " " + piece.getName() : "";

        return String.format("At position row = %d and column = %d there is %s", locationY, locationX, name);
    }
}

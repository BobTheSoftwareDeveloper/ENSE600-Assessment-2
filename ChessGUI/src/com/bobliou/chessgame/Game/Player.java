package com.bobliou.chessgame.Game;

/**
 * The player class keeps track of the player's colour and name.
 *
 * @author Bob Liou - 18013456
 */
public class Player {

    private boolean whitePlayer;
    private String name;

    public Player(String name, boolean whitePlayer) {
        this.setWhitePlayer(whitePlayer);
        this.setName(name);
    }

    public boolean isWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(boolean whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

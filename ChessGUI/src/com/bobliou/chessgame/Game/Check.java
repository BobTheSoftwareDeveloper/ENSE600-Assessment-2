package com.bobliou.chessgame.Game;

import com.bobliou.chessgame.Pieces.Piece;
import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Helper class with static functions to check the possible moves for a given
 * position. Check if a player is in check, checkmate, or stalemate.
 *
 * @author Bob Liou - 18013456
 */
public class Check {

    // Static boolean variable to indicate if the move checking was stopped 
    // due to meeting the move amount condition.
    public static boolean moveStop = false;

    /**
     * Check up from the given position.
     *
     * @param board The current board.
     * @param current The current position.
     * @param amount The amount of moves allowed.
     * @return An array deque with the possible positions (moves).
     */
    public static ArrayDeque<Position> checkUp(Board board, Position current, int amount) {
        ArrayDeque<Position> ad = new ArrayDeque<>(7);
        int counter = 0;
        int x = current.getLocationX();
        for (int y = current.getLocationY() - 1; y >= 0; y--) {
            Position currentPosition = board.getPosition(y, x);

            if (checkPossibleMove(current, currentPosition)) {
                ad.add(currentPosition);
            }
            if (moveStop) {
                break;
            }

            counter++;
            if (amount != 0 && counter == amount) {
                break;
            }
        }
        return ad;
    }

    /**
     * Check down from the given position.
     *
     * @param board The current board.
     * @param current The current position.
     * @param amount The amount of moves allowed.
     * @return An array deque with the possible positions (moves).
     */
    public static ArrayDeque<Position> checkDown(Board board, Position current, int amount) {
        ArrayDeque<Position> ad = new ArrayDeque<>(7);
        int counter = 0;
        int x = current.getLocationX();
        for (int y = current.getLocationY() + 1; y <= 7; y++) {
            Position currentPosition = board.getPosition(y, x);

            if (checkPossibleMove(current, currentPosition)) {
                ad.add(currentPosition);
            }
            if (moveStop) {
                break;
            }

            counter++;
            if (amount != 0 && counter == amount) {
                break;
            }
        }
        return ad;
    }

    /**
     * Check left from the given position.
     *
     * @param board The current board.
     * @param current The current position.
     * @param amount The amount of moves allowed.
     * @return An array deque with the possible positions (moves).
     */
    public static ArrayDeque<Position> checkLeft(Board board, Position current, int amount) {
        ArrayDeque<Position> ad = new ArrayDeque<>(7);
        int counter = 0;
        int y = current.getLocationY();
        for (int x = current.getLocationX() - 1; x >= 0; x--) {
            Position currentPosition = board.getPosition(y, x);

            if (checkPossibleMove(current, currentPosition)) {
                ad.add(currentPosition);
            }
            if (moveStop) {
                break;
            }

            counter++;
            if (amount != 0 && counter == amount) {
                break;
            }
        }
        return ad;
    }

    /**
     * Check right from the given position.
     *
     * @param board The current board.
     * @param current The current position.
     * @param amount The amount of moves allowed.
     * @return An array deque with the possible positions (moves).
     */
    public static ArrayDeque<Position> checkRight(Board board, Position current, int amount) {
        ArrayDeque<Position> ad = new ArrayDeque<>(7);
        int counter = 0;
        int y = current.getLocationY();
        for (int x = current.getLocationX() + 1; x <= 7; x++) {
            Position currentPosition = board.getPosition(y, x);

            if (checkPossibleMove(current, currentPosition)) {
                ad.add(currentPosition);
            }
            if (moveStop) {
                break;
            }

            counter++;
            if (amount != 0 && counter == amount) {
                break;
            }
        }
        return ad;
    }

    /**
     * Check top left (diagonal) from the given position.
     *
     * @param board The current board.
     * @param current The current position.
     * @param amount The amount of moves allowed.
     * @return An array deque with the possible positions (moves).
     */
    public static ArrayDeque<Position> checkTopLeft(Board board, Position current, int amount) {
        ArrayDeque<Position> ad = new ArrayDeque<>(7);
        int counter = 0;
        int x = current.getLocationX() - 1;
        for (int y = current.getLocationY() - 1; y >= 0; y--) {
            if (x < 0 || y < 0) {
                return ad;
            }
            Position currentPosition = board.getPosition(y, x);

            if (checkPossibleMove(current, currentPosition)) {
                ad.add(currentPosition);
            }
            if (moveStop) {
                break;
            }

            counter++;
            if (amount != 0 && counter == amount) {
                break;
            }
            x--;
        }
        return ad;
    }

    /**
     * Check top right (diagonal) from the given position.
     *
     * @param board The current board.
     * @param current The current position.
     * @param amount The amount of moves allowed.
     * @return An array deque with the possible positions (moves).
     */
    public static ArrayDeque<Position> checkTopRight(Board board, Position current, int amount) {
        ArrayDeque<Position> ad = new ArrayDeque<>(7);
        int counter = 0;
        int x = current.getLocationX() + 1;
        for (int y = current.getLocationY() - 1; y >= 0; y--) {
            if (x > 7 || y < 0) {
                return ad;
            }
            Position currentPosition = board.getPosition(y, x);

            if (checkPossibleMove(current, currentPosition)) {
                ad.add(currentPosition);
            }
            if (moveStop) {
                break;
            }

            counter++;
            if (amount != 0 && counter == amount) {
                break;
            }
            x++;
        }
        return ad;
    }

    /**
     * Check bottom left (diagonal) from the given position.
     *
     * @param board The current board.
     * @param current The current position.
     * @param amount The amount of moves allowed.
     * @return An array deque with the possible positions (moves).
     */
    public static ArrayDeque<Position> checkBottomLeft(Board board, Position current, int amount) {
        ArrayDeque<Position> ad = new ArrayDeque<>(7);
        int counter = 0;
        int x = current.getLocationX() - 1;
        for (int y = current.getLocationY() + 1; y <= 7; y++) {
            if (x < 0 || y > 7) {
                return ad;
            }
            Position currentPosition = board.getPosition(y, x);

            if (checkPossibleMove(current, currentPosition)) {
                ad.add(currentPosition);
            }
            if (moveStop) {
                break;
            }

            counter++;
            if (amount != 0 && counter == amount) {
                break;
            }
            x--;
        }
        return ad;
    }

    /**
     * Check bottom right (diagonal) from the given position.
     *
     * @param board The current board.
     * @param current The current position.
     * @param amount The amount of moves allowed.
     * @return An array deque with the possible positions (moves).
     */
    public static ArrayDeque<Position> checkBottomRight(Board board, Position current, int amount) {
        ArrayDeque<Position> ad = new ArrayDeque<>(7);
        int counter = 0;
        int x = current.getLocationX() + 1;
        for (int y = current.getLocationY() + 1; y <= 7; y++) {
            if (x > 7 || y > 7) {
                return ad;
            }
            Position currentPosition = board.getPosition(y, x);

            if (checkPossibleMove(current, currentPosition)) {
                ad.add(currentPosition);
            }
            if (moveStop) {
                break;
            }

            counter++;
            if (amount != 0 && counter == amount) {
                break;
            }
            x++;
        }
        return ad;
    }

    /**
     * Check that the given move is possible and does not move to a position of
     * the same colour chess piece.
     *
     * @param current The position of the original piece.
     * @param currentPosition The position of the current iteration piece.
     * @return True, if the move is possible. False otherwise.
     */
    public static boolean checkPossibleMove(Position current, Position currentPosition) {
        moveStop = false;
        if (currentPosition.getPiece() == null) {
            // moveable position
            return true;
        } else if (current.getPiece().getIsWhite() != currentPosition.getPiece().getIsWhite()) {
            // opponent piece here
            moveStop = true;
            return true;
        }
        moveStop = true;
        return false;
    }

    /**
     * Check the given x and y values are within the chess board boundaries.
     *
     * @param x The x location.
     * @param y The y location.
     * @return True, if the x and y location are within the chess board. False
     * otherwise.
     */
    public static boolean checkBoundary(int x, int y) {
        if (x >= 0 && x <= 7) {
            if (y >= 0 && y <= 7) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the current player is in check.
     * 
     * @param board The current board.
     * @return The current condition of the game.
     */
    public static String checkForCheck(Board board) {
        boolean isWhiteTurn = GameEngine.getGameData().isWhiteTurn;

        // Using hashmap for O(1) store and access. Store the current player's king square position.
        HashMap<Position, Integer> hashMapKingSquare = new HashMap<>(); 
        
        // Using hashmap for O(1) store and access. Stores the possible move for the current player's king.
        HashMap<Position, Integer> hashMapKingPossibleSquare = new HashMap<>(); 
        
        Position squareInCheck = null;
        
        // An array deque of positions that the current player's piece can block.
        ArrayDeque<Position> possibleBlock = new ArrayDeque<>();
        
        int possibleBlockNo = 0;
        boolean noPieceCanMove = true;

        if (isWhiteTurn) {
            // get white king current position
            hashMapKingSquare.put(board.getKingPositions()[0], 0);
            for (Position p : board.getKingPositions()[0].getPiece().getPossibleMoves()) {
                hashMapKingPossibleSquare.put(p, 0);
            }
        } else {
            // get black king current position
            hashMapKingSquare.put(board.getKingPositions()[1], 0);
            for (Position p : board.getKingPositions()[1].getPiece().getPossibleMoves()) {
                hashMapKingPossibleSquare.put(p, 0);
            }
        }

        for (int row = 0; row < Board.BOARD_LENGTH; row++) {
            for (int column = 0; column < Board.BOARD_LENGTH; column++) {
                Position currentPosition = board.getPosition(row, column);
                Piece currentPiece = currentPosition.getPiece();
                if (currentPiece == null) {
                    // skip empty square
                    continue;
                }
                if (currentPiece.getIsWhite() == isWhiteTurn && !currentPiece.getName().equals("King")) {
                    // skip white pieces on white player's turn
                    // likewise, skip black pieces on black player's turn
                    for (Position p : currentPiece.getPossibleMoves()) {
                        possibleBlock.add(p);
                    }
                }

                // loop through the possible moves of the current piece and check with the hash map
                if (currentPiece.getPossibleMoves().size() > 0) {
                    noPieceCanMove = false;
                }
                for (Position p : currentPiece.getPossibleMoves()) {
                    if (hashMapKingSquare.get(p) != null) {
                        hashMapKingSquare.put(p, -1);
                    } else if (hashMapKingPossibleSquare.get(p) != null) {
                        hashMapKingPossibleSquare.put(p, -1);
                    }
                }
            }
        }

        boolean check = false;
        boolean checkmate = false;
        boolean stalemate = false;

        for (Position key : hashMapKingSquare.keySet()) {
            if (hashMapKingSquare.get(key) == -1) {
                // in check
                check = true;
            }
        }

        for (Position key : possibleBlock) {
            if (hashMapKingPossibleSquare.get(key) != null && hashMapKingPossibleSquare.get(key) == -1) {
                // this position exist
                // can be blocked by the user's piece and also making sure that the blocking position is a position being attack by
                possibleBlockNo++;
            }
        }

        // Check the number of possible moves for the current player's king.
        int kingsPossibleMove = 0;
        for (Position key : hashMapKingPossibleSquare.keySet()) {
            if (hashMapKingPossibleSquare.get(key) == 0) {
                kingsPossibleMove++;
            }
        }

        // Check for checkmate and stalemate conditions.
        if (check && kingsPossibleMove == 0 && possibleBlockNo == 0) {
            checkmate = true;
        } else if (!check && noPieceCanMove) {
            stalemate = true;
        }

        // Return the current game state.
        if (checkmate) {
            return "checkmate";
        } else if (check) {
            return "check";
        } else if (stalemate) {
            return "stalemate";
        }

        return "none";
    }
}

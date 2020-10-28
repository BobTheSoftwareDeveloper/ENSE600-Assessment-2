package com.bobliou.chessgame.Game;

/**
 * Helper class with static functions to test if a piece is moving the correct
 * amount of squares.
 *
 * @author Bob Liou - 18013456
 */
public class Move {

    /**
     * Check move in the x-axis direction. Calls the internal function with a
     * default parameter of exact amount = false. Using method overloading.
     *
     * @param board The current board.
     * @param start The starting position.
     * @param end The ending position.
     * @param amount The amount of moves allowed.
     * @return True, if the move is allowed. False otherwise.
     */
    public static boolean moveInXAxis(Board board, Position start, Position end, int amount) {
        return moveInXAxis(board, start, end, amount, false);
    }

    /**
     * Check move in the x-axis direction. Used only for the king piece when
     * castling by passing in exactAmount = true.
     *
     * @param board The current board.
     * @param start The starting position.
     * @param end The ending position.
     * @param amount The amount of moves allowed.
     * @param exactAmount If it is required to move an exact amount of given
     * squares.
     * @return True, if the move is allowed. False otherwise.
     */
    public static boolean moveInXAxis(Board board, Position start, Position end, int amount, boolean exactAmount) {
        if (end.getPiece() != null && start.getPiece().getIsWhite() == end.getPiece().getIsWhite()) {
            return false; // Same colour piece
        }

        int difference = calculateDifference(start, end, false);
        boolean returnResult = false;

        if (difference != 0) {
            // The piece has moved in the y-axis
            return false;
        }

        difference = calculateDifference(start, end, true);
        if (exactAmount && difference == amount) {
            // move the exact amount of moves
            returnResult = true;
        } else if (!exactAmount && difference <= amount) {
            // Valid move. Done in less than or equal to the valid amount. 
            returnResult = true;
        } else if (amount == 0) {
            // Unlimited amount of moves
            returnResult = true;
        }

        if (returnResult) {
            // Check if the movement crosses any pieces
            int row = start.getLocationY();
            int startCol = start.getLocationX(); // Start from the next column
            int endCol = end.getLocationX();

            if (startCol > endCol) {
                int endColTemp = endCol;
                endCol = startCol;
                startCol = endColTemp + 1;
            } else {
                startCol += 1; // Do not need to check itself
            }

            for (int col = startCol; col < endCol; col++) {
                if (board.getPosition(row, col).getPiece() != null) {
                    return false;
                }
            }
        }

        return returnResult;
    }

    /**
     * Check move in the y-axis direction.
     *
     * @param board The current board.
     * @param start The starting position.
     * @param end The ending position.
     * @param amount The amount of moves allowed.
     * @return True, if the move is allowed. False otherwise.
     */
    public static boolean moveInYAxis(Board board, Position start, Position end, int amount) {
        if (end.getPiece() != null && start.getPiece().getIsWhite() == end.getPiece().getIsWhite()) {
            return false; // Same colour piece
        }

        int difference = calculateDifference(start, end, true);
        boolean returnResult = false;

        if (difference != 0) {
            // The piece has moved in the x-axis
            return false;
        }

        difference = calculateDifference(start, end, false);
        if (difference <= amount) {
            // Valid move. Done in less than or equal to the valid amount.
            returnResult = true;
        } else if (amount == 0) {
            // Unlimited amount of moves
            returnResult = true;
        }

        if (returnResult) {
            // Check if the movement crosses any pieces
            int col = start.getLocationX();
            int startRow = start.getLocationY(); // Start from the next column
            int endRow = end.getLocationY();

            if (startRow > endRow) {
                int endRowTemp = endRow;
                endRow = startRow;
                startRow = endRowTemp + 1;
            } else {
                startRow += 1;
            }

            for (int row = startRow; row < endRow; row++) {
                if (board.getPosition(row, col).getPiece() != null) {
                    return false;
                }
            }
        }

        return returnResult;
    }

    /**
     * Check move in diagonal.
     *
     * @param board The current board.
     * @param start The starting position.
     * @param end The ending position.
     * @param amount The amount of moves allowed.
     * @return True, if the move is allowed. False otherwise.
     */
    public static boolean moveInDiagonal(Board board, Position start, Position end, int amount) {
        if (end.getPiece() != null && start.getPiece().getIsWhite() == end.getPiece().getIsWhite()) {
            return false; // Same colour piece
        }

        int xDifference = calculateDifference(start, end, true);
        int yDifference = calculateDifference(start, end, false);

        if (xDifference == yDifference && amount == 0) {
            // Moved in diagonal
            return true;
        }

        if (xDifference == yDifference && xDifference <= amount) {
            // Moved in diagonal
            return true;
        }

        return false;
    }

    /**
     * Get the position of the square to the left of the given position.
     *
     * @param board The current board.
     * @param position The current position of the piece.
     * @return The position object of the piece to the left. If there are no
     * piece to the left then null is returned.
     */
    public static Position getLeft(Board board, Position position) {
        int currentX = position.getLocationX();
        int currentY = position.getLocationY();
        if (currentX == 0) {
            // going out of bound
            return null;
        }

        Position newPosition = board.getPosition(currentY, currentX - 1);
        if (newPosition.getPiece() == null) {
            return null;
        }

        return newPosition;
    }

    /**
     * Get the position of the square to the right of the given position.
     *
     * @param board The current board.
     * @param position The current position of the piece.
     * @return The position object of the piece to the right. If there are no
     * piece to the right then null is returned.
     */
    public static Position getRight(Board board, Position position) {
        int currentX = position.getLocationX();
        int currentY = position.getLocationY();
        if (currentX == 7) {
            // going out of bound
            return null;
        }

        Position newPosition = board.getPosition(currentY, currentX + 1);
        if (newPosition.getPiece() == null) {
            return null;
        }

        return newPosition;
    }

    /**
     * Calculate the difference amount with the starting and ending position.
     *
     * @param start The starting position.
     * @param end The ending position.
     * @param inXAxis If the position is in the x axis or not.
     * @return The difference amount.
     */
    public static int calculateDifference(Position start, Position end, boolean inXAxis) {
        // Calculate the difference in the x-axis
        if (inXAxis) {
            return Math.abs(start.getLocationX() - end.getLocationX());
        } else {
            return Math.abs(start.getLocationY() - end.getLocationY());
        }
    }
}

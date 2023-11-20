package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author Grigoriy Emiliyanov
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        if (atLeastOneMoveExists(this.board)) {
            board.setViewingPerspective(side);
            changed = tiltBoardToUp();
            this.board.setViewingPerspective(Side.NORTH);
        }
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /**
     * Tilt whole board to up and return true if any of tiles was moved
     * @return true, if any of tiles moved, otherwise false
     */
    public boolean tiltBoardToUp() {
        boolean someColumnChanged = false;
        for (int col = 0; col < this.board.size(); col++) {
            boolean columnChanged = tiltColumnToUp(col);
            someColumnChanged = columnChanged || someColumnChanged;
        }
        return someColumnChanged;
    }
    /**
     * Tilt single column to up, update score respectively and return true if any of tiles was moved
     * @return true, if any of tiles was moved, otherwise false
     */
    public boolean tiltColumnToUp(int col) {
        boolean changed = false;
        boolean wasUpperTileMerged = false;
        for (int startRow = topRow(); startRow >= 0; startRow--) {
            if (!isEmptyTileAt(col, startRow)) {
                int endRow = onWhichRowShouldEndUpTile(col, startRow, wasUpperTileMerged);
                if (endRow != startRow) {
                    Tile tileToMove = this.board.tile(col, startRow);
                    wasUpperTileMerged = this.board.move(col, endRow, tileToMove);
                    changed = true;
                    updateScore(wasUpperTileMerged, tileToMove.value());
                }
            }
        }
        return changed;
    }

    /** Update score with value if there was a move */
    public void updateScore(boolean wasAMove, int value) {
        if (wasAMove) {
            score += 2 * value;
        }
    }

    /** Returns the row on which tile(col, row) should end up */
    private int onWhichRowShouldEndUpTile(int col, int row, boolean wasUpperMerged) {
        if (isTopRow(row)) {
            return topRow();
        }
        int endRow = row;
        while (isBellowTopRow(endRow) && canMoveOneMoreRowUp(col, row, endRow, wasUpperMerged)) {
            endRow++;
        }
        return endRow;
    }

    /** Returns true if ONE of the conditions are met:
     * 1. empty tile at upper row
     * 1. tiles at (c1, r1) and (c2, r2) have same value
     * 2. the upper tile was not merged,
     * otherwise false */
    public boolean canMoveOneMoreRowUp(int c, int initialRow, int r2, boolean wasUpperMerged) {
        int upperRow = r2 + 1;
        return isEmptyTileAt(c, upperRow) || canMerge(c, initialRow, c, upperRow, wasUpperMerged);
    }

    /** Returns true if BOTH conditions are met:
     * 1. tiles at (c1, r1) and (c2, r2) have same value
     * 2. the upper tile was not merged,
     * otherwise false */
    public boolean canMerge(int c1, int r1, int c2, int r2, boolean wasUpperMerged) {
        return (sameTileValuesAt(c1, r1, c2, r2) && !wasUpperMerged);
    }

    /** Returns the number of the top row */
    private int topRow() {
        return this.board.size() - 1;
    }

    /** Returns true if given row is bellow top one, otherwise false */
    private boolean isBellowTopRow(int row) {
        return row < topRow();
    }

    /** Returns true if given row is top one, otherwise false */
    private boolean isTopRow(int row) {
        return row == topRow();
    }

    /** Returns true if tile at (c, r) is null, otherwise false */
    private boolean isEmptyTileAt(int c, int r) {
       return (this.board.tile(c, r) == null) ;
    }

    /** Returns true if tiles at (c1, r1) and (c2, r2) have same value, otherwise false */
    private boolean sameTileValuesAt(int c1, int r1, int c2, int r2) {
        return this.board.tile(c1, r1).value() == this.board.tile(c2, r2).value();
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        for (int col = 0; col < b.size(); col++) {
            for (int row = 0; row < b.size(); row++) {
                if (b.tile(col, row) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        for (int col = 0; col < b.size(); col++) {
            for (int row = 0; row < b.size(); row++) {
                if (isMaxTile(b.tile(col, row))) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns true if the given tile has MAX_PIECE value */
    public static boolean isMaxTile(Tile t) {
        return t != null && (t.value() == MAX_PIECE);
    }
    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        return emptySpaceExists(b) || twoAdjacentTilesSameValue(b);
    }

    /**
     * Returns true if there are two adjacent tiles with the same value, otherwise false.
     * @param b a board
     * @return true if there are two adjacent tiles with the same value, otherwise false
     */
    private static boolean twoAdjacentTilesSameValue(Board b) {
        boolean sameValuesVertically = twoVerticallyAdjacentTilesWithSameValues(b);
        boolean sameValuesHorizontally = twoHorizontallyAdjacentTilesWithSameValues(b);
        return sameValuesVertically || sameValuesHorizontally;
    }

    /**
     * Returns true if there are two vertically adjacent tiles with the same value, otherwise false.
     * @param b a board
     * @return true if there are two vertically adjacent tiles with the same value, otherwise false
     */
    private static boolean twoVerticallyAdjacentTilesWithSameValues(Board b) {
        for (int c = 0; c < b.size(); c++) {
            for (int r = 0; r < b.size() - 1; r++) {
                int currentRowValue = b.tile(c, r).value();
                int upperRowValue = b.tile(c, r + 1).value();
                if (currentRowValue == upperRowValue) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are two horizontally adjacent tiles with the same value, otherwise false.
     * @param b a board
     * @return true if there are two horizontally  adjacent tiles with the same value, otherwise false
     */
    private static boolean twoHorizontallyAdjacentTilesWithSameValues(Board b) {
        b.setViewingPerspective(Side.WEST);
        boolean sameValues = twoVerticallyAdjacentTilesWithSameValues(b);
        b.setViewingPerspective(Side.NORTH);
        return sameValues;
    }
    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}

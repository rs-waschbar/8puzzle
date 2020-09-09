/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Board {
    private final int[][] board;
    private final int[][] goalBoard;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        checkInputCorrectnessOf(tiles);
        goalBoard = createGoalBoardFor(tiles);
        board = tiles;
    }

    private void checkInputCorrectnessOf(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException("input can't be null");

        int rows = tiles.length;
        int columns = tiles[0].length;
        if (rows != columns) {
            throw new IllegalArgumentException("input tiles must be square");
        }

        int capasity = rows * rows;
        int[] testArr = new int[capasity];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                testArr[tiles[i][j]]++;
            }
        }
        for (int tile : testArr) {
            if (tile != 1) {
                throw new IllegalArgumentException("input tiles must not "
                                                           + "contain duplicates");
            }
        }
    }

    private int[][] createGoalBoardFor(int[][] tiles) {
        int[][] goal = new int[tiles.length][tiles.length];
        int num = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                goal[i][j] = ++num;
            }
        }
        goal[tiles.length - 1][tiles.length - 1] = 0;

        return goal;
    }

    // string representation of this board
    public String toString() {
        System.out.println();
        return null;
    }

    // board dimension n
    public int dimension() {

        return 0;
    }

    // number of tiles out of place
    public int hamming() {

        return 0;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {

        return 0;
    }

    // is this board the goal board?
    public boolean isGoal() {

        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {

        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}
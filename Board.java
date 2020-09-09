/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Board {
    private final int[][] board;
    private final int[][] goalBoard;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        checkInputCorrectnessOf(tiles);
        board = tiles.clone();
        goalBoard = createGoalBoardFor(tiles);
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
        StringBuilder sb = new StringBuilder();
        sb.append(board.length).append("\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                sb.append(" ").append(board[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of tiles out of place
    public int hamming() {
        int countOutOfPlace = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != goalBoard[i][j])
                    countOutOfPlace++;
            }
        }
        return countOutOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {

        return 0;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != goalBoard[i][j])
                    return false;
            }
        }
        return true;
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
    // public static void main(String[] args) {
    //
    // }

    static class BoardTest {
        int[][] inputTiles;
        Board testBoard1;


        @BeforeEach
        void setUp() {
            In in = new In("data/puzzle3x3-00.txt");
            int n = in.readInt();
            inputTiles = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    inputTiles[i][j] = in.readInt();
                }
            }
            testBoard1 = new Board(inputTiles);
        }

        @Test
        void testToString() {
            System.out.println(testBoard1);
        }

        @Test
        void isGoal() {
        }
    }

}
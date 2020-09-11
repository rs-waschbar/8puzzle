/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
                if (board[i][j] != goalBoard[i][j]) {
                    countOutOfPlace++;
                }
            }
        }
        return countOutOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sumDistances = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != goalBoard[i][j]) {
                    sumDistances += calculateGoalDistance(board, i, j);
                }
            }
        }
        return sumDistances;
    }

    private int calculateGoalDistance(int[][] tiles, int row, int col) {
        int distance = 0;
        int goalRow;
        int goalCol;

        if (tiles[row][col] == 0) {
            goalRow = tiles.length - 1;
            goalCol = tiles.length - 1;
        } else {
            goalRow = (tiles[row][col] - 1) / tiles.length;
            goalCol = (tiles[row][col] - 1) % tiles.length;
        }

        distance += Math.abs(goalRow - row);
        distance += Math.abs(goalCol - col);

        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (board.length != goalBoard.length) {
            return false;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != goalBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Board thatBoard = (Board) that;

        if (board.length != thatBoard.board.length ||
                board[0].length != thatBoard.board[0].length) {
            return false;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != thatBoard.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twinBoard;
        int[][] twin = cloneThisData();

        if (twin[0][0] != 0 && twin[0][1] != 0) {
            twinBoard = createMovedBoard(Direction.RIGHT, 0, 0);
        }
        else {
            twinBoard = createMovedBoard(Direction.LEFT, twin.length - 1,
                                         twin.length - 1);
        }
        return twinBoard;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int emptyRow = -1;
        int emptyCol = -1;

        search:
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                    break search;
                }
            }
        }
        return allPossibleMoves(emptyRow, emptyCol);
    }

    private List<Board> allPossibleMoves(int emptyRow, int emptyCol) {
        List<Board> nextBoards = new ArrayList<>();

        if (emptyRow == -1) {
            return nextBoards;
        }
        int lowBorder = 0;
        int hiBorder = board.length - 1;

        if (emptyRow != lowBorder) {
            nextBoards.add(createMovedBoard(Direction.UP, emptyRow, emptyCol));
        }
        if (emptyRow != hiBorder) {
            nextBoards.add(createMovedBoard(Direction.DOWN, emptyRow, emptyCol));
        }
        if (emptyCol != lowBorder) {
            nextBoards.add(createMovedBoard(Direction.LEFT, emptyRow, emptyCol));
        }
        if (emptyCol != hiBorder) {
            nextBoards.add(createMovedBoard(Direction.RIGHT, emptyRow, emptyCol));
        }

        return nextBoards;
    }

    private Board createMovedBoard(Direction dir, int row, int col) {
        int[][] movedBoard = cloneThisData();
        int tileRow = row + dir.dRow;
        int tileCol = col + dir.dCol;

        int temp = movedBoard[tileRow][tileCol];
        movedBoard[row][col] = movedBoard[tileRow][tileCol];
        movedBoard[tileRow][tileCol] = temp;

        return new Board(movedBoard);
    }

    private int[][] cloneThisData() {
        int[][] copyBoard = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            copyBoard[i] = board[i].clone();
        }
        return copyBoard;
    }

    private enum Direction {
        UP(-1, 0),
        DOWN(+1, 0),
        LEFT(0, -1),
        RIGHT(0, +1);

        int dRow;
        int dCol;

        Direction(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }

        public int getDRow() {
            return dRow;
        }

        public int getDCol() {
            return dCol;
        }
    }



    // unit testing (not graded)
    public static void main(String[] args) {

    }

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
            // System.out.println(testBoard1.isGoal());
            Assert.assertTrue(testBoard1.isGoal());
        }
    }

}
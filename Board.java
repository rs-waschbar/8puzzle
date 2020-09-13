/** *****************************************************************************
 *  Name: Ruslan Zhdanov
 *  Date: 09/13/2020
 *  Description: Coursera Princeton Algorithms course part1
 *              week 4 assignment
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
                if (board[i][j] != goalBoard[i][j] && board[i][j] != 0) {
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
                if (board[i][j] != goalBoard[i][j] && board[i][j] != 0) {
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

        int temp = movedBoard[row][col];
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
   /* public static void main(String[] args) {
        int[][] inputTiles = {
                {1, 2, 3},
                {4, 0, 6},
                {7, 8, 5},
                };
        Board testBoard = new Board(inputTiles);

        System.out.println(testBoard);

    }*/

    static class BoardTest {
        int[][] inputTiles1;
        int[][] wrongInputTiles1;
        int[][] wrongInputTiles2 = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 7},
                };
        int[][] inputTiles2 = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                };
        int[][] inputTiles3 = {
                {0, 2, 3},
                {4, 5, 6},
                {7, 8, 1},
                };
        int[][] inputTiles4 = {
                {1, 2, 3},
                {4, 0, 6},
                {7, 8, 5},
                };

        Board testBoard1;
        Board testBoard2;
        Board testBoard3;
        Board testBoard4;


        @BeforeEach
        void setUp() {
            In in = new In("data/puzzle3x3-00.txt");
            int n = in.readInt();
            inputTiles1 = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    inputTiles1[i][j] = in.readInt();
                }
            }
            testBoard1 = new Board(inputTiles1);
            testBoard2 = new Board(inputTiles2);
            testBoard3 = new Board(inputTiles3);
            testBoard4 = new Board(inputTiles4);
            wrongInputTiles1 = new int[4][2];
        }

        @Test
        void testBoardCreation() {
            // must throw IllegalArgumentException from not squared
            //Board input1 = new Board(wrongInputTiles1);
            // must throw IllegalArgumentException from duplicates
            //Board input2 = new Board(wrongInputTiles2);
        }

        @Test
        void testToString() {
            System.out.println(testBoard1);

        }

        @Test
        void testHamming() {
            Assert.assertEquals(0, testBoard1.hamming());
            Assert.assertEquals(8, testBoard2.hamming());
            Assert.assertEquals(1, testBoard3.hamming());
        }

        @Test
        void testManhattan() {
            Assert.assertEquals(0, testBoard1.manhattan());
            Assert.assertEquals(12, testBoard2.manhattan());
            Assert.assertEquals(4, testBoard3.manhattan());
        }

        @Test
        void testNeighbors() {
            System.out.println("**** test neighbours testBoard3: ");
            for (Board board : testBoard3.neighbors()) {
                System.out.println(board);
            }

            System.out.println("**** test neighbours testBoard4: ");
            ArrayList<Board> board4neighbours = (ArrayList<Board>) testBoard4.neighbors();

            for (Board board : testBoard4.neighbors()) {
                System.out.println(board);
            }

            Assert.assertEquals(4, board4neighbours.size());
        }

        @Test
        void testCreateMovedBoard() {
            System.out.println("**** test createMovedBoard testBoard3: ");
            System.out.println(testBoard3.createMovedBoard(Direction.DOWN, 0, 0));
        }

        @Test
        void testTwin() {
            System.out.println("**** test twin testBoard3: ");
            System.out.println(testBoard3.twin());

        }

        @Test
        void isGoal() {
            // System.out.println(testBoard1.isGoal());
            Assert.assertTrue(testBoard1.isGoal());
        }
    }

}
/** *****************************************************************************
 *  Name: Ruslan Zhdanov
 *  Date: 09/13/2020
 *  Description: Coursera Princeton Algorithms course part1
 *              week 4 assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.LinkedList;

public class Solver {
    private final SearchNode root;
    private LinkedList<Board> solutionBoards;

    private class SearchNode {
        Board board;
        int moves;
        SearchNode prev;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Input board must not be null");
        }
        root = new SearchNode(initial, 0, null);
        solutionBoards = new LinkedList<>();
        findSolutionFor(root);
    }

    private void findSolutionFor(SearchNode rootNode) {
        ArrayList<Board> visited = new ArrayList<>();
        MinPQ<SearchNode> pQueue = new MinPQ<>((s1, s2) -> Integer.compare(
                (s1.moves + s1.board.manhattan()), (s2.moves + s2.board.manhattan())));

        SearchNode curr = rootNode;
        SearchNode currTwin = new SearchNode(rootNode.board.twin(), 0, null);
        pQueue.insert(curr);
        pQueue.insert(currTwin);


        while (!searchingIsOver(curr) && !pQueue.isEmpty()) {
            curr = pQueue.delMin();
            visited.add(curr.board);

            for (Board nextBoard : curr.board.neighbors()) {
                if (!visited.contains(nextBoard)) {
                    SearchNode next = new SearchNode(nextBoard, curr.moves++, curr);
                    pQueue.insert(next);
                }
            }
        }

        if (searchingIsOver(curr)) {
            restorePathTo(curr);
        }
    }

    private void restorePathTo(SearchNode finalNode) {
        LinkedList<Board> path = new LinkedList<>();
        SearchNode curr = finalNode;
        while (curr.prev != null) {
            path.addFirst(curr.board);
            curr = curr.prev;
        }

        if (curr.equals(root)) {
            path.addFirst(curr.board);
            solutionBoards = path;
        }
    }

    private boolean searchingIsOver(SearchNode node) {
        return node.board.isGoal();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return (!solutionBoards.isEmpty());
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return  solutionBoards.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return solutionBoards;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        // In in = new In(args[0]);
        // int n = in.readInt();
        // int[][] tiles = new int[n][n];
        int[][] tiles = {
                {2, 1, 3},
                {4, 0, 6},
                {7, 8, 5},
                };
        // for (int i = 0; i < n; i++)
        //     for (int j = 0; j < n; j++)
        //         tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}

package nqueens.solver;

// COSC 3P71 - Assignment 1
// Author: Matthew Grahlman	[5875695]
// Date: October 2017

import nqueens.Board;
import nqueens.model.SolverEvent;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class HeuristicSolver implements Solver {

    private static final int SEED = 21;

    @Override
    public String name() {
        return "Heuristic Search (A*)";
    }

    @Override
    public void solve(int n, BlockingQueue<SolverEvent> queue) {
        int[] initialBoard = buildRandomBoard(n);
        Board current = new Board(initialBoard);

        LinkedList<Board> needChecked = new LinkedList<>();
        LinkedList<Board> checked = new LinkedList<>();
        checked.add(current);
        int attempts = 0;

        while (!checked.isEmpty() && !Thread.currentThread().isInterrupted()) {
            int bestHValue = Integer.MAX_VALUE;
            int bestIndex = 0;
            attempts++;

            for (int i = 0; i < checked.size(); i++) {
                if (checked.get(i).getHeuristicValue() < bestHValue) {
                    bestHValue = checked.get(i).getHeuristicValue();
                    bestIndex = i;
                }
            }

            Board best = checked.remove(bestIndex);
            emitBoardState(queue, best.getBoardState(), n, attempts);

            if (best.getHeuristicValue() == 0) {
                emit(queue, SolverEvent.solution(attempts));
                return;
            }

            needChecked.add(best);
            PriorityQueue<Board> nextBoards = best.genNextBoards();
            for (Board next : nextBoards) {
                if (!needChecked.contains(next)) {
                    checked.add(next);
                }
            }
        }

        if (!Thread.currentThread().isInterrupted()) {
            emit(queue, SolverEvent.done(attempts));
        }
    }

    private void emitBoardState(BlockingQueue<SolverEvent> queue, int[] board, int n, int attempts) {
        emit(queue, SolverEvent.clear(attempts));
        for (int col = 0; col < n; col++) {
            if (board[col] > 0) {
                emit(queue, SolverEvent.place(col, board[col], attempts));
            }
        }
    }

    private int[] buildRandomBoard(int n) {
        Random rand = new Random(SEED);
        int[] board = new int[n];
        for (int i = 0; i < n; i++) {
            board[i] = rand.nextInt(n) + 1;
        }
        return board;
    }

    private void emit(BlockingQueue<SolverEvent> queue, SolverEvent event) {
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

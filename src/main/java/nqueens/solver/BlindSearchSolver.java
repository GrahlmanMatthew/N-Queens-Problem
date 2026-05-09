package nqueens.solver;

// COSC 3P71 - Assignment 1
// Author: Matthew Grahlman	[5875695]
// Date: October 2017

import nqueens.model.SolverEvent;

import java.util.concurrent.BlockingQueue;

public class BlindSearchSolver implements Solver {

    @Override
    public String name() {
        return "Blind Search (DFS)";
    }

    @Override
    public void solve(int n, BlockingQueue<SolverEvent> queue) {
        int[] board = new int[n];
        int col = 0, row = 1, rTemp = 0, mainRow = 1;
        int attempts = 0;

        board[col] = mainRow;
        emit(queue, SolverEvent.place(col, mainRow, ++attempts));
        col++;

        while (col < n && !Thread.currentThread().isInterrupted()) {
            while (row <= n && !Thread.currentThread().isInterrupted()) {
                board[col] = row;
                emit(queue, SolverEvent.place(col, row, attempts));

                if (checkState(board, n, col, row)) {
                    col++;
                    row = 1;
                    break;
                } else {
                    emit(queue, SolverEvent.conflict(col, row, attempts));
                    board[col] = 0;
                    emit(queue, SolverEvent.remove(col, row, attempts));
                    row++;
                    attempts++;
                }

                if (row > n) {
                    col--;
                    while (col != 0 && !Thread.currentThread().isInterrupted()) {
                        rTemp = board[col] + 1;
                        while (rTemp <= n && !Thread.currentThread().isInterrupted()) {
                            board[col] = rTemp;
                            emit(queue, SolverEvent.place(col, rTemp, attempts));
                            if (checkState(board, n, col, rTemp)) {
                                col++;
                                row = 0;
                                rTemp = 1;
                                break;
                            } else {
                                emit(queue, SolverEvent.conflict(col, rTemp, attempts));
                                board[col] = 0;
                                emit(queue, SolverEvent.remove(col, rTemp, attempts));
                                rTemp++;
                                row = rTemp;
                                attempts++;
                            }
                        }
                        if (row == 0) {
                            row++;
                            break;
                        } else {
                            board[col] = 0;
                            col--;
                        }
                    }
                }
            }

            if (row > n) {
                mainRow++;
                if (mainRow > n) break;
                board[col] = mainRow;
                emit(queue, SolverEvent.place(col, mainRow, attempts));
                row = 1;
                col++;
                attempts++;
            }
        }

        if (Thread.currentThread().isInterrupted()) return;

        if (col >= n) {
            emit(queue, SolverEvent.solution(attempts));
        } else {
            emit(queue, SolverEvent.done(attempts));
        }
    }

    private boolean checkState(int[] board, int n, int col, int row) {
        for (int a = 0; a < n; a++) {
            for (int b = 0; b < n; b++) {
                if (a != b && board[a] == board[b] && board[a] != 0 && board[b] != 0) {
                    return false;
                }
            }
        }

        int nCol = col - 1;
        int nRow = row - 1;
        while (nCol >= 0 && nRow > 0) {
            if (board[nCol] == nRow) return false;
            nCol--;
            nRow--;
        }

        nCol = col + 1;
        nRow = row - 1;
        while (nCol < n && nRow > 0) {
            if (board[nCol] == nRow) return false;
            nCol++;
            nRow--;
        }

        nCol = col - 1;
        nRow = row + 1;
        while (nCol >= 0 && nRow <= n) {
            if (board[nCol] == nRow) return false;
            nCol--;
            nRow++;
        }

        nCol = col + 1;
        nRow = row + 1;
        while (nCol < n && nRow <= n) {
            if (board[nCol] == nRow) return false;
            nCol++;
            nRow++;
        }

        return true;
    }

    private void emit(BlockingQueue<SolverEvent> queue, SolverEvent event) {
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

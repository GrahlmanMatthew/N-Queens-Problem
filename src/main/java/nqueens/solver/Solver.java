package nqueens.solver;

import nqueens.model.SolverEvent;

import java.util.concurrent.BlockingQueue;

public interface Solver {
    void solve(int n, BlockingQueue<SolverEvent> queue);
    String name();
}

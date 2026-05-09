package nqueens.solver;

import nqueens.model.SolverEvent;
import nqueens.model.SolverEventType;
import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class HeuristicSolverTest {

    private final HeuristicSolver solver = new HeuristicSolver();

    @Test
    void name_returns_expected_label() {
        assertEquals("Heuristic Search (A*)", solver.name());
    }

    @Test
    void solves_four_queens() throws Exception {
        SolverEvent result = runUntilTerminal(4, 10);
        assertEquals(SolverEventType.SOLUTION, result.type());
    }

    @Test
    void solves_eight_queens() throws Exception {
        SolverEvent result = runUntilTerminal(8, 30);
        assertEquals(SolverEventType.SOLUTION, result.type());
    }

    @Test
    void emits_clear_events_between_board_states() throws Exception {
        LinkedBlockingQueue<SolverEvent> queue = new LinkedBlockingQueue<>();
        Thread thread = new Thread(() -> solver.solve(4, queue));
        thread.setDaemon(true);
        thread.start();

        boolean sawClear = false;
        while (true) {
            SolverEvent event = queue.poll(10, TimeUnit.SECONDS);
            assertNotNull(event, "solver timed out");
            if (event.type() == SolverEventType.CLEAR) sawClear = true;
            if (event.type() == SolverEventType.SOLUTION || event.type() == SolverEventType.DONE) break;
        }
        thread.join(1000);
        assertTrue(sawClear, "expected at least one CLEAR event from heuristic solver");
    }

    private SolverEvent runUntilTerminal(int n, int timeoutSeconds) throws Exception {
        LinkedBlockingQueue<SolverEvent> queue = new LinkedBlockingQueue<>();
        Thread thread = new Thread(() -> solver.solve(n, queue));
        thread.setDaemon(true);
        thread.start();

        while (true) {
            SolverEvent event = queue.poll(timeoutSeconds, TimeUnit.SECONDS);
            assertNotNull(event, "solver timed out for N=" + n);
            if (event.type() == SolverEventType.SOLUTION || event.type() == SolverEventType.DONE) {
                thread.join(1000);
                return event;
            }
        }
    }
}

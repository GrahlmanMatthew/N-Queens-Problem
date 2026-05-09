package nqueens.solver;

import nqueens.model.SolverEvent;
import nqueens.model.SolverEventType;
import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BlindSearchSolverTest {

    private final BlindSearchSolver solver = new BlindSearchSolver();

    @Test
    void name_returns_expected_label() {
        assertEquals("Blind Search (DFS)", solver.name());
    }

    @Test
    void solves_four_queens() throws Exception {
        SolverEvent result = runUntilTerminal(4, 5);
        assertEquals(SolverEventType.SOLUTION, result.type());
    }

    @Test
    void solves_eight_queens_in_expected_attempts() throws Exception {
        SolverEvent result = runUntilTerminal(8, 10);
        assertEquals(SolverEventType.SOLUTION, result.type());
        assertEquals(764, result.attemptCount());
    }

    @Test
    void reports_no_solution_for_two_queens() throws Exception {
        SolverEvent result = runUntilTerminal(2, 5);
        assertEquals(SolverEventType.DONE, result.type());
    }

    @Test
    void reports_no_solution_for_three_queens() throws Exception {
        SolverEvent result = runUntilTerminal(3, 5);
        assertEquals(SolverEventType.DONE, result.type());
    }

    @Test
    void emits_place_events_before_solution() throws Exception {
        LinkedBlockingQueue<SolverEvent> queue = new LinkedBlockingQueue<>();
        Thread thread = new Thread(() -> solver.solve(4, queue));
        thread.setDaemon(true);
        thread.start();

        boolean sawPlace = false;
        while (true) {
            SolverEvent event = queue.poll(5, TimeUnit.SECONDS);
            assertNotNull(event, "solver timed out");
            if (event.type() == SolverEventType.PLACE) sawPlace = true;
            if (event.type() == SolverEventType.SOLUTION || event.type() == SolverEventType.DONE) break;
        }
        thread.join(1000);
        assertTrue(sawPlace, "expected at least one PLACE event before terminal");
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

package nqueens.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolverEventTest {

    @Test
    void place_factory_sets_correct_fields() {
        SolverEvent event = SolverEvent.place(3, 5, 42);
        assertEquals(SolverEventType.PLACE, event.type());
        assertEquals(3, event.col());
        assertEquals(5, event.row());
        assertEquals(42, event.attemptCount());
    }

    @Test
    void remove_factory_sets_correct_fields() {
        SolverEvent event = SolverEvent.remove(1, 2, 10);
        assertEquals(SolverEventType.REMOVE, event.type());
        assertEquals(1, event.col());
        assertEquals(2, event.row());
        assertEquals(10, event.attemptCount());
    }

    @Test
    void conflict_factory_sets_correct_fields() {
        SolverEvent event = SolverEvent.conflict(0, 4, 7);
        assertEquals(SolverEventType.CONFLICT, event.type());
        assertEquals(0, event.col());
        assertEquals(4, event.row());
        assertEquals(7, event.attemptCount());
    }

    @Test
    void clear_factory_uses_sentinel_coordinates() {
        SolverEvent event = SolverEvent.clear(99);
        assertEquals(SolverEventType.CLEAR, event.type());
        assertEquals(-1, event.col());
        assertEquals(-1, event.row());
        assertEquals(99, event.attemptCount());
    }

    @Test
    void solution_factory_sets_correct_type() {
        SolverEvent event = SolverEvent.solution(764);
        assertEquals(SolverEventType.SOLUTION, event.type());
        assertEquals(764, event.attemptCount());
    }

    @Test
    void done_factory_sets_correct_type() {
        SolverEvent event = SolverEvent.done(5);
        assertEquals(SolverEventType.DONE, event.type());
        assertEquals(5, event.attemptCount());
    }
}

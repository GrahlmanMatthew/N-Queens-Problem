package nqueens.model;

public record SolverEvent(SolverEventType type, int col, int row, int attemptCount) {

    public static SolverEvent place(int col, int row, int attempts) {
        return new SolverEvent(SolverEventType.PLACE, col, row, attempts);
    }

    public static SolverEvent remove(int col, int row, int attempts) {
        return new SolverEvent(SolverEventType.REMOVE, col, row, attempts);
    }

    public static SolverEvent conflict(int col, int row, int attempts) {
        return new SolverEvent(SolverEventType.CONFLICT, col, row, attempts);
    }

    public static SolverEvent clear(int attempts) {
        return new SolverEvent(SolverEventType.CLEAR, -1, -1, attempts);
    }

    public static SolverEvent solution(int attempts) {
        return new SolverEvent(SolverEventType.SOLUTION, -1, -1, attempts);
    }

    public static SolverEvent done(int attempts) {
        return new SolverEvent(SolverEventType.DONE, -1, -1, attempts);
    }
}

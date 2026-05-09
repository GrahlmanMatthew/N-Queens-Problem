package nqueens.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import nqueens.solver.BlindSearchSolver;
import nqueens.solver.HeuristicSolver;

public class CompetitionView extends HBox {

    public enum RunState { IDLE, RUNNING, PAUSED }

    private final SolverLane blindLane;
    private final SolverLane heuristicLane;
    private RunState runState = RunState.IDLE;

    public CompetitionView(int n) {
        blindLane     = new SolverLane(new BlindSearchSolver(), n);
        heuristicLane = new SolverLane(new HeuristicSolver(), n);

        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(20, 20, 10, 20));
        getChildren().addAll(blindLane, heuristicLane);
    }

    public RunState getRunState() {
        return runState;
    }

    public void start(int n) {
        blindLane.start(n);
        heuristicLane.start(n);
        runState = RunState.RUNNING;
    }

    public void pause() {
        blindLane.pause();
        heuristicLane.pause();
        runState = RunState.PAUSED;
    }

    public void resume() {
        blindLane.resume();
        heuristicLane.resume();
        runState = RunState.RUNNING;
    }

    public void reset(int n) {
        blindLane.reset(n);
        heuristicLane.reset(n);
        runState = RunState.IDLE;
    }

    public void setTickIntervalMs(double ms) {
        blindLane.setTickIntervalMs(ms);
        heuristicLane.setTickIntervalMs(ms);
    }
}

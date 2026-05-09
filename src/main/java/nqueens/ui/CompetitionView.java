package nqueens.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import nqueens.solver.BlindSearchSolver;
import nqueens.solver.HeuristicSolver;

public class CompetitionView extends HBox {

    private final SolverLane blindLane;
    private final SolverLane heuristicLane;

    public CompetitionView(int n) {
        blindLane     = new SolverLane(new BlindSearchSolver(), n);
        heuristicLane = new SolverLane(new HeuristicSolver(), n);

        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(20, 20, 10, 20));
        getChildren().addAll(blindLane, heuristicLane);
    }

    public void start(int n) {
        blindLane.start(n);
        heuristicLane.start(n);
    }

    public void stop() {
        blindLane.stop();
        heuristicLane.stop();
    }

    public void setTickIntervalMs(double ms) {
        blindLane.setTickIntervalMs(ms);
        heuristicLane.setTickIntervalMs(ms);
    }
}

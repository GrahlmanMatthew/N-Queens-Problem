package nqueens.ui;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import nqueens.model.SolverEvent;
import nqueens.model.SolverEventType;
import nqueens.solver.Solver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SolverLane extends VBox {

    private final Solver solver;
    private final BoardGridPane board;
    private final Label attemptsLabel;
    private final Label statusLabel;

    private BlockingQueue<SolverEvent> queue;
    private AnimationTimer animationTimer;
    private Thread solverThread;
    private long startNanos;

    private volatile double tickIntervalMs = 100;

    public SolverLane(Solver solver, int n) {
        this.solver = solver;
        this.board = new BoardGridPane(n);

        Label nameLabel = new Label(solver.name());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #e0e0e0;");

        attemptsLabel = new Label("Attempts: 0");
        attemptsLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #a0d0a0;");

        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");

        setAlignment(Pos.CENTER);
        setSpacing(10);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #2a2a3a; -fx-background-radius: 8;");
        getChildren().addAll(nameLabel, board, attemptsLabel, statusLabel);
    }

    public void start(int n) {
        stop();
        board.reset(n);
        attemptsLabel.setText("Attempts: 0");
        statusLabel.setText("Running...");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64b5f6;");
        startNanos = System.nanoTime();

        queue = new LinkedBlockingQueue<>(50_000);
        solverThread = new Thread(() -> solver.solve(n, queue));
        solverThread.setDaemon(true);
        solverThread.start();

        animationTimer = buildAnimationTimer();
        animationTimer.start();
    }

    public void pause() {
        if (animationTimer != null) animationTimer.stop();
    }

    public void resume() {
        if (animationTimer != null) animationTimer.start();
    }

    public void stop() {
        if (animationTimer != null) animationTimer.stop();
        if (solverThread != null) solverThread.interrupt();
    }

    public void reset(int n) {
        stop();
        board.reset(n);
        attemptsLabel.setText("Attempts: 0");
        statusLabel.setText("Ready");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");
    }

    public void setTickIntervalMs(double ms) {
        this.tickIntervalMs = ms;
    }

    private AnimationTimer buildAnimationTimer() {
        return new AnimationTimer() {
            private long lastTickNanos = 0;

            @Override
            public void handle(long now) {
                double intervalNs = tickIntervalMs * 1_000_000;
                if (now - lastTickNanos < intervalNs) return;
                lastTickNanos = now;

                int eventsPerTick = Math.max(1, (int)(500 / tickIntervalMs));
                for (int i = 0; i < eventsPerTick; i++) {
                    SolverEvent event = queue.poll();
                    if (event == null) break;

                    board.applyEvent(event);
                    attemptsLabel.setText("Attempts: " + event.attemptCount());

                    if (event.type() == SolverEventType.SOLUTION) {
                        double elapsed = (System.nanoTime() - startNanos) / 1e9;
                        statusLabel.setText(String.format("Solved in %.2fs", elapsed));
                        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #43A047;");
                        stop();
                        break;
                    }
                    if (event.type() == SolverEventType.DONE) {
                        statusLabel.setText("No solution found");
                        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #e53935;");
                        stop();
                        break;
                    }
                }
            }
        };
    }
}

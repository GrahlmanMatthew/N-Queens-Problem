package nqueens.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import nqueens.model.SolverEvent;

public class BoardGridPane extends GridPane {

    private static final Color LIGHT_SQUARE = Color.web("#F0D9B5");
    private static final Color DARK_SQUARE  = Color.web("#B58863");
    private static final Color CONFLICT     = Color.web("#e53935");
    private static final Color SOLUTION     = Color.web("#43A047");
    private static final String QUEEN_SYMBOL = "♛";

    private Rectangle[][] backgrounds;
    private Label[][] queens;
    private int n;
    private double cellSize;

    public BoardGridPane(int n) {
        reset(n);
    }

    public final void reset(int n) {
        this.n = n;
        this.cellSize = Math.max(28, Math.min(70, 560.0 / n));
        getChildren().clear();
        backgrounds = new Rectangle[n][n];
        queens = new Label[n][n];

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                StackPane cell = buildCell(col, row);
                add(cell, col, row);
            }
        }
    }

    public void applyEvent(SolverEvent event) {
        switch (event.type()) {
            case PLACE    -> showQueen(event.col(), event.row() - 1);
            case REMOVE   -> clearCell(event.col(), event.row() - 1);
            case CONFLICT -> flashConflict(event.col(), event.row() - 1);
            case CLEAR    -> clearAll();
            case SOLUTION -> highlightSolution();
            case DONE     -> {}
        }
    }

    private StackPane buildCell(int col, int row) {
        Rectangle bg = new Rectangle(cellSize, cellSize);
        bg.setFill(naturalColor(col, row));
        backgrounds[col][row] = bg;

        Label queen = new Label(QUEEN_SYMBOL);
        queen.setStyle("-fx-font-size: " + (cellSize * 0.6) + "px; -fx-text-fill: white;");
        queen.setVisible(false);
        queens[col][row] = queen;

        StackPane cell = new StackPane(bg, queen);
        return cell;
    }

    private void showQueen(int col, int boardRow) {
        // Backtracking moves a queen to a new row in the same column without
        // always emitting REMOVE first — clear the whole column before placing.
        for (int row = 0; row < n; row++) {
            if (queens[col][row].isVisible()) {
                clearCell(col, row);
            }
        }
        queens[col][boardRow].setVisible(true);
        backgrounds[col][boardRow].setFill(naturalColor(col, boardRow));
    }

    private void clearCell(int col, int boardRow) {
        queens[col][boardRow].setVisible(false);
        backgrounds[col][boardRow].setFill(naturalColor(col, boardRow));
    }

    private void flashConflict(int col, int boardRow) {
        backgrounds[col][boardRow].setFill(CONFLICT);
    }

    private void clearAll() {
        for (int col = 0; col < n; col++) {
            for (int row = 0; row < n; row++) {
                clearCell(col, row);
            }
        }
    }

    private void highlightSolution() {
        for (int col = 0; col < n; col++) {
            for (int row = 0; row < n; row++) {
                if (queens[col][row].isVisible()) {
                    backgrounds[col][row].setFill(SOLUTION);
                }
            }
        }
    }

    private Color naturalColor(int col, int row) {
        return (col + row) % 2 == 0 ? LIGHT_SQUARE : DARK_SQUARE;
    }
}

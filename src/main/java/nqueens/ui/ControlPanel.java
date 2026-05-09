package nqueens.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;

public class ControlPanel extends HBox {

    private static final int N_MIN     = 4;
    private static final int N_MAX     = 20;
    private static final int N_DEFAULT = 8;

    private final CompetitionView view;
    private final Spinner<Integer> nSpinner;

    public ControlPanel(CompetitionView view) {
        this.view = view;

        nSpinner = new Spinner<>(N_MIN, N_MAX, N_DEFAULT);
        nSpinner.setEditable(true);
        nSpinner.setPrefWidth(70);

        Slider speedSlider = buildSpeedSlider();

        Button startButton = new Button("▶  Start");
        startButton.setStyle(buttonStyle("#1565C0"));
        startButton.setOnAction(e -> view.start(nSpinner.getValue()));

        Button resetButton = new Button("↺  Reset");
        resetButton.setStyle(buttonStyle("#424242"));
        resetButton.setOnAction(e -> {
            view.stop();
            view.start(nSpinner.getValue());
        });

        setAlignment(Pos.CENTER);
        setSpacing(16);
        setPadding(new Insets(14, 20, 20, 20));
        setStyle("-fx-background-color: #1e1e2e;");
        getChildren().addAll(
                styledLabel("N:"), nSpinner,
                styledLabel("Speed:"), speedSlider,
                startButton, resetButton
        );
    }

    private Slider buildSpeedSlider() {
        Slider slider = new Slider(1, 20, 5);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setMinorTickCount(4);
        slider.setPrefWidth(160);
        slider.setStyle("-fx-control-inner-background: #3a3a4a;");

        updateSpeed(slider.getValue());
        slider.valueProperty().addListener((obs, oldVal, newVal) -> updateSpeed(newVal.doubleValue()));
        return slider;
    }

    private void updateSpeed(double sliderValue) {
        // sliderValue 1-20 → tickIntervalMs 500ms (slow) to 25ms (fast)
        double tickMs = 500.0 / sliderValue;
        view.setTickIntervalMs(tickMs);
    }

    private Label styledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #b0b0c0; -fx-font-size: 13px;");
        return label;
    }

    private String buttonStyle(String bgColor) {
        return "-fx-background-color: " + bgColor + "; -fx-text-fill: white; "
                + "-fx-font-size: 13px; -fx-padding: 8 16; -fx-background-radius: 4; "
                + "-fx-cursor: hand;";
    }
}

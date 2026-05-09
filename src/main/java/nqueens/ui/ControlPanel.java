package nqueens.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import nqueens.ui.CompetitionView.RunState;

public class ControlPanel extends HBox {

    private static final int N_MIN     = 4;
    private static final int N_MAX     = 20;
    private static final int N_DEFAULT = 8;

    private static final String STYLE_PLAY  = "#1565C0";
    private static final String STYLE_PAUSE = "#E65100";
    private static final String STYLE_RESET = "#424242";

    private final CompetitionView view;
    private final Spinner<Integer> nSpinner;
    private final Button toggleButton;

    public ControlPanel(CompetitionView view) {
        this.view = view;

        nSpinner = new Spinner<>(N_MIN, N_MAX, N_DEFAULT);
        nSpinner.setEditable(true);
        nSpinner.setPrefWidth(70);

        Slider speedSlider = buildSpeedSlider();

        toggleButton = new Button("▶  Start");
        toggleButton.setStyle(buttonStyle(STYLE_PLAY));
        toggleButton.setOnAction(e -> handleToggle());

        Button resetButton = new Button("↺  Reset");
        resetButton.setStyle(buttonStyle(STYLE_RESET));
        resetButton.setOnAction(e -> handleReset());

        setAlignment(Pos.CENTER);
        setSpacing(16);
        setPadding(new Insets(14, 20, 20, 20));
        setStyle("-fx-background-color: #1e1e2e;");
        getChildren().addAll(
                styledLabel("N:"), nSpinner,
                styledLabel("Speed:"), speedSlider,
                toggleButton, resetButton
        );
    }

    private void handleToggle() {
        switch (view.getRunState()) {
            case IDLE -> {
                view.start(nSpinner.getValue());
                toggleButton.setText("⏸  Pause");
                toggleButton.setStyle(buttonStyle(STYLE_PAUSE));
            }
            case RUNNING -> {
                view.pause();
                toggleButton.setText("▶  Resume");
                toggleButton.setStyle(buttonStyle(STYLE_PLAY));
            }
            case PAUSED -> {
                view.resume();
                toggleButton.setText("⏸  Pause");
                toggleButton.setStyle(buttonStyle(STYLE_PAUSE));
            }
        }
    }

    private void handleReset() {
        view.reset(nSpinner.getValue());
        toggleButton.setText("▶  Start");
        toggleButton.setStyle(buttonStyle(STYLE_PLAY));
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

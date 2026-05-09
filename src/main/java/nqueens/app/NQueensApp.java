package nqueens.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nqueens.ui.CompetitionView;
import nqueens.ui.ControlPanel;

public class NQueensApp extends Application {

    private static final int DEFAULT_N = 8;

    @Override
    public void start(Stage stage) {
        CompetitionView competitionView = new CompetitionView(DEFAULT_N);
        ControlPanel controlPanel = new ControlPanel(competitionView);

        BorderPane root = new BorderPane();
        root.setCenter(competitionView);
        root.setBottom(controlPanel);
        root.setStyle("-fx-background-color: #1a1a2e;");

        Scene scene = new Scene(root);
        stage.setTitle("N-Queens Problem");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Auto-start so the boards are animated on launch
        competitionView.start(DEFAULT_N);
    }

    @Override
    public void stop() {
        // Ensure solver threads are cleaned up when the window is closed
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package br.edu.ifsp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifsp/view/main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("FitTech");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}

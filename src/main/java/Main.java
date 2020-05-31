import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static javafx.application.Platform.exit;

public class Main extends Application {
private static Stage stage;

public Main(){

}

    public void start(Stage primaryStage) throws Exception {
    stage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 300.0D, 140.0D));
        primaryStage.setOnCloseRequest((e) -> {
            exit();
        });
        primaryStage.show();



    }
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }
}

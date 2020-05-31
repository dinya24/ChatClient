import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField tfLogin;
    @FXML
    private PasswordField tfPass;
    @FXML
    private Label lResponse;
    public ChatController chatController;
    public Scene scene;
    private static LoginController instance;

    public LoginController() {
        instance = this;
    }

    public static LoginController getInstance() {
        return instance;
    }

    public void showResponse(String message) {
        Platform.runLater(() -> {
            this.lResponse.setText(message);
            this.lResponse.setTextFill(Color.web("#ff7e7e"));
        });
    }

    public void loginButtonAction() throws IOException {
        FXMLLoader fmxlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource("/ChatView.fxml"));
        Parent window = (BorderPane)fmxlLoader.load();
        this.chatController = (ChatController)fmxlLoader.getController();
        ClientConnection clientConnection = new ClientConnection(this.tfLogin.getText(), this.tfPass.getText(), this.chatController);
        Thread x = new Thread(clientConnection);
        x.start();
        this.scene = new Scene(window);
    }

    public void registerButtonAction() throws IOException {
        FXMLLoader fmxlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource("/ChatView.fxml"));
        Parent window = (BorderPane)fmxlLoader.load();
        this.chatController = (ChatController)fmxlLoader.getController();
        ClientConnection clientConnection = new ClientConnection(this.tfLogin.getText(), this.tfPass.getText(), this.chatController);
        clientConnection.setRegister(true);
        Thread x = new Thread(clientConnection);
        x.start();
        this.scene = new Scene(window);
    }

    public void showScene() throws IOException {
        Platform.runLater(() -> {
            Stage stage = (Stage)this.tfLogin.getScene().getWindow();
            stage.setResizable(true);
            stage.setWidth(500.0D);
            stage.setHeight(400.0D);
            stage.setOnCloseRequest((e) -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setScene(this.scene);
            stage.setMinWidth(300.0D);
            stage.setMinHeight(300.0D);
            stage.centerOnScreen();
        });
    }
}

package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ChatApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent auth = FXMLLoader.load(getClass().getResource("fxml/auth.fxml"));
        primaryStage.setTitle("Welcome to the GeekChat");
        primaryStage.getIcons().add(new Image("project/icon.png"));
        primaryStage.setScene(new Scene(auth));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
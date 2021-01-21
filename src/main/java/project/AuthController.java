package project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    public TextField login;
    public TextField password;
    Client client;

    public void enter(ActionEvent actionEvent) throws IOException {

        if(!login.getText().trim().isEmpty() && !password.getText().trim().isEmpty()){
            try {
                client = Client.getInstance();
                client.write("/auth " + login.getText().trim() + " " + password.getText().trim());
            } catch (IOException e) {
                System.out.println("Server not found");
                return;
            }
        } else {
            login.clear();
            login.setPromptText("Login or password is Empty");
            password.clear();
            return;
        }
        if (client.isAuthSuccess()) {
            Parent chat = FXMLLoader.load(getClass().getResource("fxml/chat.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Chat");
            stage.getIcons().add(new Image("project/icon.png"));
            stage.setScene(new Scene(chat));
            stage.setResizable(false);
            stage.show();
            login.getScene().getWindow().hide();
        } else {
            login.clear();
            login.setPromptText("Wrong login or password");
            password.clear();
        }
    }

    public void reg(ActionEvent actionEvent) throws IOException {
        Parent chat = FXMLLoader.load(getClass().getResource("fxml/registration.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Registration");
        stage.getIcons().add(new Image("project/icon.png"));
        stage.setScene(new Scene(chat));
        stage.setResizable(false);
        stage.show();
        login.getScene().getWindow().hide();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        try {
//            client = Client.getInstance();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
package com.wiktoria.flow.flowsmapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {
    private Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #f0f2f5;");

        Label title = new Label("Flow");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #7e72bf;");

        TextField loginField = new TextField();
        loginField.setPromptText("Email lub Login");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Hasło");

        Button loginBtn = new Button("Zaloguj się");
        loginBtn.setPrefWidth(200);
        loginBtn.setStyle("-fx-background-color: #7e72bf; -fx-text-fill: white; -fx-font-weight: bold;");
        loginBtn.setOnAction(e -> handleLogin(loginField.getText(), passField.getText()));

        Label noAccount = new Label("Nie masz konta?");
        Button registerBtn = new Button("Utwórz nowe konto");
        registerBtn.setPrefWidth(200);
        registerBtn.setStyle("-fx-background-color: #5f7bc4; -fx-text-fill: white; -fx-font-weight: bold;");
        registerBtn.setOnAction(e -> stage.setScene(new RegisterView(stage).getScene()));

        layout.getChildren().addAll(title, loginField, passField, loginBtn, new Separator(), noAccount, registerBtn);
        return new Scene(layout, 400, 500);
    }

    private void handleLogin(String login, String pass) {
        for (User u : DataManager.getInstance().getUsers()) {
            if (u.login(login, pass)) {
                DataManager.getInstance().setCurrentUser(u);
                stage.setScene(new FeedView(stage).getScene());
                return;
            }
        }
        showAlert("Błąd", "Nieprawidłowy login lub hasło.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
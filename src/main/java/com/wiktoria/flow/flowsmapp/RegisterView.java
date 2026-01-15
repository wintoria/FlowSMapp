package com.wiktoria.flow.flowsmapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterView {
    private Stage stage;

    public RegisterView(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #f0f2f5;");

        Label title = new Label("Utwórz konto");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField userField = new TextField();
        userField.setPromptText("Nazwa użytkownika");

        TextField emailField = new TextField();
        emailField.setPromptText("Adres email");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Hasło");

        Button regBtn = new Button("Zarejestruj się");
        regBtn.setPrefWidth(200);
        regBtn.setStyle("-fx-background-color: #5f7bc4; -fx-text-fill: white; -fx-font-weight: bold;");
        regBtn.setOnAction(e -> handleRegister(userField.getText(), emailField.getText(), passField.getText()));

        Button backBtn = new Button("Wróć do logowania");
        backBtn.setOnAction(e -> stage.setScene(new LoginView(stage).getScene()));

        layout.getChildren().addAll(title, userField, emailField, passField, regBtn, new Separator(), backBtn);
        return new Scene(layout, 400, 550);
    }

    private void handleRegister(String user, String email, String pass) {
        if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            showAlert("Błąd", "Wypełnij wszystkie pola!");
            return;
        }
        for (User u : DataManager.getInstance().getUsers()) {
            if (u.getUsername().equals(user)) {
                showAlert("Błąd", "Użytkownik o takim loginie już istnieje.");
                return;
            }
        }
        RegularUser newUser = new RegularUser(email, user, pass);
        DataManager.getInstance().addUser(newUser);
        showAlert("Sukces", "Konto utworzone! Możesz się teraz zalogować.");
        stage.setScene(new LoginView(stage).getScene());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
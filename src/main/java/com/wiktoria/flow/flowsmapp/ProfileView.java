package com.wiktoria.flow.flowsmapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;

public class ProfileView {
    private Stage stage;
    private User profileUser;
    private User currentUser;

    public ProfileView(Stage stage, User profileUser) {
        this.stage = stage;
        this.profileUser = profileUser;
        this.currentUser = DataManager.getInstance().getCurrentUser();
    }

    public Scene getScene() {
        BorderPane layout = new BorderPane();

        // GÓRA PROFILU
        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20));
        topBox.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 0, 5, 0, 0);");

        Button backBtn = new Button("← Wróć");
        backBtn.setAlignment(Pos.TOP_LEFT);
        backBtn.setOnAction(e -> stage.setScene(new FeedView(stage).getScene()));
        HBox nav = new HBox(backBtn);

        Label userLabel = new Label(profileUser.getUsername());
        userLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // BIO
        Label bioLabel = new Label(profileUser.getProfile().getBio());
        bioLabel.setWrapText(true);
        bioLabel.setStyle("-fx-font-style: italic; -fx-padding: 10;");

        VBox bioContainer = new VBox(bioLabel);
        bioContainer.setAlignment(Pos.CENTER);

        topBox.getChildren().addAll(nav, userLabel, bioContainer);

        // Przycisk Edycji BIO
        if (currentUser.getUsername().equals(profileUser.getUsername())) {
            Button editBioBtn = new Button("Edytuj BIO");
            editBioBtn.setOnAction(e -> enableBioEdit(bioContainer, bioLabel));
            topBox.getChildren().add(editBioBtn);
        }

        // ZAKŁADKI
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // 1. ZAKŁADKA: POSTY
        Tab postsTab = new Tab("Posty (" + DataManager.getInstance().getUserPosts(profileUser).size() + ")");
        postsTab.setContent(createPostsList());

        // 2. ZAKŁADKA: OBSERWUJE
        Tab followingTab = new Tab("Obserwuje (" + profileUser.getFollowing().size() + ")");
        followingTab.setContent(createUserList(profileUser.getFollowing()));

        // 3. ZAKŁADKA: OBSERWUJACY
        Tab followersTab = new Tab("Obserwujący (" + profileUser.getFollowers().size() + ")");
        followersTab.setContent(createUserList(profileUser.getFollowers()));

        tabPane.getTabs().addAll(postsTab, followingTab, followersTab);

        layout.setTop(topBox);
        layout.setCenter(tabPane);

        return new Scene(layout, 500, 700);
    }

    // EDYCJA BIO W MIEJSCU
    private void enableBioEdit(VBox container, Label label) {
        container.getChildren().clear();

        TextArea editArea = new TextArea(profileUser.getProfile().getBio());
        editArea.setWrapText(true);
        editArea.setMaxWidth(300);
        editArea.setPrefRowCount(3);

        Button saveBtn = new Button("Zapisz");
        saveBtn.setOnAction(e -> {
            String newBio = editArea.getText();
            profileUser.getProfile().updateBio(newBio);
            DataManager.getInstance().saveDataTrigger();
            // Odśwież widok (przeładuj scenę)
            stage.setScene(new ProfileView(stage, profileUser).getScene());
        });

        container.getChildren().addAll(editArea, saveBtn);
    }

    private Node createPostsList() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");

        for (Post p : DataManager.getInstance().getUserPosts(profileUser)) {
            VBox card = new VBox(5);
            card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #ddd; -fx-background-radius: 5;");

            Label date = new Label(sdf.format(p.getCreatedAt()));
            date.setStyle("-fx-text-fill: gray; -fx-font-size: 10px;");

            Label body = new Label(p.getTextBody());
            body.setWrapText(true);

            card.getChildren().addAll(date, body);
            box.getChildren().add(card);
        }
        ScrollPane sp = new ScrollPane(box);
        sp.setFitToWidth(true);
        return sp;
    }

    private Node createUserList(java.util.List<String> usernames) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(15));

        for(String uname : usernames) {
            Label l = new Label("👤 " + uname);
            l.setStyle("-fx-padding: 5; -fx-font-size: 14px;");
            box.getChildren().add(l);
        }
        return new ScrollPane(box);
    }
}
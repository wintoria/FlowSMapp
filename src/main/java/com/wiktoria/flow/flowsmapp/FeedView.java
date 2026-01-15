package com.wiktoria.flow.flowsmapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;

public class FeedView {
    private Stage stage;
    private User currentUser;
    private VBox postContainer;

    public FeedView(Stage stage) {
        this.stage = stage;
        this.currentUser = DataManager.getInstance().getCurrentUser();
    }

    public Scene getScene() {
        BorderPane layout = new BorderPane();

        // HEADER
        HBox header = new HBox(15);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");
        header.setAlignment(Pos.CENTER_LEFT);

        Button myProfileBtn = new Button("Mój Profil");
        myProfileBtn.setOnAction(e -> stage.setScene(new ProfileView(stage, currentUser).getScene()));

        Button logoutBtn = new Button("Wyloguj");
        logoutBtn.setOnAction(e -> {
            DataManager.getInstance().setCurrentUser(null);
            stage.setScene(new LoginView(stage).getScene());
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(new Label("Flow"), spacer, myProfileBtn, logoutBtn);
        layout.setTop(header);

        // LISTA POSTÓW
        postContainer = new VBox(10);
        postContainer.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane(postContainer);
        scrollPane.setFitToWidth(true);
        layout.setCenter(scrollPane);

        // NOWY POST
        VBox createBox = new VBox(10);
        createBox.setPadding(new Insets(15));
        createBox.setStyle("-fx-background-color: white;");

        TextArea input = new TextArea();
        input.setPromptText("Napisz coś...");
        input.setPrefRowCount(3);
        input.setWrapText(true);

        Button pubBtn = new Button("Opublikuj");
        pubBtn.setStyle("-fx-background-color: #5f7bc4; -fx-text-fill: white;");
        pubBtn.setOnAction(e -> {
            if(!input.getText().isEmpty()) {
                Post p = currentUser.createPost(input.getText());
                DataManager.getInstance().addPost(p);
                refreshPosts();
                input.clear();
            }
        });

        createBox.getChildren().addAll(input, pubBtn);
        layout.setBottom(createBox);

        refreshPosts();
        return new Scene(layout, 500, 700);
    }

    private void refreshPosts() {
        postContainer.getChildren().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");

        for (Post post : DataManager.getInstance().getPosts()) {
            VBox card = new VBox(8);
            card.setPadding(new Insets(15));
            card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);");

            // HEADER POSTA
            HBox postHeader = new HBox(10);
            Hyperlink authorLink = new Hyperlink(post.getAuthor().getUsername());
            authorLink.setStyle("-fx-font-weight: bold; -fx-text-fill: #1c1e21; -fx-underline: false;");
            authorLink.setOnAction(e -> stage.setScene(new ProfileView(stage, post.getAuthor()).getScene()));

            Label dateLabel = new Label(sdf.format(post.getCreatedAt()));
            dateLabel.setTextFill(Color.GRAY);

            // FOLLOW BUTTON
            Button followBtn = null;
            if (!post.getAuthor().getUsername().equals(currentUser.getUsername())) {
                boolean isFollowing = currentUser.isFollowing(post.getAuthor().getUsername());
                followBtn = new Button(isFollowing ? "Obserwujesz" : "Obserwuj");
                followBtn.setStyle(isFollowing ? "-fx-background-color: #fff5f5;" : "-fx-background-color: #e7f3ff; -fx-text-fill: #7e72bf;");
                followBtn.setOnAction(e -> {
                    User target = post.getAuthor();
                    if (isFollowing) currentUser.unfollow(target);
                    else currentUser.follow(target);
                    DataManager.getInstance().saveDataTrigger();
                    refreshPosts();
                });
            }

            Region hSpacer = new Region();
            HBox.setHgrow(hSpacer, Priority.ALWAYS);
            postHeader.getChildren().addAll(authorLink, dateLabel, hSpacer);
            if (followBtn != null) postHeader.getChildren().add(followBtn);

            // TREŚĆ
            Label textLabel = new Label(post.getTextBody());
            textLabel.setWrapText(true);
            textLabel.setStyle("-fx-text-fill: black; -fx-font-size: 13px;");

            // MENU EDYCJI
            if (currentUser.getUsername().equals(post.getAuthor().getUsername()) || currentUser.getType().equals("ADMIN")) {
                MenuButton options = new MenuButton("⋮");

                if (currentUser.getUsername().equals(post.getAuthor().getUsername())) {
                    MenuItem editItem = new MenuItem("Edytuj");
                    editItem.setOnAction(e -> enableEditMode(card, textLabel, post));
                    options.getItems().add(editItem);
                }

                MenuItem deleteItem = new MenuItem("Usuń");
                deleteItem.setOnAction(e -> {
                    DataManager.getInstance().removePost(post);
                    refreshPosts();
                });
                options.getItems().add(deleteItem);
                postHeader.getChildren().add(options);
            }

            // AKCJE (LIKE & COMMENT)
            HBox actions = new HBox(15);
            boolean isLiked = post.hasLiked(currentUser);

            // SYMBOL SERCA
            Button likeBtn = new Button();
            if (isLiked) {
                likeBtn.setText("❤ Lubisz (" + post.getLikeCount() + ")");
                likeBtn.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-background-color: transparent;");
            } else {
                likeBtn.setText("❤ Polub (" + post.getLikeCount() + ")");
                likeBtn.setStyle("-fx-text-fill: black; -fx-background-color: transparent;");
            }

            likeBtn.setOnAction(e -> {
                if (isLiked) post.removeLike(currentUser);
                else post.addLike(currentUser);
                DataManager.getInstance().saveDataTrigger();
                refreshPosts();
            });

            Button commentBtn = new Button("💬 Komentarze (" + post.getComments().size() + ")");
            commentBtn.setStyle("-fx-background-color: transparent;");
            commentBtn.setOnAction(e -> stage.setScene(new CommentView(stage, post).getScene()));

            actions.getChildren().addAll(likeBtn, commentBtn);

            card.getChildren().addAll(postHeader, textLabel, new Separator(), actions);
            postContainer.getChildren().add(card);
        }
    }

    // METODA DO EDYCJI W TYM SAMYM OKNIE
    private void enableEditMode(VBox card, Label contentLabel, Content content) {
        int index = card.getChildren().indexOf(contentLabel);
        if (index == -1) return;

        // Pole edycji
        TextArea editArea = new TextArea(content.getTextBody());
        editArea.setWrapText(true);
        editArea.setPrefRowCount(3);

        // Kontener na przyciski Zapisz/Anuluj
        HBox editControls = new HBox(10);
        Button saveBtn = new Button("Zapisz");
        saveBtn.setStyle("-fx-background-color: #5f7bc4; -fx-text-fill: white;");

        Button cancelBtn = new Button("Anuluj");

        editControls.getChildren().addAll(saveBtn, cancelBtn);

        card.getChildren().remove(index);
        card.getChildren().add(index, editArea);
        card.getChildren().add(index + 1, editControls);

        // Logika przycisków
        saveBtn.setOnAction(e -> {
            content.edit(editArea.getText());
            DataManager.getInstance().saveDataTrigger();
            refreshPosts();
        });

        cancelBtn.setOnAction(e -> refreshPosts());
    }
}
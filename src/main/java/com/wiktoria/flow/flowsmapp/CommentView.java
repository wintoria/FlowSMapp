package com.wiktoria.flow.flowsmapp;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;

public class CommentView {
    private Stage stage;
    private Post post;
    private User currentUser;

    public CommentView(Stage stage, Post post) {
        this.stage = stage;
        this.post = post;
        this.currentUser = DataManager.getInstance().getCurrentUser();
    }

    public Scene getScene() {
        BorderPane layout = new BorderPane();

        Button backBtn = new Button("← Wróć");
        backBtn.setOnAction(e -> stage.setScene(new FeedView(stage).getScene()));
        layout.setTop(backBtn);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f0f2f5;");

        // ORYGINALNY POST
        VBox postCard = new VBox(10);
        postCard.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");

        Label author = new Label(post.getAuthor().getUsername());
        author.setStyle("-fx-font-weight: bold;");
        Label body = new Label(post.getTextBody());
        body.setWrapText(true);

        postCard.getChildren().addAll(author, body);

        Label commentHeader = new Label("Komentarze:");
        commentHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        VBox commentsContainer = new VBox(10);
        refreshComments(commentsContainer);

        ScrollPane scroll = new ScrollPane(new VBox(15, postCard, commentHeader, commentsContainer));
        scroll.setFitToWidth(true);
        layout.setCenter(scroll);

        // DODAWANIE
        HBox bottom = new HBox(10);
        bottom.setPadding(new Insets(10));
        bottom.setStyle("-fx-background-color: white;");

        TextArea input = new TextArea();
        input.setPromptText("Napisz komentarz...");
        input.setPrefRowCount(2);
        input.setWrapText(true);
        HBox.setHgrow(input, Priority.ALWAYS);

        Button send = new Button("Wyślij");
        send.setOnAction(e -> {
            if (!input.getText().isEmpty()) {
                post.addComment(new Comment(currentUser, input.getText(), post));
                DataManager.getInstance().saveDataTrigger();
                refreshComments(commentsContainer);
                input.clear();
            }
        });
        bottom.getChildren().addAll(input, send);
        layout.setBottom(bottom);

        return new Scene(layout, 500, 700);
    }

    private void refreshComments(VBox container) {
        container.getChildren().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");

        for (Comment c : post.getComments()) {
            VBox card = new VBox(5);
            card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 2, 0, 0, 1);");

            HBox header = new HBox(10);
            Label cAuth = new Label(c.getAuthor().getUsername());
            cAuth.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
            Label date = new Label(sdf.format(c.getCreatedAt()));
            date.setStyle("-fx-text-fill: gray; -fx-font-size: 10px;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            header.getChildren().addAll(cAuth, date, spacer);

            Label textLabel = new Label(c.getTextBody());
            textLabel.setWrapText(true);

            // Menu (Edit/Delete)
            if (currentUser.getUsername().equals(c.getAuthor().getUsername()) || currentUser.getType().equals("ADMIN")) {
                MenuButton options = new MenuButton("⋮");
                options.setStyle("-fx-font-size: 10px;");

                if (currentUser.getUsername().equals(c.getAuthor().getUsername())) {
                    MenuItem edit = new MenuItem("Edytuj");
                    edit.setOnAction(e -> enableEditMode(card, textLabel, c, container));
                    options.getItems().add(edit);
                }

                MenuItem del = new MenuItem("Usuń");
                del.setOnAction(e -> {
                    post.removeComment(c);
                    DataManager.getInstance().saveDataTrigger();
                    refreshComments(container);
                });
                options.getItems().add(del);
                header.getChildren().add(options);
            }

            card.getChildren().addAll(header, textLabel);
            container.getChildren().add(card);
        }
    }

    // Edycja komentarza w miejscu
    private void enableEditMode(VBox card, Label label, Comment comment, VBox container) {
        int index = card.getChildren().indexOf(label);
        if(index == -1) return;

        TextArea edit = new TextArea(comment.getTextBody());
        edit.setWrapText(true);
        edit.setPrefRowCount(2);

        Button save = new Button("Zapisz");
        save.setOnAction(e -> {
            comment.edit(edit.getText());
            DataManager.getInstance().saveDataTrigger();
            refreshComments(container);
        });

        card.getChildren().remove(index);
        card.getChildren().add(index, edit);
        card.getChildren().add(index + 1, save);
    }
}
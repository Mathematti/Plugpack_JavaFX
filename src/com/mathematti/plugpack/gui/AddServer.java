package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.Server;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;

public class AddServer {
    public static Server[] servers = new Server[0];

    public static void addServerGUI(Stage stage, String error) {
        stage.setTitle("Plugpack - Add new server");

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        final int width = 350;
        final int height = 200;
        stage.setScene(new Scene(vBox, width, height));

        Label nameInput = new Label("Server name:");
        TextField serverName = new TextField();
        Button confirm = new Button("Add");
        confirm.setOnAction(actionEvent -> addServer(stage, serverName.getText()));
        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> MainMenu.showMainGUI(stage));

        HBox hBox = new HBox();
        hBox.setSpacing(padding);
        hBox.getChildren().addAll(confirm, cancel);

        if (error.equals("exists")) {
            Label errorLabel = new Label("Server already exists!");
            errorLabel.setTextFill(Color.RED);
            vBox.getChildren().add(errorLabel);
        } else if (error.equals("empty")) {
            Label errorLabel = new Label("Server name can't be empty!");
            errorLabel.setTextFill(Color.RED);
            vBox.getChildren().add(errorLabel);
        }
        vBox.getChildren().addAll(nameInput, serverName, hBox);
    }

    private static void addServer(Stage stage, String name) {
        boolean error = false;
        if (name.equals("")) {
            addServerGUI(stage, "empty");
            error = true;
        }
        for (Server server : servers) {
            if (server.getName().equals(name)) {
                addServerGUI(stage, "exists");
                error = true;
            }
        }
        if (!error) {
            servers = Arrays.copyOf(servers, servers.length + 1);
            servers[servers.length - 1] = new Server(name);
            MainMenu.showMainGUI(stage);
        }
    }
}

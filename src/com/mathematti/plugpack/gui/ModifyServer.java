package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.Server;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mathematti.plugpack.Server.servers;

public class ModifyServer {

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
            modifyServerGUI(stage, name);
        }
    }

    public static void chooseServerGUI(Stage stage) {
        stage.setTitle("Plugpack - Modify servers");

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        FlowPane flowPane = new FlowPane();
        flowPane.setVgap(padding);
        flowPane.setHgap(padding);

        int width = 350;
        final int height = 200;
        stage.setScene(new Scene(vBox, width, height));

        Label top = new Label("Which server do you want to modify?");
        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> MainMenu.showMainGUI(stage));
        Button addServer = new Button("Add server");
        addServer.setOnAction(actionEvent -> addServerGUI(stage, ""));

        List<Button> buttonList = new ArrayList<>();

        for (Server server : servers) {
            buttonList.add(new Button(server.getName()));
        }

        for (int i = 0; i < buttonList.size(); i++) {
            int finalI = i;
            buttonList.get(i).setOnAction(actionEvent -> modifyServerGUI(stage, buttonList.get(finalI).getText()));
        }

        HBox hBox = new HBox();
        hBox.setSpacing(padding);

        hBox.getChildren().addAll(addServer, cancel);
        flowPane.getChildren().addAll(buttonList);
        vBox.getChildren().addAll(top, flowPane, hBox);
    }

    public static void modifyServerGUI(Stage stage, String serverName) {
        stage.setTitle("Plugpack - Modify " + serverName);

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        HBox hBox = new HBox();
        hBox.setSpacing(padding);

        int width = 350;
        final int height = 200;
        stage.setScene(new Scene(vBox, width, height));

        Label top = new Label("Modify " + serverName + ":");

        Button deleteServer = new Button("Delete");
        deleteServer.setOnAction(actionEvent -> {
            Server[] temp = servers.clone();
            servers = new Server[temp.length - 1];

            int placeAt = 0;
            for (Server server : temp) {
                if (!server.getName().equals(serverName)) {
                    servers[placeAt] = server;
                    placeAt++;
                }
            }
            chooseServerGUI(stage);
        });

        Button modifyPlugins = new Button("Modify plugins");

        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> chooseServerGUI(stage));

        hBox.getChildren().addAll(deleteServer, modifyPlugins);

        vBox.getChildren().addAll(top, hBox, cancel);
    }
}

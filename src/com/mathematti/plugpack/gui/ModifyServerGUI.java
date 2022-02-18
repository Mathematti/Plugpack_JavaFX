package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.functions.Server;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.mathematti.plugpack.functions.Server.servers;

public class ModifyServerGUI {
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
            ChooseServerGUI.chooseServerGUI(stage);
        });

        int serverSaveLoc = 0;
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getName().equals(serverName)) {
                serverSaveLoc = i;
                break;
            }
        }

        Button modifyPlugins = new Button("Modify plugins");
        int finalServerSaveLoc = serverSaveLoc;
        modifyPlugins.setOnAction(actionEvent -> ChoosePluginGUI.choosePluginGUI(stage, servers[finalServerSaveLoc]));

        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> ChooseServerGUI.chooseServerGUI(stage));

        hBox.getChildren().addAll(cancel, modifyPlugins, deleteServer);

        vBox.getChildren().addAll(top, hBox);
    }
}

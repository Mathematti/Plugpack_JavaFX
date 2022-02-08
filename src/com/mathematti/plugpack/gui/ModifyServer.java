package com.mathematti.plugpack.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ModifyServer {
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

        List<Button> buttonList = new ArrayList<>();

        for (int i = 0; i < AddServer.servers.length; i++) {
            buttonList.add(new Button(AddServer.servers[i].getName()));
        }

        for (int i = 0; i < buttonList.size(); i++) {
            int finalI = i;
            buttonList.get(i).setOnAction(actionEvent -> modifyServerGUI(stage, buttonList.get(finalI).getText()));
        }

        flowPane.getChildren().addAll(buttonList);
        vBox.getChildren().addAll(top, flowPane, cancel);
    }

    public static void modifyServerGUI(Stage stage, String serverName) {
        stage.setTitle("Plugpack - Modify " + serverName);

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        int width = 350;
        final int height = 200;
        stage.setScene(new Scene(vBox, width, height));

        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> MainMenu.showMainGUI(stage));

        vBox.getChildren().addAll(cancel);
    }
}

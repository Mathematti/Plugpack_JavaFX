package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.functions.Server;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ChooseServerGUI {
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
        cancel.setOnAction(actionEvent -> MainMenuGUI.showMainGUI(stage));
        Button addServer = new Button("Add server");
        addServer.setOnAction(actionEvent -> AddServerGUI.addServerGUI(stage, ""));

        List<Button> buttonList = new ArrayList<>();

        for (Server server : Server.servers) {
            buttonList.add(new Button(server.getName()));
        }

        for (int i = 0; i < buttonList.size(); i++) {
            int finalI = i;
            buttonList.get(i).setOnAction(actionEvent -> ModifyServerGUI.modifyServerGUI(stage, buttonList.get(finalI).getText()));
        }

        HBox hBox = new HBox();
        hBox.setSpacing(padding);

        hBox.getChildren().addAll(cancel, addServer);
        flowPane.getChildren().addAll(buttonList);
        vBox.getChildren().addAll(top, flowPane, hBox);
    }
}
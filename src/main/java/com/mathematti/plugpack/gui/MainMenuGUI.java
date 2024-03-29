package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.functions.Server;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Main menu of the Plugpack application
 * @author Mathematti
 */
public class MainMenuGUI extends Application {
    @Override
    public void start(Stage stage) {
        final int width = 350;
        final int height = 200;
        stage.setScene(new Scene(new Pane(), width, height));

        stage.show();
        showMainGUI(stage);
    }

    public static void showMainGUI(Stage stage) {
        stage.setTitle("Plugpack");

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        HBox hBox = new HBox();
        hBox.setSpacing(padding);

        final int width = 350;
        final int height = 200;
        stage.setScene(new Scene(vBox, width, height));

        Label main = new Label("Plugpack by Mathematti");
        main.setTextFill(Color.BLUE);
        Label servers = new Label("You currently have " + Server.servers.length + " servers set up.");
        Button modify = new Button("Modify servers");
        modify.setOnAction(actionEvent -> ChooseServerGUI.chooseServerGUI(stage));
        Button generateScript = new Button("Generate script");
        generateScript.setOnAction(actionEvent -> GenerateScriptGUI.generateScriptGUI(stage));
        Button importScript = new Button("Import script");
        importScript.setOnAction(actionEvent -> ImportScriptGUI.importScriptGUI(stage));

        hBox.getChildren().addAll(generateScript, importScript);

        vBox.getChildren().addAll(main, servers, modify, hBox);
    }
}

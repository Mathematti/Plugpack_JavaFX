package com.mathematti.plugpack.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Main menu of the Plugpack application
 * @author Mathematti
 */
public class MainMenu extends Application {
    @Override
    public void start(Stage stage) {
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
        Label servers = new Label("You currently have " + 0 + " servers set up.");
        Button modify = new Button("Modify servers");
        Button addServer = new Button("Add server");
        Button generateScript = new Button("Generate script");

        hBox.getChildren().addAll(modify, addServer);

        vBox.getChildren().addAll(main, servers, hBox, generateScript);

        stage.show();
    }
}
